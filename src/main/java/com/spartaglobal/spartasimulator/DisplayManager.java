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
        CONTINUE("Continue? 'Y' to continue, 'N' to stop"),
        PRINT_DATA_CHOICE("Do you want to print the data each month (M) or after completing the simulation (S)?: "),
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
        // SELECT training_course, COUNT(*) FROM training_centres GROUP BY (training_course);
        System.out.println(String.format(Message.CENTRES_OPEN.message, Arrays.stream(traineeDao.getCentreCapacities()).filter(t -> t > 0)) + "\n" +
                "MISSING CLOSED CENTRE\n" +
                // SELECT training_course, COUNT(*) FROM training_centres GROUP BY (training_course);
                String.format(Message.FULL_CENTRES.message, Arrays.stream(traineeDao.getCentreCapacities()).filter(t -> t == 0)) + "\n" +
                // SELECT training_course, COUNT(*) FROM trainees_training_centres GROUP BY training_course;
                String.format(Message.TRAINEES_TRAINING.message, traineeDao.getTrainingTraineesCount() + "\n" +
                // SELECT training_course, COUNT(*) FROM trainees_training_centres GROUP BY training_course;
                String.format(Message.TRAINEES_WAITING.message, traineeDao.getWaitingTrainees(false).length)
        );
    }

    public static void printException(Exception e){
        logger.error(e);
    }
}


//            System.out.println(String.format(String.format(Message.CENTRES_OPEN.message, Arrays.stream(traineeDao.getCentreCapacities()).filter(t -> t > 0))) + "\n" +
//                    String.format(Message.FULL_CENTRES.message, Arrays.stream(traineeDao.getCentreCapacities()).filter(t -> t == 0)) + "\n" +
//                    String.format(Message.TRAINEES_TRAINING.message, traineeDao.getTrainingTrainees().length) + "\n" +
//                    String.format(Message.TRAINEES_WAITING.message, traineeDao.getWaitingTrainees(false).length)