package com.ticket.Ticketing.service;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryResult;
import com.ticket.Ticketing.config.EventConfig;
import com.ticket.Ticketing.config.UserConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.ticket.Ticketing.Ticketing.cluster;

@Service
public class BlockChainService {

    public void checkTicketModified(String eventId) {
        Bucket eventBucket = cluster.bucket(EventConfig.getStaticBucketName());
        Collection eventCollection = eventBucket.defaultCollection();

        int row;
        int col;

        row = Integer.parseInt(String.valueOf(eventCollection.get(eventId).contentAsObject().get("row")));
        col = Integer.parseInt(String.valueOf(eventCollection.get(eventId).contentAsObject().get("col")));

        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= col; j++) {
                String seatNum = eventId + "-" + String.format("%03d", i) + ":" + String.format("%03d", j);
                String query = "SELECT id FROM " + "`" + UserConfig.getStaticBucketName()
                        + "` WHERE id and ANY s IN seat SATISFIES s = \"" + seatNum + "\" END";
                QueryResult result = cluster.query(query, QueryOptions.queryOptions());
                String owner = String.valueOf(result);

                // give JSON{TicketID, EventID, SeatNumber, Owner} to blockchain-network
                JsonObject ticketData = JsonObject.create()
                        .put("TicketID", "ticket" + i + ":" + j)
                        .put("EventID", eventId)
                        .put("SeatNumber", seatNum)
                        .put("Owner", owner);

                // call blockchain-network
                String blockchainApiUrl = "http://localhost:";

                RestTemplate restTemplate = new RestTemplate();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<String> requestEntity = new HttpEntity<>(String.valueOf(ticketData), headers);
                restTemplate.postForObject(blockchainApiUrl, requestEntity, String.class);
            }
        }

    }
}
