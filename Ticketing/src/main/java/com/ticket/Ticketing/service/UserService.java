package com.ticket.Ticketing.service;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.ExistsResult;
import com.couchbase.client.java.query.QueryOptions;
import com.ticket.Ticketing.config.UserConfig;
import com.ticket.Ticketing.domain.repository.Gender;
import com.ticket.Ticketing.domain.repository.Role;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.ticket.Ticketing.Ticketing.cluster;


@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    public void updateUser(String loginUser, List<List<Integer>> seatList, String eventId) {
        Bucket userBucket = cluster.bucket(UserConfig.getStaticBucketName());
        Collection userCollection = userBucket.defaultCollection();

        List<String> seatStringList = new ArrayList<>();

        int row = seatList.size();
        int col = seatList.isEmpty() ? 0 : seatList.get(0).size();

        // format seatList
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                String seatInfo = eventId + "-" + seatList.get(i) + ":" + seatList.get(j);
                seatStringList.add(seatInfo);
            }
        }

        // update seat info from user bucket
        JsonObject revisedInfo = userCollection.get(loginUser).contentAsObject();
        revisedInfo.put("seat", seatStringList);

        userCollection.replace(loginUser, revisedInfo);
    }

    public void registerUser(HashMap<String, Object> registerData) {
        Bucket userBucket = cluster.bucket(UserConfig.getStaticBucketName());
        Collection userCollection = userBucket.defaultCollection();

        String n_ID = String.valueOf(registerData.get("n_ID"));
        String email = String.valueOf(registerData.get("email"));
        String phoneNumber = String.valueOf(registerData.get("phoneNumber"));

        ExistsResult resultID = userCollection.exists(n_ID);
        ExistsResult resultPN = userCollection.exists(phoneNumber);
        ExistsResult resultEmail = userCollection.exists(email);

        // check inputted id, phoneNumber, email from DB
        if (resultID.exists() | resultPN.exists() | resultEmail.exists()) {
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

        // create index on the basis of id
        String query = "CREATE INDEX user_idx ON " + "`" + UserConfig.getStaticBucketName() + "` (id)";
        cluster.query(query, QueryOptions.queryOptions());
    }

}
