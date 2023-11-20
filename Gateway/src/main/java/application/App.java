package application;

import org.hyperledger.fabric.gateway.*;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class App {

    // allow to find networks with domain in localhost
    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    // helper function for getting connected to the gateway
    public static Gateway connect() throws Exception {
        // load a file system based wallet for managing identities.
        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("..", "ticket-network", "organizations", "peerOrganizations", "seller.ticket.com", "connection-seller.yaml");

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "User1").networkConfig(networkConfigPath).discovery(true);
        return builder.connect();
    }

    public static void main(String[] args) throws Exception {

        // load blockchain properties file
        String blockchainConfigPath = Thread.currentThread().getContextClassLoader().getResource("blockchain.properties").getPath();

        Properties blockchainProps = new Properties();
        blockchainProps.load(new FileInputStream(blockchainConfigPath));

        String bcChannelName = blockchainProps.getProperty("ticket.channelName");
        String bcContractTicket = blockchainProps.getProperty("ticket.contract.ticket");
        String bcContractEvent = blockchainProps.getProperty("ticket.contract.event");

        // enrolls the admin and registers the user
        try {
            EnrollAdmin.main(null);
            RegisterUser.main(null);
        } catch (Exception e) {
            System.err.println(e);
        }

        // connect to the network and invoke the smart contract
        try (Gateway gateway = connect()) {

            // get the network and contract
            Network network = gateway.getNetwork(bcChannelName);
            Contract contract = network.getContract(bcContractTicket);

            byte[] result;


        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
