package com.ticket.Ticketing.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {
    private String ticketID;
    private String eventID;
    private String seatNum;
    private String owner;

    @Override
    public String toString() {
        return "TicketDto{" +
                "ticketID='" + ticketID + '\'' +
                ", eventID='" + eventID + '\'' +
                ", seatNum=" + seatNum +
                ", owner='" + owner + '\'' +
                '}';
    }
}
