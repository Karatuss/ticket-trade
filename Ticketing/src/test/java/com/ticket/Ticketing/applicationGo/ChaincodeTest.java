package com.ticket.Ticketing.applicationGo;

import com.ticket.Ticketing.dto.TicketDto;
import org.junit.jupiter.api.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ChaincodeTest {

    private static Chaincode chaincode;

    @BeforeAll
    static void constructorWorksProperly() throws IOException {
        // load blockchain properties file
        String blockchainConfigPath = Thread.currentThread().getContextClassLoader().getResource("blockchain.properties").getPath();

        Properties blockchainProps = new Properties();
        blockchainProps.load(new FileInputStream(blockchainConfigPath));

        String blockchainChannelName = blockchainProps.getProperty("ticket.channelName");
        String blockchainContractBasic = blockchainProps.getProperty("ticket.contract.basic");

        // execute constructor
        chaincode = new Chaincode(blockchainChannelName, blockchainContractBasic);
        assertNotNull(chaincode, "chaincode is not generated");

        // InitLedger call
        int exitCode = chaincode.initLedger(1);
        assertEquals(exitCode, 0);
    }

    @Test
    void readTicketExist() {
        // TODO: check the result text is intended text.
        int exitCode = chaincode.readTicket(1, "ticket1");

        assertEquals(exitCode, 0);
    }

    @Test
    void readTicketButNotExist() {
        // TODO: check the result text is intended text.
        int exitCode = chaincode.readTicket(1, "ticket1111111111");

        assertEquals(exitCode, 0);
    }

    @Test
    void getAllTickets() {
        // TODO: check the result text is intended text.
        int exitCode = chaincode.getAllTickets(1);

        assertEquals(exitCode, 0);
    }

    @Test
    void createTicket() {
        // TODO: check the result text is intended text.
        chaincode.createTicket(1, "ticket3", "2", "2", "me");
        int exitCode = chaincode.readTicket(1, "ticket3");

        assertEquals(exitCode, 0);
    }

    @Test
    void createTicket2() {
        // TODO: check the result text is intended text.
        chaincode.createTicket(1, "ticket1000", "2", "2", "me");
        int exitCode = chaincode.readTicket(1, "ticket1000");

        assertEquals(exitCode, 0);
    }

    @Test
    void transferTicket() {
        // TODO: check the result text is intended text.
        chaincode.transferTicket(1, "ticket3", "you");
        int exitCode = chaincode.readTicket(1, "ticket3");

        assertEquals(exitCode, 0);
    }

    @AfterEach
    void tearDown() {
        List<TicketDto> ticketDtoList = chaincode.getResult();
        for (TicketDto ticket: ticketDtoList) {
            System.out.println(ticket.toString());
        }
    }
}