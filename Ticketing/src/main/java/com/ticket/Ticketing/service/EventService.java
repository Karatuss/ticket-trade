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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ticket.Ticketing.Ticketing.cluster;


@Service
public class EventService {

    public void startEvent(SeatService seatService, String loginManager, Integer row, Integer col
            , String eventName, String eventStart, String eventEnd) {
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
                .put("row", row)
                .put("col", col)
                .put("eventName", eventName)
                .put("eventStart", eventStart)
                .put("eventEnd", eventEnd)
                .put("eventStatus", false);

        // create new event on event_bucket by only manager
        eventCollection.insert(String.valueOf(eventNum), jsonData);

        // create seat by event
        seatService.createSeatDocuments(String.valueOf(eventNum), row, col);
    }

    public void removeEvent(SeatService seatService, String eventId) {
        // access to bucket
        Bucket eventBucket = cluster.bucket(EventConfig.getStaticBucketName());
        Collection eventCollection = eventBucket.defaultCollection();

        JsonObject content = eventCollection.get(eventId).contentAsObject();
        int row = (int) content.get("row");
        int col = (int) content.get("col");

        // delete seat by event
        seatService.deleteSeatByEventDocuments(eventId, row, col);

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
            for (int i = 1; i < eventNum; i++) {
                if (isEventAccessible(String.valueOf(i))) {
                    eventList.put(String.valueOf(i), eventCollection.get(String.valueOf(i)).contentAsObject());
                }
            }
        } else {
            // return event list that login user is participating in
            JsonArray seatData = userCollection.get(loginUser).contentAsObject().getArray("seat");

            for (int i = 0; i < seatData.size(); i++) {
                String eventId = String.valueOf(seatData.get(i)).split("-")[0];
                if ((!eventId.equals("null")) && !eventId.isEmpty()) {
                    if (isEventAccessible(eventId)) {
                        eventList.put(eventId, eventCollection.get(eventId).contentAsObject());
                    }
                }
            }
        }

        return eventList;
    }

    public HashMap<String, String> managerEventList(String loginManager) {

        Bucket eventBucket = cluster.bucket(EventConfig.getStaticBucketName());
        Collection eventCollection = eventBucket.defaultCollection();

        // count event number
        int eventNum = 1;
        while (eventExists(eventCollection, String.valueOf(eventNum))) {
            eventNum++;
        }

        // return event list login manager created
        HashMap<String, String> eventList = new HashMap<>();

        for (int i = 1; i < eventNum; i++) {
            JsonObject event = eventCollection.get(String.valueOf(i)).contentAsObject();
            if (event.getString("managerId").equals(loginManager)) {
                eventList.put(String.valueOf(i), String.valueOf(event));
            }
        }

        return eventList;
    }

    public List<String> eventParticipantsList(String eventId) {
        Bucket userBucket = cluster.bucket(UserConfig.getStaticBucketName());
        Collection userCollection = userBucket.defaultCollection();

        List<String> stringList = new ArrayList<>();

        String query = "SELECT * FROM " + "`" + UserConfig.getStaticBucketName() + "` WHERE id";
        QueryResult result = cluster.query(query, QueryOptions.queryOptions());

        for (int i = 0; i < result.rowsAsObject().size(); i++) {
            Object seat = result.rowsAsObject().get(i).getObject("user_bucket").getArray("seat").get(0);
            if (seat != null) {
                String event = seat.toString().split("-")[0];
                if (event.equals(eventId)) {
                    String userID = String.valueOf(result.rowsAsObject().get(i).getObject("user_bucket").get("id"));
                    JsonObject content = userCollection.get(userID).contentAsObject();
                    String userId = String.valueOf(content.get("id"));
                    stringList.add(String.valueOf(userCollection.get(userId).contentAsObject()));
                }
            }
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

    private boolean isEventAccessible(String eventId) {

        Bucket eventBucket = cluster.bucket(EventConfig.getStaticBucketName());
        Collection eventCollection = eventBucket.defaultCollection();

        LocalDateTime currentTime = LocalDateTime.now();

        JsonObject eventObject = eventCollection.get(eventId).contentAsObject();

        LocalDateTime eventStart = LocalDateTime.parse(String.valueOf(eventObject.get("eventStart")));


        if (currentTime.isBefore(eventStart)) {
            eventCollection.get(eventId).contentAsObject().put("eventStatus", true);
            return true;
        } else {
            eventCollection.get(eventId).contentAsObject().put("eventStatus", false);
            return false;
        }

    }

}
