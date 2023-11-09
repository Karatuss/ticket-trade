package com.ticket.Ticketing.controller;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.ticket.Ticketing.config.UserConfig;
import com.ticket.Ticketing.domain.repository.SessionConst;
import com.ticket.Ticketing.service.EventService;
import com.ticket.Ticketing.service.SeatService;
import com.ticket.Ticketing.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.List;

import static com.ticket.Ticketing.Ticketing.cluster;


// Mapping
@RestController
@AllArgsConstructor
@SessionAttributes("user")
public class PostController {

    @Autowired
    PasswordEncoder passwordEncoder;
    private static final int MAX_ATTEMPTS = 3;
    private static long rejectionEndTime = System.currentTimeMillis() + 180000;

    private final EventService eventService;
    private final SeatService seatService;
    private final UserService userService;

    // Register
    @PostMapping(value = "/register")
    public ResponseEntity<Object> register(@RequestBody HashMap<String, Object> registerData) {
        try {
            // put data to user bucket
            userService.registerUser(registerData);

            registerData.put("submit", true);

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(registerData);

        } catch (DuplicateKeyException E) {  // check duplicated id, phoneNumber, email
            registerData.put("submit", false);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(registerData);
        } catch (Exception e) { // network server connect error
            registerData.put("submit", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(registerData);
        }
    }

    // Login
    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(@RequestBody HashMap<String, Object> loginData, HttpServletRequest request) {
        try {
            Bucket userBucket = cluster.bucket(UserConfig.getStaticBucketName());
            Collection userCollection = userBucket.defaultCollection();

            // input id, pw
            Object userID = loginData.get("user_ID");
            Object userPW = loginData.get("user_PW");
            String loginUserID = (String) userID;

            // if not fully inputted id or pw, throw error
            if (((String) userID).isEmpty() || ((String) userPW).isEmpty()) {
                throw new LoginException();
            }

            // Couchbase pw for login
            Object checkedPW;

            // get data from DB that id is userID
            JsonObject content = userCollection.get(loginUserID).contentAsObject();
            checkedPW = content.get("password");

            // check login attempts
            content.put("loginAttempts", (int) content.get("loginAttempts") + 1);
            userCollection.replace(loginUserID, content);

            if ((int) content.get("loginAttempts") > MAX_ATTEMPTS) {
                long currentTime = System.currentTimeMillis();

                if (currentTime < rejectionEndTime) {
                    loginData.put("loginSuccess", false);
                    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                            .body(loginData);
                }

            }

            // hashing
            String rawPassword = String.valueOf(userPW);
            String encodedPassword = String.valueOf(checkedPW);

            // password does not equal as stored
            if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
                throw new LoginException();
            }

            // login success
            // reset login attempts
            content.put("loginAttempts", 0);
            userCollection.replace(loginUserID, content);

            // get login session
            HttpSession session = request.getSession(); // if no session exists, make a new session

            // give information user is member or manager to session
            if (content.get("role").equals("MEMBER")) {
                session.setAttribute(SessionConst.LOGIN_MEMBER, loginUserID);
            } else {
                session.setAttribute(SessionConst.LOGIN_MANAGER, loginUserID);

            }

            loginData.put("loginSuccess", true);
            loginData.put("identify", content.get("role"));

            // return loginData
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(loginData);

        } catch (LoginException | DocumentNotFoundException e) { // password error | id isn't found from DB
            loginData.put("loginSuccess", false);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(loginData);

        } catch (Exception e) { // network server connect error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(loginData);
        }

    }

