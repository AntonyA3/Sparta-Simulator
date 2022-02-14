package com.spartaglobal.spartasimulator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class DisplayManager {

    private static Logger logger = LogManager.getLogger("Display Manager");

    public enum Message{
        MONTHS("Please, enter the simulation length in months: "),
        INVALID_INPUT_MONTHS("Invalid input. " + MONTHS.message);

        public final String message;
        Message(String message) {
            this.message = message;
        }
    }

    public static String promptUserInput(Message m){
        System.out.print(m.message);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        return input;
    }

    public static void printSystemInfo(Simulation sim){

    }

    public static void printException(Exception e){
        logger.error(e);
    }
}
