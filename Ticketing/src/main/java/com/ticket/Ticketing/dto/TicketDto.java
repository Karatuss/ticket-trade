package com.ticket.Ticketing.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketDto ticketDto = (TicketDto) o;

        if (!Objects.equals(ticketID, ticketDto.ticketID)) return false;
        if (!Objects.equals(eventID, ticketDto.eventID)) return false;
        if (!Objects.equals(seatNum, ticketDto.seatNum)) return false;
        return Objects.equals(owner, ticketDto.owner);
    }

    @Override
    public int hashCode() {
        int result = ticketID != null ? ticketID.hashCode() : 0;
        result = 31 * result + (eventID != null ? eventID.hashCode() : 0);
        result = 31 * result + (seatNum != null ? seatNum.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }

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