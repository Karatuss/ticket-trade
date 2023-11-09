package com.ticket.Ticketing.service;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonArray;
import com.couchbase.client.java.json.JsonObject;
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
                .put("eventName", eventName)
                .put("participants", (List<String>) null);

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
        Bucket eventBucket = cluster.bucket(EventConfig.getStaticBucketName());
        Collection eventCollection = eventBucket.defaultCollection();

        List<Object> objectList = eventCollection.get(eventId).contentAsObject().getArray("participants").toList();
        List<String> stringList = new ArrayList<>();
        for (Object obj : objectList) {
            stringList.add((String) obj);
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
