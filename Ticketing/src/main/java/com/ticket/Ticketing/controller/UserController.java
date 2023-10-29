package com.ticket.Ticketing.controller;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.ExistsResult;
import com.couchbase.client.java.kv.GetResult;
import com.ticket.Ticketing.Ticketing;
import com.ticket.Ticketing.domain.repository.Gender;
import com.ticket.Ticketing.domain.repository.Role;
import com.ticket.Ticketing.dto.UserDto;
import com.ticket.Ticketing.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;


// Mapping
//@CrossOrigin(originPatterns = "http://localhost:8080")
@RestController
@AllArgsConstructor
public class UserController {

        private final UserService userService;
        private final Cluster cluster = Ticketing.cluster;

        // Register
        @PostMapping(value = "/register")
        public ResponseEntity<Object> getRegisterInfo(@RequestBody HashMap<String, Object> registerData){


            try {
                Bucket bucket = cluster.bucket("sample_bucket");
                Collection collection = bucket.defaultCollection();

                Object n_ID = registerData.get("n_ID");

                ExistsResult result = collection.exists((String) n_ID);

                if(result.exists())
                    throw new Exception("There exists already same ID. ");

                // put data into couchbase
                Gender gender = Gender.valueOf(String.valueOf(registerData.get("gender")));

                UserDto userDto = new UserDto(
                        String.valueOf(n_ID),
                        String.valueOf(registerData.get("n_PW")),
                        String.valueOf(String.valueOf(registerData.get("username"))),
                        Integer.valueOf(String.valueOf(registerData.get("age"))),
                        String.valueOf(registerData.get("email")),
                        gender, String.valueOf( registerData.get("phoneNumber")),
                        null,
                        Role.MEMBER,
                        0
                );

                userService.setList(userDto);

                registerData.put("submit", true);
                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(registerData);
            }catch (Exception e) {
                registerData.put("submit", false);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(registerData);
            }
        }

        // Login
        @PostMapping(value = "/login")
        public ResponseEntity<Object> getLoginIDPW(@RequestBody HashMap<String, Object> loginData) {

            try {
                //! bucket 이름도 나중에 바꾸기
                Bucket bucket = cluster.bucket("sample_bucket");
                Collection collection = bucket.defaultCollection();

                // input id, pw
                Object userID = loginData.get("user_ID");
                Object userPW = loginData.get("user_PW");

                // Couchbase pw for login
                Object checkedPW;

                GetResult result = collection.get((String) userID);
                // `result` get nothing
                if(result == null)
                    throw new NullPointerException();

                JsonObject content = result.contentAsObject();
                checkedPW = content.get("password");

                // password does not equal as stored
                if(!checkedPW.equals(userPW))
                    throw new Exception();

                loginData.put("loginSuccess", true);
                loginData.put("identify", content.get("role"));


                // return loginData
                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(loginData);
            } catch (Exception e) {
                loginData.put("loginSuccess", false);

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(loginData);
            }

        }


        // Check member information
        //! 수정해서 사용하거나 지워질 수도 있음.
//        @GetMapping(value = "/register/{id}")
//        public List<Map<String, Object>> getList(@PathVariable String id, String password){
//                List<UserDto> userDtoList = userService.getList(id, password);
//
//                List<Map<String, Object>> userLoginList = new ArrayList<>();
//                for(UserDto userDto : userDtoList){
//                        Map<String,Object> result = new HashMap<>();
//                        result.put("id", userDto.getId());
//                        result.put("age", userDto.getAge());
//                        result.put("name", userDto.getName());
//                        result.put("email", userDto.getEmail());
//                        result.put("password", userDto.getPassword());
//                        result.put("phonenumber", userDto.getPhoneNumber());
//                        result.put("gender", userDto.getGender());
//                        result.put("role", userDto.getRole());
////                        result.put("faceimg", userDto.getFaceImg());
//                        userLoginList.add(result);
//                }
//                return userLoginList;
//        }

}
