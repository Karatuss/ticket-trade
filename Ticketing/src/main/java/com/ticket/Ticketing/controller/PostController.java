package com.ticket.Ticketing.controller;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.ExistsResult;
import com.couchbase.client.java.kv.GetResult;
import com.ticket.Ticketing.domain.repository.Gender;
import com.ticket.Ticketing.domain.repository.Role;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import java.util.*;

import static com.ticket.Ticketing.Ticketing.cluster;


// Mapping
@RestController
@AllArgsConstructor
public class PostController {
    public static String loginUserID;  //! logout 수행에서 null로 초기화 필요


    // Register
    @PostMapping(value = "/register")
    public ResponseEntity<Object> register(@RequestBody HashMap<String, Object> registerData){
        try {
            Bucket userBucket = cluster.bucket("user_bucket");
            Collection userCollection = userBucket.defaultCollection();

            String n_ID = String.valueOf(registerData.get("n_ID"));
            String email = String.valueOf(registerData.get("email"));
            String phoneNumber = String.valueOf(registerData.get("phoneNumber"));

            ExistsResult resultID = userCollection.exists(n_ID);
            ExistsResult resultPN = userCollection.exists(phoneNumber);
            ExistsResult resultEmail = userCollection.exists(email);

            // check inputted id, phoneNumber, email from DB
            if(resultID.exists() | resultPN.exists() | resultEmail.exists())
                throw new DuplicateKeyException("There are duplicated ID, phone number or email. ");

            // put data into couchbase
            Gender gender = Gender.valueOf(String.valueOf(registerData.get("gender")));

            // data for saving to couchbase
            JsonObject jsonData = JsonObject.create()
                    .put("id", n_ID)
                    .put("password",  String.valueOf(registerData.get("n_PW")))
                    .put("name", String.valueOf(registerData.get("username")))
                    .put("age", Integer.valueOf(String.valueOf(registerData.get("age"))))
                    .put("email", email)
                    .put("gender", String.valueOf(gender))
                    .put("phoneNumber", phoneNumber)
                    .put("seat", Collections.nCopies(2, null))
                    .put("role", String.valueOf(Role.MEMBER));

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
    public ResponseEntity<Object> login(@RequestBody HashMap<String, Object> loginData) {
        try {
            Bucket userBucket = cluster.bucket("user_bucket");
            Collection userCollection = userBucket.defaultCollection();

            // input id, pw
            Object userID = loginData.get("user_ID");
            Object userPW = loginData.get("user_PW");
            loginUserID = (String) userID;

            // Couchbase pw for login
            Object checkedPW;

            // get data from DB that id is userID
            GetResult result = userCollection.get(loginUserID); // document id
            JsonObject content = result.contentAsObject();
            checkedPW = content.get("password");

            // password does not equal as stored
            if(!checkedPW.equals(userPW))
                throw new LoginException();

            // login success
            loginData.put("loginSuccess", true);
            loginData.put("identify", content.get("role"));


            // return loginData
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(loginData);

        } catch (LoginException | DocumentNotFoundException e){ // password error | id isn't found from DB
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
    public ResponseEntity<Object> seat(@RequestBody HashMap<String, Object> seatData){
        try{
            Bucket seatBucket = cluster.bucket("seat_bucket");
            Collection seatCollection = seatBucket.defaultCollection();

            Bucket userBucket = cluster.bucket("user_bucket");
            Collection userCollection = userBucket.defaultCollection();

            // get data from seat post(buy-book.js and logout.js)

            if(seatData.get("seat") == null){   // logout
                Object dataLogout = seatData.get("logout");

                //TODO: 추후에 로그인 세션 유지 작업 끝나면 작성

            }else{                              // seat
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

            // return seatData
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(seatData);

        }catch (Exception e){ // network server connect error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(seatData);
        }

    }


//        @PostMapping(value = "seat")
//        public ResponseEntity<Object> postSeatData(@RequestBody HashMap<String, Object> seatData){
//
//            try {
//            } catch(){
//            }
//        }

}
