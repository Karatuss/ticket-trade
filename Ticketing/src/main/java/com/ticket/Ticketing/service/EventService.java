package com.ticket.Ticketing.service;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonArray;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryResult;
import com.ticket.Ticketing.config.EventConfig;
import com.ticket.Ticketing.config.UserConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ticket.Ticketing.Ticketing.cluster;


@Service
public class EventService {

    //TODO: startEvent/endEvent 를 호출한 쪽에서 loginManager 의 Role 이 Manager 인지 체크
    public void startEvent(SeatService seatService, String loginManager, Integer seatNum, String eventName) {
        // access to bucket
        Bucket eventBucket = cluster.bucket(EventConfig.getStaticBucketName());
        Collection eventCollection = eventBucket.defaultCollection();

        // count event number
        int eventNum = 1;
        while (eventExists(eventCollection, String.valueOf(eventNum))) {
            eventNum++;
        }

        JsonObject jsonData = JsonObject.create()
                .put("id", eventNum)
                .put("managerId", loginManager)
                .put("seatNum", seatNum)
                .put("eventName", eventName);

        // create new event on event_bucket by only manager
        eventCollection.insert(String.valueOf(eventNum), jsonData);

        // create seat by event
        seatService.createSeatDocuments(String.valueOf(eventNum), seatNum);
    }

    public void endEvent(SeatService seatService, String eventId) {
        // access to bucket
        Bucket eventBucket = cluster.bucket(EventConfig.getStaticBucketName());
        Collection eventCollection = eventBucket.defaultCollection();

        JsonObject content = eventCollection.get(eventId).contentAsObject();
        int seatNum = (int) content.get("seatNum");

        // delete seat by event
        seatService.deleteSeatByEventDocuments(eventId, seatNum);

        // delete event on event_bucket by only manager
        eventCollection.remove(eventId);
    }

    public HashMap<String, Object> userEventList(String loginUser) {
        // access to bucket
        Bucket eventBucket = cluster.bucket(EventConfig.getStaticBucketName());
        Collection eventCollection = eventBucket.defaultCollection();

        Bucket userBucket = cluster.bucket(UserConfig.getStaticBucketName());
        Collection userCollection = userBucket.defaultCollection();

        HashMap<String, Object> eventList = new HashMap<>();

        // count event number
        int eventNum = 1;
        while (eventExists(eventCollection, String.valueOf(eventNum))) {
            eventNum++;
        }

        // return whole event list
        if (loginUser == null) {
            for (int i = 1; i <= eventNum; i++) {
                eventList.put(String.valueOf(i), eventCollection.get(String.valueOf(i)));
            }
        }

        // return event list that login user is participating in
        JsonArray seatData = userCollection.get(loginUser).contentAsObject().getArray("seat");
        for (int i = 1; i <= seatData.size(); i++) {
            String eventId = String.valueOf(seatData.get(i)).split("-")[0];
            eventList.put(eventId, eventCollection.get(eventId));
        }

        return eventList;
    }

    public List<String> managerEventList(String loginManager) {

        Bucket eventBucket = cluster.bucket(EventConfig.getStaticBucketName());
        Collection eventCollection = eventBucket.defaultCollection();

        // count event number
        int eventNum = 1;
        while (eventExists(eventCollection, String.valueOf(eventNum))) {
            eventNum++;
        }

        // return event list login manager created
        List<String> eventList = new ArrayList<>();
        for (int i = 1; i <= eventNum; i++) {
            JsonObject event = eventCollection.get(String.valueOf(eventNum)).contentAsObject();
            if (event.getString("managerId").equals(loginManager)) {
                eventList.add(event.getString("id"));
            }
        }

        return eventList;
    }

    public List<String> eventParticipantsList(String eventId) {
        Bucket userBucket = cluster.bucket(EventConfig.getStaticBucketName());
        Collection userCollection = userBucket.defaultCollection();

        List<String> stringList = new ArrayList<>();

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

            for (String key : keys) {
                JsonObject content = userCollection.get(key).contentAsObject();
                String event = String.valueOf(content.get("seat")).split("-")[0];
                if (event.equals(eventId)) {
                    stringList.add(String.valueOf(content.get("id")));
                }
            }
        } finally {
            // delete primary index
            cluster.query("DROP INDEX `" + UserConfig.getStaticBucketName() + "`.`#primary`", QueryOptions.queryOptions());
        }
        return stringList;
    }

    private boolean eventExists(Collection collection, String eventId) {
        try {
            collection.get(eventId);
            return true; // event exists
        } catch (DocumentNotFoundException e) {
            return false; // event not exists
        }
    }

}
