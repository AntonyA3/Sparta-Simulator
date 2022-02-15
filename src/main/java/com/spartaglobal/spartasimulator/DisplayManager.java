package com.spartaglobal.spartasimulator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Scanner;

public class DisplayManager {

    private static Logger logger = LogManager.getLogger("Display Manager");

    public enum Message{
        MONTHS("Please, enter the simulation length in months: "),
        INVALID_INPUT_MONTHS("Invalid input. " + MONTHS.message),
        TOTAL_MONTHS("Run the System for %d months."),
        CENTRES_OPEN("Number of open centres open: %d"),
        FULL_CENTRES("Number of full centres: %d"),
        TRAINEES_TRAINING("Number of trainees currently training: %d"),
        TRAINEES_WAITING("Number of trainees on the waiting list: %d"),
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

    public static void printSystemInfo(TraineeDAO traineeDao){
        System.out.println(String.format(Message.CENTRES_OPEN.message, Arrays.stream(traineeDao.getCentreCapacities()).filter(t -> t > 0)));
        System.out.println(String.format(Message.FULL_CENTRES.message, Arrays.stream(traineeDao.getCentreCapacities()).filter(t -> t == 0)));
        System.out.println(String.format(Message.TRAINEES_TRAINING.message, traineeDao.getTrainingTrainees().length));
        System.out.println(String.format(Message.TRAINEES_WAITING.message, traineeDao.getWaitingTrainees(false).length));
    }

    public static void printException(Exception e){
        logger.error(e);
    }
}
