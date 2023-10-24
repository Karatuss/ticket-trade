package com.ticket.Ticketing.service;

import com.ticket.Ticketing.domain.document.TicketDocument;
import com.ticket.Ticketing.domain.repository.TicketRepository;
import com.ticket.Ticketing.dto.TicketDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;

    // CREATE
    private TicketDocument createTicketDocument(TicketDto ticketDto){
        return  TicketDocument.builder()
                .ticketPrice(ticketDto.getTicketPrice())
                .build();
    }

    // READ
    public List<TicketDto> getTicketList(String idticket, String ticketprice){
        List<TicketDto> ticketDtos = new ArrayList<>();
        List<TicketDocument> ticketDocuments = (List<TicketDocument>) ticketRepository.findAll();
        for(TicketDocument ticketDocument : ticketDocuments){
            ticketDtos.add(this.convertDocumentToDto(ticketDocument));
        }
        return ticketDtos;
    }

    // UPDATE
    public void setTicketList(TicketDto ticketDto) { ticketRepository.save(this.createTicketDocument(ticketDto));}

    // DELETE
    public void deleteTicketList(TicketDto ticketDto){ ticketRepository.delete(this.createTicketDocument(ticketDto));}

    // convert document to dto
    private TicketDto convertDocumentToDto(TicketDocument ticketDocument) {
        return TicketDto.builder()
                .ticketPrice(ticketDocument.getTicketPrice())
                .build();
    }

}
