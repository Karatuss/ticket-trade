package com.ticket.Ticketing.controller;


import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryResult;
import com.ticket.Ticketing.config.UserConfig;
import com.ticket.Ticketing.domain.repository.Role;
import com.ticket.Ticketing.domain.repository.SessionConst;
import com.ticket.Ticketing.service.EventService;
import com.ticket.Ticketing.service.SeatService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.HashMap;
import java.util.List;

import static com.ticket.Ticketing.Ticketing.cluster;


@Controller
@AllArgsConstructor
@SessionAttributes("user")
public class GetController {
    //    private final UserService userService;
    private final SeatService seatService;
    private final EventService eventService;

    @GetMapping("/")
    public String none() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "index";
    }


    @GetMapping("/login")
    public String login(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) String loginUser) {

        if (loginUser != null) {
            return "index";
        }

        return "login";
    }

    @GetMapping(value = "/register")
    public String setRegister(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) String loginUser) {

        if (loginUser != null) {
            return "index";
        }

        return "register";
    }

    @GetMapping(value = "/manager")
    public String manager(@SessionAttribute(name = SessionConst.LOGIN_MANAGER, required = false) String loginManager, HttpServletRequest request, Model model) {
        // if normally checked login get session
        HttpSession session = request.getSession(false);

        // check session and login manager exist
        if (session == null || loginManager == null) {
            return "index";
        }

        // put user data into model for user-info.js
        Bucket userBucket = cluster.bucket(UserConfig.getStaticBucketName());
        Collection userCollection = userBucket.defaultCollection();

        try {
            // create primary index
            cluster.query("CREATE PRIMARY INDEX ON `" + UserConfig.getStaticBucketName() + "`", QueryOptions.queryOptions());

            // TODO: 동기 혹은 비동기 처리
//            // wait for available status
//            userBucket.waitUntilReady(Duration.ofSeconds(2));

            // get all keys from every documents by using N1QL query
            String query = "SELECT RAW META().id FROM `" + UserConfig.getStaticBucketName() + "`";
            QueryResult result = cluster.query(query, QueryOptions.queryOptions());

            // save data to model for giving data to front-end
            List<String> keys = result.rowsAs(String.class);
            HashMap<String, Object> userInfo = new HashMap<>();

            int i = 1;
            for (String key : keys) {
                if (userCollection.get(key).contentAsObject().get("role").equals(String.valueOf(Role.MEMBER))) {
                    userInfo.put(String.valueOf(i++), userCollection.get(key).contentAsObject());
                }
            }
            model.addAttribute("user", userInfo);
        } finally {
            // delete primary index
            cluster.query("DROP INDEX `" + UserConfig.getStaticBucketName() + "`.`#primary`", QueryOptions.queryOptions());
        }

        return "manager";
    }

    @GetMapping("/user-event")
    public String userEvent(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) String loginUser
            , HttpServletRequest request, Model model) {
        // if normally checked login get session
        HttpSession session = request.getSession(false);

        // check session and login user exist
        if (session == null || loginUser == null) {
            return "index";
        }

        // put whole event list to model
        model.addAttribute("wholeEvent", eventService.userEventList(null));

        // put event list user is participating in to model
        model.addAttribute("userEvent", eventService.userEventList(loginUser));

        return "user-event";
    }

    @GetMapping("/user-event-seat")
    public String userEventSeat(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) String loginUser,
                                @SessionAttribute(name = SessionConst.CURRENT_EVENT_ID, required = false) String currentEventId,
                                HttpServletRequest request, Model model) {
        // if normally checked login get session
        HttpSession session = request.getSession(false);

        // check session and login user exist
        if (session == null || loginUser == null) {
            return "index";
        }

        // put current event info
        model.addAttribute("userEvent", currentEventId);

        // put reserved seat of current event id by all users
        model.addAttribute("allReservedSeat", seatService.seatList(currentEventId, null));

        // put reserved seat of current event id by login user
        model.addAttribute("userReservedSeat", seatService.seatList(currentEventId, loginUser));

        return "user-event-seat";
    }

    @GetMapping("/manager-event")
    public String managerEvent(@SessionAttribute(name = SessionConst.LOGIN_MANAGER, required = false) String loginManager
            , HttpServletRequest request, Model model) {
        // if normally checked login get session
        HttpSession session = request.getSession(false);

        // check session and login user exist
        if (session == null || loginManager == null) {
            return "index";
        }

        // get event list login manager created
        List<String> eventList = eventService.managerEventList(loginManager);

        // put event list
        model.addAttribute("managerEventList", eventList);

        return "manager-event";
    }

    @GetMapping("/manager-event-generate")
    public String managerEventGenerate(@SessionAttribute(name = SessionConst.LOGIN_MANAGER, required = false) String loginManager,
                                       HttpServletRequest request) {
        // if normally checked login get session
        HttpSession session = request.getSession(false);

        // check session and login user exist
        if (session == null || loginManager == null) {
            return "index";
        }

        return "manager-event-generate";
    }

}
