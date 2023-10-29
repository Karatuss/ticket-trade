package com.ticket.Ticketing.controller;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.ExistsResult;
import com.ticket.Ticketing.config.SeatConfig;
import com.ticket.Ticketing.config.UserConfig;
import com.ticket.Ticketing.domain.repository.Gender;
import com.ticket.Ticketing.domain.repository.Role;
import com.ticket.Ticketing.domain.repository.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import java.util.*;

import static com.ticket.Ticketing.Ticketing.cluster;


// Mapping
@RestController
@AllArgsConstructor
@SessionAttributes("user")
public class PostController {
    public static String loginUserID;
    @Autowired
    PasswordEncoder passwordEncoder;
    private static final int MAX_ATTEMPTS = 3;
    private static long rejectionEndTime = System.currentTimeMillis() + 180000;

    // Register
    @PostMapping(value = "/register")
    public ResponseEntity<Object> register(@RequestBody HashMap<String, Object> registerData){
        try {
            Bucket userBucket = cluster.bucket(UserConfig.getStaticBucketName());
            Collection userCollection = userBucket.defaultCollection();

            String n_ID = String.valueOf(registerData.get("n_ID"));
            String email = String.valueOf(registerData.get("email"));
            String phoneNumber = String.valueOf(registerData.get("phoneNumber"));

            ExistsResult resultID = userCollection.exists(n_ID);
            ExistsResult resultPN = userCollection.exists(phoneNumber);
            ExistsResult resultEmail = userCollection.exists(email);

            // check inputted id, phoneNumber, email from DB
            if(resultID.exists() | resultPN.exists() | resultEmail.exists()) {
                throw new DuplicateKeyException("There are duplicated ID, phone number or email. ");
            }

            // put data into couchbase
            Gender gender = Gender.valueOf(String.valueOf(registerData.get("gender")));
            String rawPassword = String.valueOf(registerData.get("n_PW"));
            String EncPW = passwordEncoder.encode(rawPassword);

            // data for saving to couchbase
            JsonObject jsonData = JsonObject.create()
                    .put("id", n_ID)
                    .put("password", EncPW)
                    .put("name", String.valueOf(registerData.get("username")))
                    .put("age", Integer.valueOf(String.valueOf(registerData.get("age"))))
                    .put("email", email)
                    .put("gender", String.valueOf(gender))
                    .put("phoneNumber", phoneNumber)
                    .put("seat", Collections.nCopies(2, null))
                    .put("role", String.valueOf(Role.MEMBER))
                    .put("loginAttempts", 0);

            // insert data
            userCollection.insert(n_ID, jsonData);


            registerData.put("submit", true);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(registerData);

        } catch (DuplicateKeyException E){  // check duplicated id, phoneNumber, email
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
            loginUserID = (String) userID;

            // Couchbase pw for login
            Object checkedPW;

            // get data from DB that id is userID
            JsonObject content = userCollection.get(loginUserID).contentAsObject();
            checkedPW = content.get("password");

            // check login attempts
            content.put("loginAttempts",  (int) content.get("loginAttempts") + 1);
            userCollection.replace(loginUserID, content);

            if((int) content.get("loginAttempts") > MAX_ATTEMPTS){
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
            if(!passwordEncoder.matches(rawPassword, encodedPassword)) {
                throw new LoginException();
            }

            // login success
            // reset login attempts
            content.put("loginAttempts",  0);
            userCollection.replace(loginUserID, content);

            //TODO 추후에 로그인 세션에 담긴 유저가 매니저인지 체크해서 세션에 전달
            
            // get login session
            HttpSession session = request.getSession(); // if no session exists, make a new session
            session.setAttribute(SessionConst.LOGIN_MEMBER, loginUserID);

            loginData.put("loginSuccess", true);
            loginData.put("identify", content.get("role"));


            // return loginData
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(loginData);

        }  catch (LoginException | DocumentNotFoundException e){ // password error | id isn't found from DB
            loginData.put("loginSuccess", false);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(loginData);

        } catch(Exception e){ // network server connect error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(loginData);
        }

    }


    @PostMapping(value="/seat")
    public ResponseEntity<Object> seat(@RequestBody HashMap<String, Object> seatData,
                                       @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)String loginUser,
                                       HttpServletRequest request){

        try{
            Bucket seatBucket = cluster.bucket(SeatConfig.getStaticBucketName());
            Collection seatCollection = seatBucket.defaultCollection();

            Bucket userBucket = cluster.bucket(UserConfig.getStaticBucketName());
            Collection userCollection = userBucket.defaultCollection();

            // get data from seat post(seat-book.js and logout.js)

            // get session
            HttpSession session = request.getSession(false);

              // check session is maintained
            if(session == null){
                throw new IllegalStateException();
            }
            // check login user exists
            if(loginUser == null){ // login exception
                session.invalidate();
                throw new LoginException();
            }

            if(seatData.get("seat") == null){ // logout
                loginUserID = null; // reset because of logout

            } else{  // seat
                Object dataSeat = seatData.get("seat");
                List<String> seatList = (List<String>) dataSeat;

                // update seat info from user data
                JsonObject revisedInfo = userCollection.get(loginUserID).contentAsObject();
                revisedInfo.put("seat", seatList);

                userCollection.replace(loginUserID, revisedInfo);

                // update seat info from seat data
                for(String seatNum : seatList) {
                    String documentId = String.format("%03d", Integer.valueOf(seatNum));

                    // check if seat already sold out
                    Object seatResult = seatCollection.get(documentId).contentAsObject().get("sold");
                    if(seatResult.equals(false)) {
                        JsonObject jsonData = JsonObject.create()
                                .put("id", documentId)
                                .put("sold", true);
                        seatCollection.replace(documentId, jsonData);
                    }
                }

            }

            // don't need anything to seatData because front-end didn't request
//            seatData.put();

            // return seatData
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(seatData);

        }  catch(IllegalStateException | LoginException e){ // session or login error
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(seatData);
        } catch (Exception e){ // network server connect error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(seatData);
        }

    }



}
