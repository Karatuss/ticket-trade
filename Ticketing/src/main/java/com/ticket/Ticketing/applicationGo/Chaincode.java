package com.ticket.Ticketing.applicationGo;

import com.couchbase.client.core.deps.com.fasterxml.jackson.core.type.TypeReference;
import com.couchbase.client.core.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.ticket.Ticketing.dto.TicketDto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Chaincode {

    // PUBLIC
    public String ChannelName;
    public String ChaincodeName;
    public static final String WINDOWS_SHELL = "cmd.exe";
    public static final String LINUX_SHELL = "bash";

    // PRIVATE - command arguments
    private int OrgNum;
    private String FncName;
    private String[] Args;

    // PRIVATE - local variables
    private final boolean isWindows;
    private final ExecutorService executorService;
    private final Path blockchainNetworkPath;
    private final Path testNetworkPath;
    private final Path resultFilePath;


    public Chaincode(String channelName, String chaincodeName) {
        ChannelName = channelName;
        ChaincodeName = chaincodeName;

        isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        blockchainNetworkPath = Paths.get(System.getProperty("user.dir") + "/../blockchain-network");
        testNetworkPath = Paths.get(blockchainNetworkPath + "/test-network");
        resultFilePath = Paths.get(testNetworkPath + "/result.txt");
        executorService = Executors.newFixedThreadPool(4);
    }

    private int makeProcessExecutingCommand(Command command) throws IOException, InterruptedException {
        // TODO: List<ProcessBuilder>
        // https://www.baeldung.com/run-shell-command-in-java#1-handle-pipes-1
        ProcessBuilder builder = new ProcessBuilder();

        builder.directory(testNetworkPath.toFile());    // working dir is "current dir of user".
        if (isWindows) {
            // TODO: Synchronize with bash command
            builder.command(WINDOWS_SHELL, "/c", command.toString());
        } else {
            builder.command(LINUX_SHELL, "-c", command.toString());
        }

        Process process = builder.start();  // command execution

        // process's input and output stream is connected to a thread asynchronously.
        StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
        Future<?> future = executorService.submit(streamGobbler);

        return process.waitFor();
    }

    @Deprecated
    private int invoke(int orgNum, String fncName, String[] args) throws IOException, InterruptedException {
        Command command = new Command("invoke.sh", orgNum, fncName, args);
        return makeProcessExecutingCommand(command);
    }

    @Deprecated
    private int query(int orgNum, String fncName, String[] args) throws IOException, InterruptedException {
        Command command = new Command("query.sh", orgNum, fncName, args);
        return makeProcessExecutingCommand(command);
    }

    private int letsGo(int orgNum, String fncName, String[] args) throws IOException, InterruptedException {
        Command command = new Command("lets-go.sh", orgNum, fncName, args);
        return makeProcessExecutingCommand(command);
    }

    public int initLedger(int orgNum) {
        // TODO: Currently, the organization number is fixed as 1.
        orgNum = 1;
        try {
            return letsGo(orgNum, "InitLedger", new String[]{});
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
        }
        return 0;
    }

    public int getAllTickets(int orgNum) {
        // TODO: Currently, the organization number is fixed as 1.
        orgNum = 1;
        try {
            return letsGo(orgNum, "GetAllTickets", new String[]{});
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
        }
        return 0;
    }

    public int createTicket(int orgNum, String ticketId, String eventId, int seatNum, String owner) {
        // TODO: Currently, the organization number is fixed as 1.
        orgNum = 1;
        try {
            return letsGo(orgNum, "CreateTicket", new String[]{ticketId, eventId, String.valueOf(seatNum), owner});
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
        }
        return 0;
    }

    public int readTicket(int orgNum, String ticketId) {
        // TODO: Currently, the organization number is fixed as 1.
        orgNum = 1;
        try {
            return letsGo(orgNum, "ReadTicket", new String[]{ticketId});
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
        }
        return 0;
    }

    public int transferTicket(int orgNum, String ticketId, String newOwner) {
        // TODO: Currently, the organization number is fixed as 1.
        orgNum = 1;
        try {
            return letsGo(orgNum, "TransferTicket", new String[]{ticketId, newOwner});
        } catch (IOException | InterruptedException e) {
            System.err.println(e);
        }
        return 0;
    }

    public List<TicketDto> getResult() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(resultFilePath.toString()));

            String line;
            StringBuilder lines = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                lines.append(line);
            }

            List<TicketDto> ticketDtoList = new ArrayList<>();
            if (lines.isEmpty()) {
                System.out.println("No Ticket");
            } else if (lines.toString().startsWith("{")) {
                ticketDtoList.add(new ObjectMapper().readValue(lines.toString(), TicketDto.class));
            } else {
                ticketDtoList = new ObjectMapper().readValue(lines.toString(), new TypeReference<List<TicketDto>>() {});
            }

            return ticketDtoList;
        } catch (IOException e) {
            System.err.println(e);
        }

        return null;
    }

}
