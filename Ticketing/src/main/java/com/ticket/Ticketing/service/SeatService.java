package com.ticket.Ticketing.service;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryResult;
import com.ticket.Ticketing.config.SeatConfig;
import com.ticket.Ticketing.domain.document.SeatDocument;
import com.ticket.Ticketing.domain.repository.SeatRepository;
import com.ticket.Ticketing.dto.SeatDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ticket.Ticketing.Ticketing.cluster;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class SeatService {
    private SeatRepository seatRepository;

    //! 추후에 manager가 관리하게 변경
    private final static int totalSeats = 20;

    // CREATE
    public void createSeatDocuments() {
        Bucket seatBucket = cluster.bucket("seat_bucket");
        Collection seatCollection = seatBucket.defaultCollection();

        for (int i = 1; i <= totalSeats; i++) {
            String documentId = String.format("%03d", i);
            if(!documentExists(seatCollection, documentId)) {   // don't make initial documents if already exist
                JsonObject jsonData = JsonObject.create()
                        .put("id", String.valueOf(i))
                        .put("sold", false);
                seatCollection.insert(documentId, jsonData);
            }
        }
    }
    private boolean documentExists(Collection collection, String documentId) {
        try {
            collection.get(documentId);
            return true; // Document exists
        } catch (DocumentNotFoundException e) {
            return false; // Document not exists
        }
    }
    // READ
    public List<SeatDto> getSeatList(){
        // return dto list made by dto got from couchbase
        String n1qlQuery = "SELECT * FROM `" + SeatConfig.getStaticBucketName() + "`";
        QueryResult queryResult = cluster.query(n1qlQuery, QueryOptions.queryOptions().adhoc(true));

        List<SeatDto> seatDtoList = new ArrayList<>();

        for (JsonObject row : queryResult.rowsAsObject()) {
            SeatDto dto = new SeatDto();
            dto.setId(row.getString("id"));
            dto.setSold(row.getBoolean("sold"));
            // 필요한 다른 필드 설정
            seatDtoList.add(dto);
        }
        return seatDtoList;
    }

    public int getTotalSeats(){
        return totalSeats;
    }



    // UPDATE
    private SeatDocument updateSeatDocument(SeatDto seatDto){
        return SeatDocument.builder()
                .id(seatDto.getId())
                .sold(seatDto.getSold())
                .build();
    }
    public void setSeatList(SeatDto seatDto){

        seatRepository.save(this.updateSeatDocument(seatDto));
    }

    // DELETE
    public void deleteSeatList(SeatDto seatDto){
        seatRepository.delete(this.updateSeatDocument(seatDto));
    }

    public void deleteAllSeatDocuments(){
        Bucket seatBucket = cluster.bucket("seat_bucket");
        Collection seatCollection = seatBucket.defaultCollection();

        for (int i = 1; i <= totalSeats; i++) {
            String documentId = String.format("%03d", i);
            if(documentExists(seatCollection, documentId)) {
                seatCollection.remove(documentId);
            }
        }
    }
    public void deleteSeatDocuments(Integer id){
        Bucket seatBucket = cluster.bucket("seat_bucket");
        Collection seatCollection = seatBucket.defaultCollection();
        String documentId = String.format("%03d", id);

        if(documentExists(seatCollection, documentId)) {
            seatCollection.remove(documentId);
        }

    }

    // convert document to dto
    private SeatDto convertDocumentToDto(SeatDocument seatDocument) {
        return SeatDto.builder()
                .id(seatDocument.getId())
                .sold(seatDocument.getSold())
                .build();

    }



}
