package org.hyperledger.fabric.chaincode;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.chaincode.model.Event;
import org.hyperledger.fabric.chaincode.model.Ticket;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeStub;

//TODO: Full implementation plan starts after completing
//      the connect with backend and controling.
@Contract(
        name = "event",
        info = @Info(
                title = "event-transaction",
                description = "Event transaction transfer",
                version = "0.0.1-SNAPSHOT",
                contact = @Contact(
                        email = "shm1193@gmail.com:",
                        name = "Hyeonmin Shin",
                        url = "http://ticket.com")))
@Default
public class EventTransfer {

    private final Genson genson = new Genson();

    private enum EventTransferErrors {
        EVENT_NOT_FOUND
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitEventLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();


    }

    public Event CreateEvent(final Context ctx, final String eventID, final String organizer) {
        ChaincodeStub stub = ctx.getStub();
        String eventJSON = stub.getStringState(eventID);

        Event event = genson.deserialize(eventJSON, Event.class);
        return event;
    }
}
