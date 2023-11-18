package com.ticket.Ticketing.applicationGo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ChaincodeTest {

    private Chaincode chaincode;

    @Test
    void constructorWorksProperly() throws IOException, InterruptedException {
        // load blockchain properties file
        String blockchainConfigPath = Thread.currentThread().getContextClassLoader().getResource("blockchain.properties").getPath();

        Properties blockchainProps = new Properties();
        blockchainProps.load(new FileInputStream(blockchainConfigPath));

        String blockchainChannelName = blockchainProps.getProperty("ticket.channelName");
        String blockchainContractBasic = blockchainProps.getProperty("ticket.contract.basic");

        // execute constructor
        chaincode = new Chaincode(blockchainChannelName, blockchainContractBasic);

        assertNotNull(chaincode, "chaincode is not generated");
    }

//    @Disabled
//    @AfterEach
//    void invokeTest() throws IOException, InterruptedException {
//        //given
//        assertNotNull(chaincode);
//
//        //when
//        int exitCode = chaincode.invoke(1, "InitLedger", new String[]{""});
//
//        //then
//        assertEquals(exitCode, 0);
//    }

    @AfterEach
    void queryTest() throws IOException, InterruptedException {
        //given
        assertNotNull(chaincode);

        //when
//        int exitCode = chaincode.query(1, "ReadAsset", new String[]{""});
        int exitCode = chaincode.query(1, "InitLedger", new String[]{""});

        //then
        assertEquals(exitCode, 0);
    }
}