package com.ticket.Ticketing.controller;


import com.couchbase.client.core.deps.com.google.protobuf.StringValue;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.manager.query.QueryIndex;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryResult;
import com.ticket.Ticketing.config.SeatConfig;
import com.ticket.Ticketing.config.UserConfig;
import com.ticket.Ticketing.domain.repository.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ticket.Ticketing.Ticketing.cluster;


@Controller
@AllArgsConstructor
@SessionAttributes("user")
public class GetController {
//    private final UserService userService;
//    private final SeatService seatService;


    @GetMapping("/index")
    public String index() {
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


    @GetMapping(value = "/seat")
    public String setSeat(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) String loginUser, HttpServletRequest request, Model model) {
        // check login member user
        if (loginUser == null) {
            return "index";
        }

        // if normally checked login get session
        HttpSession session = request.getSession(false);

        // check session is maintained
        if (session == null) {
            return "index";
        }

        // access to buckets
        Bucket seatBucket = cluster.bucket(SeatConfig.getStaticBucketName());
        Collection seatCollection = seatBucket.defaultCollection();

        Bucket userBucket = cluster.bucket(UserConfig.getStaticBucketName());
        Collection userCollection = userBucket.defaultCollection();

        // add login user's seats info to model
        Object loginUserSeat = userCollection.get(loginUser).contentAsObject().get("seat");
        model.addAttribute("loginUserSeat", loginUserSeat);

        // add reserved seats info to model
        List<String> seatReserved = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            JsonObject seatInfo = seatCollection.get(String.format("%03d", i)).contentAsObject();
            if (seatInfo.getBoolean("sold")) {
                seatReserved.add((String) seatInfo.get("id"));
            }
        }
        model.addAttribute("seatReserved", seatReserved);

        return "seat";
    }


    @GetMapping(value = "/manager")
    public String manager(@SessionAttribute(name = SessionConst.LOGIN_MANAGER, required = false) String loginManager, HttpServletRequest request, Model model) {

        // check login user
        if (loginManager == null) {
            return "index";
        }

        // if normally checked login get session
        HttpSession session = request.getSession(false);

        // check session is maintained
        if (session == null) {
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
            List<String> keys = result.rowsAs(String.class);

            // save data to model for giving data to front-end
            int num = 1;
            for (String key : keys) {
                model.addAttribute("user" + (num++), userCollection.get(key).contentAsObject());
            }

        } finally {
            // delete primary index
            cluster.query("DROP INDEX `" + UserConfig.getStaticBucketName() + "`.`#primary`", QueryOptions.queryOptions());
        }

        return "manager";

    }


}
