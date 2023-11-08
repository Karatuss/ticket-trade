package org.hyperledger.fabric.chaincode.model;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public class Ticket {

    // designated serial event ID(or number)
    // TODO: how to generate those unique string
    @Property()
    private final String eventID;

    // unique ticket ID
    @Property()
    private final String ticketID;

    @Property()
    private final String seatNum;

    // Must exist on the member database
    @Property()
    private final String owner;

    public String getTicketID() {
        return ticketID;
    }

    public String getEventID() {
        return eventID;
    }

    public String getSeatNum() {
        return seatNum;
    }

    public String getOwner() {
        return owner;
    }

    public Ticket(
            @JsonProperty("eventID") final String eventID,
            @JsonProperty("ticketID") final String ticketID,
            @JsonProperty("seatNum") final String seatNum,
            @JsonProperty("owner") final String owner) {
        this.eventID = eventID;
        this.ticketID = ticketID;
        this.seatNum = seatNum;
        this.owner = owner;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEventID(), getTicketID(), getSeatNum(), getOwner());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Ticket other = (Ticket) obj;

        return Objects.deepEquals(
                new String[] {getEventID(), getTicketID(), getSeatNum(), getOwner()},
                new String[] {other.getEventID(), other.getTicketID(), other.getSeatNum(), other.getOwner()});
    }

    // TODO: toString() for Ticket
    @Override
    public String toString() {
        return super.toString();
    }
}