    @PostMapping(value = "user-event")
    public ResponseEntity<Object> userEvent(@RequestBody HashMap<String, Object> userEventData,
                                            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) String loginUser,
                                            HttpServletRequest request) {
        try {
            // get session
            HttpSession session = request.getSession(false);

            // check session is maintained
            if (session == null) {
                throw new IllegalStateException();
            }

            // check login user exists
            if (loginUser == null) { // login exception
                throw new LoginException();
            }

            // set event id to session
            String eventId = String.valueOf(userEventData.get("eventId"));
            session.setAttribute(SessionConst.CURRENT_EVENT_ID, eventId);

            // put event id to body
            userEventData.put("eventId", eventId);

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userEventData);
        } catch (IllegalStateException | LoginException e) { // session or login error
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userEventData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(userEventData);
        }
    }

    @PostMapping(value = "/user-event-seat")
    public ResponseEntity<Object> seat(@RequestBody HashMap<String, Object> seatData,
                                       @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) String loginUser,
                                       HttpServletRequest request) {

        try {
            // get session
            HttpSession session = request.getSession(false);

            // check session is maintained
            if (session == null) {
                throw new IllegalStateException();
            }
            // check login user exists
            if (loginUser == null) { // login exception
                throw new LoginException();
            }

            if (seatData.containsKey("logout")) { // logout
                session.invalidate(); // delete session

            } else {  // seat
                Object dataSeat = seatData.get("selectedSeatIds");

                List<Integer> seatList = (List<Integer>) dataSeat;

                // user check wrong the number of seats
                if (seatList.isEmpty() || seatList.size() > 2) {
                    seatData.put("success", false);
                    throw new IllegalArgumentException();
                }

                // update userCollection and seatCollection
                userService.updateUser(loginUser, seatList);
                seatService.updateSeat(seatList);

                seatData.put("success", true);
            }


            // return seatData
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(seatData);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(seatData);
        } catch (IllegalStateException | LoginException e) { // session or login error
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(seatData);
        } catch (Exception e) { // network server connect error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(seatData);
        }

    }

    // Admin
    @PostMapping(value = "/manager")
    public ResponseEntity<Object> manager(@RequestBody HashMap<String, Object> managerData,
                                          @SessionAttribute(name = SessionConst.LOGIN_MANAGER, required = false) String loginManager,
                                          HttpServletRequest request) {
        try {
            // get session
            HttpSession session = request.getSession(false);

            // check session is maintained
            if (session == null) {
                throw new IllegalStateException();
            }
            // check login manager exists
            if (!loginManager.equals(SessionConst.LOGIN_MANAGER)) { // login exception
                throw new LoginException();
            }

            if (managerData.get("logout").equals("logout")) { // logout
                session.invalidate(); // delete session
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(managerData);

        } catch (IllegalStateException | LoginException e) { // session or login error
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(managerData);
        } catch (Exception e) { // network server connect error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(managerData);
        }
    }


    @PostMapping(value = "manager-event")
    public ResponseEntity<Object> managerEvent(@RequestBody HashMap<String, Object> managerData,
                                               @SessionAttribute(name = SessionConst.LOGIN_MANAGER, required = false) String loginManager,
                                               HttpServletRequest request) {

        try {
            // get session
            HttpSession session = request.getSession(false);

            // check session is maintained
            if (session == null) {
                throw new IllegalStateException();
            }
            // check login manager exists
            if (!loginManager.equals(SessionConst.LOGIN_MANAGER)) { // login exception
                throw new LoginException();
            }

            // get event id from manager data
            String eventId = (String) managerData.get("eventId");

            // put event participants list
            managerData.put("eventParticipantsList", eventService.eventParticipantsList(eventId));

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(managerData);

        } catch (IllegalStateException | LoginException e) { // session or login error
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(managerData);
        } catch (Exception e) { // network server connect error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(managerData);
        }
    }

    @PostMapping(value = "/manager-event-generate")
    public ResponseEntity<Object> managerEventGenerate(@RequestBody HashMap<String, Object> managerData,
                                                       @SessionAttribute(name = SessionConst.LOGIN_MANAGER, required = false) String loginManager,
                                                       HttpServletRequest request) {
        try {
            // get session
            HttpSession session = request.getSession(false);

            // check session is maintained
            if (session == null) {
                throw new IllegalStateException();
            }
            // check login manager exists
            if (!loginManager.equals(SessionConst.LOGIN_MANAGER)) { // login exception
                throw new LoginException();
            }

            Integer seatNum = (Integer) managerData.get("row") * (Integer) managerData.get("col");
            String eventName = (String) managerData.get("eventName");

            eventService.startEvent(seatService, loginManager, seatNum, eventName);

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(managerData);

        } catch (IllegalStateException | LoginException e) { // session or login error
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(managerData);
        } catch (Exception e) { // network server connect error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(managerData);
        }
    }
}
