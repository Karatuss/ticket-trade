package com.ticket.Ticketing.applicationGo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;

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
    private boolean isWindows;
    private Path blockchainNetworkPath;
    private Path testNetworkPath;
    private ExecutorService executorService;


    public Chaincode(String channelName, String chaincodeName) {
        ChannelName = channelName;
        ChaincodeName = chaincodeName;

        isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        blockchainNetworkPath = Paths.get(System.getProperty("user.dir") + "/../blockchain-network");
        testNetworkPath = Paths.get(blockchainNetworkPath.toString() + "/test-network");
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

    // TODO: 1. should return JSON
    // TODO: 2. change public to private after creating smartcontract methods
    public int invoke(int orgNum, String fncName, String[] args) throws IOException, InterruptedException {
        Command command = new Command("invoke.sh", orgNum, fncName, args);
        return makeProcessExecutingCommand(command);
    }

    public int query(int orgNum, String fncName, String[] args) throws IOException, InterruptedException {
        Command command = new Command("query.sh", orgNum, fncName, args);
        return makeProcessExecutingCommand(command);
    }

}
