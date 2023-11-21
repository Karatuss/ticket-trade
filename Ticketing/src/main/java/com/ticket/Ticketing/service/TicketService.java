package com.ticket.Ticketing.service;

import com.ticket.Ticketing.applicationGo.Chaincode;
import com.ticket.Ticketing.dto.TicketDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {
    public void createTickets(List<List<Integer>> seatList, Chaincode chaincode, Object eventId, String loginUser) {
        for (int i = 0; i < seatList.size(); i++) {
            String ticketId = String.valueOf(seatList.get(i)).replace(":", "");
            chaincode.createTicket(1, "Ticket" + ticketId, String.valueOf(eventId),
                    ticketId, loginUser);
        }
    }

    /**
     * The caller function should deal with NullPointerException
     *
     * @param chaincode
     * @param ticketId
     * @return
     */
    public List<TicketDto> ticketVerify(Chaincode chaincode, String ticketId, String eventId, String seatNum, String owner) {
        List<TicketDto> ticketDtos = new ArrayList<>();

        chaincode.readTicket(1, "Ticket" + ticketId);
        try {
            TicketDto ticketDto = chaincode.getResult().get(0);
            if ( ! ticketDto.equals(new TicketDto("Ticket" + ticketId, eventId, seatNum, owner))) {
                throw new NullPointerException();
            }
            ticketDtos.add(ticketDto);
        } catch (NullPointerException e) { // Not Verified
            ticketDtos.add(null);
        }

        // Verified - not null
        return ticketDtos;
    }

}