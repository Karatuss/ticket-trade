package org.hyperledger.fabric.chaincode.model;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public class Event {

    @Property()
    private final String eventID;

    @Property()
    private final String organizer;

    public String getEventID() {
        return eventID;
    }

    public String getOrganizer() {
        return organizer;
    }

    public Event(
            @JsonProperty("eventID") final String eventID,
            @JsonProperty("organizer") final String organizer) {
        this.eventID = eventID;
        this.organizer = organizer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.deepEquals(eventID, event.eventID) && Objects.deepEquals(organizer, event.organizer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventID, organizer);
    }
}
