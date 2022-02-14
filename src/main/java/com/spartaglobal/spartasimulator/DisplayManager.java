package com.spartaglobal.spartasimulator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class DisplayManager {

    private static Logger logger = LogManager.getLogger("Display Manager");

    public enum Message{
        MONTHS("Please, enter the simulation length in months: "),
        INVALID_INPUT_MONTHS("Invalid input. " + MONTHS.message),
//        TOTAL_MONTHS("Run the System for " + "<numberOfMonths>" + " months."),
        CENTRES_OPEN("Number of open centres open: "),
        FULL_CENTRES("Number of full centres: "),
        TRAINEES_TRAINING("Number of trainees currently training: "),
        TRAINEES_WAITING("Number of trainees on the waiting list: "),
        SIMULATION_COMPLETE("Simulation complete");

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

    // Thing to work on
//    public static void printSystemInfo(TraineeDAO traineeDao){
//        System.out.println(Message.CENTRES_OPEN);
//        System.out.println(Message.FULL_CENTRES);
//        System.out.println(Message.TRAINEES_TRAINING);
//        System.out.println(Message.TRAINEES_WAITING);
//    }

    public static void printException(Exception e){
        logger.error(e);
    }
}
