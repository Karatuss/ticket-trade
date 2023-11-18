package com.ticket.Ticketing.applicationGo;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
public class Command {

    // TODO: should change int to String
    private String targetFile;
    private int orgNum;
    private String cmd;
    private String[] args;

//    public Command() {
//        this.targetFile = "";
//        this.orgNum = 1;
//        this.cmd = "";
//        this.args = null;
//    }

    public Command(String targetFile, int orgNum, String cmd, String[] args) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (isWindows) {
            targetFile = ".\\" + targetFile;
        } else {
            targetFile = "./" + targetFile;
        }

        this.targetFile = targetFile;
        this.orgNum = orgNum;
        this.cmd = cmd;
        this.args = args;
    }

    @Override
    public String toString() {
        return targetFile + " " +
                String.valueOf(orgNum) + " " +
                cmd + " " +
                String.join(" ", args);
    }
}
