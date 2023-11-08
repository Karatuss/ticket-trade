package org.hyperledger.fabric.chaincode;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.chaincode.model.Ticket;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

@Contract(
        name = "ticket",
        info = @Info(
                title = "ticket-transaction",
                description = "Ticket transaction transfer",
                version = "0.0.1-SNAPSHOT",
                contact = @Contact(
                        email = "shm1193@gmail.com:",
                        name = "Hyeonmin Shin",
                        url = "http://ticket.com")))
@Default
public class TicketTransfer {

    private final Genson genson = new Genson();

    private enum TicketTransferErrors {
        TICKET_NOT_FOUND,
        TICKET_ALREADY_SOLDOUT
    }

    /**
     * TODO: What should we do at here?
     *
     * @param ctx the transaction context
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        // test ticket instances
        CreateTicket(ctx,
                "test-event",
                "test-event-ticket-1A",
                "1A",
                "hyeonmin");
    }

    /**
     * Creates a new ticket on the ledger.
     *
     * @param ctx the transaction context
     * @param eventID the opened event ID for the new ticket
     * @param ticketID the ID of the new ticket
     * @param seatNum the seat number of the new ticket
     * @param owner the owner(same as last buyer) of the new ticket
     * @return the created ticket
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Ticket CreateTicket(final Context ctx, final String eventID, final String ticketID,
                               final String seatNum, final String owner) {
        ChaincodeStub stub = ctx.getStub();
        String ticketJSON = stub.getStringState(ticketID);

        Ticket ticket = genson.deserialize(ticketJSON, Ticket.class);
        return ticket;
    }

    /**
     * Retrieves a ticket with the specified ID from the ledger.
     *
     * @param ctx the transaction context
     * @param ticketID the ID of the ticket
     * @return the ticket found on the ledger if there was one
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Ticket ReadTicket(final Context ctx, final String ticketID) {
        ChaincodeStub stub = ctx.getStub();
        String assetTicket = stub.getStringState(ticketID);

        if (assetTicket == null || assetTicket.isEmpty()) {
            String errorMessage = String.format("Ticket %s does not exist", ticketID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, TicketTransferErrors.TICKET_NOT_FOUND.toString());
        }

        Ticket ticket = genson.deserialize(assetTicket, Ticket.class);
        return ticket;
    }

    /**
     * Updates the properties of a ticket on the ledger.
     *
     * @param ctx the transaction context
     * @param eventID the opened event ID for the new ticket
     * @param ticketID the ID of the new ticket
     * @param seatNum the seat number of the new ticket
     * @param owner the owner(same as last buyer) of the new ticket
     * @return the updated ticket
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Ticket UpdateTicket(final Context ctx, final String eventID, final String ticketID,
                               final String seatNum, final String owner) {
        ChaincodeStub stub = ctx.getStub();

        if (!TicketExists(ctx, ticketID)) {
            String errorMessage = String.format("Ticket %s does not exist", ticketID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, TicketTransferErrors.TICKET_NOT_FOUND.toString());
        }

        Ticket newTicket = new Ticket(eventID, ticketID, seatNum, owner);
        String sortedJson = genson.serialize(newTicket);
        stub.putStringState(ticketID, sortedJson);
        return newTicket;
    }

    /**
     * Deletes ticket on the ledger.
     *
     * @param ctx the transaction context
     * @param ticketID the ID of the ticket being deleted
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeleteTicket(final Context ctx, final String ticketID) {
        ChaincodeStub stub = ctx.getStub();

        if (!TicketExists(ctx, ticketID)) {
            String errorMessage = String.format("Ticket %s does not exist", ticketID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, TicketTransferErrors.TICKET_NOT_FOUND.toString());
        }

        stub.delState(ticketID);
    }

    /**
     * Checks the existence of the ticket on the ledger.
     *
     * @param ctx the transaction context
     * @param ticketID the ID of the ticket
     * @return boolean indicating the existence of the ticket
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean TicketExists(final Context ctx, final String ticketID) {
        ChaincodeStub stub = ctx.getStub();
        String ticketJSON = stub.getStringState(ticketID);

        return (ticketJSON != null && !ticketJSON.isEmpty());
    }
}
