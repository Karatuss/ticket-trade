package com.ticket.Ticketing.service;

import com.ticket.Ticketing.domain.document.SeatDocument;
import com.ticket.Ticketing.domain.repository.SeatRepository;
import com.ticket.Ticketing.dto.SeatDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;


    // CREATE
    private SeatDocument createSeatDocument(SeatDto seatDto){
        return SeatDocument.builder()
                .seatNum(seatDto.getSeatNum())
                .sold(seatDto.getSold())
                .build();
    }

    // READ
    public List<SeatDto> getSeatList(String idseat, String seatnum){
        List<SeatDto> seatDtos = new ArrayList<>();
        List<SeatDocument> seatDocuments = (List<SeatDocument>) seatRepository.findAll();
        for(SeatDocument seatDocument : seatDocuments){
            seatDtos.add(this.convertDocumentToDto(seatDocument));
        }
        return seatDtos;
    }



    // UPDATE
    public void setSeatList(SeatDto seatDto){
        seatRepository.save(this.createSeatDocument(seatDto));
    }


    // DELETE
    public void deleteSeatList(SeatDto seatDto){
        seatRepository.delete(this.createSeatDocument(seatDto));
    }

    // convert document to dto
    private SeatDto convertDocumentToDto(SeatDocument seatDocument) {
        return SeatDto.builder()
                .seatNum(seatDocument.getSeatNum())
                .sold(seatDocument.getSold())
                .build();

    }

}
