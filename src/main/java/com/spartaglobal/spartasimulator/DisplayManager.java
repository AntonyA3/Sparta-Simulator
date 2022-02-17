package com.spartaglobal.spartasimulator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DisplayManager {
    private static Logger logger = LogManager.getLogger("Display Manager");

    public enum Message{
        SIMULATION_START("Simulation starting"),
        MONTHS(String.format("Please, enter the simulation length in months (Min. %d - Max. %d): ", Main.MIN_MONTHS, Main.MAX_MONTHS)),
        INVALID_INPUT("Invalid input."),
        CENTRES_OPEN("-------------------- Number of open centres open --------------------"),
        FULL_CENTRES("-------------------- Number of full centres --------------------"),
        CLOSED_CENTRES("-------------------- Number of closed centres --------------------"),
        TRAINEES_TRAINING("-------------------- Number of trainees currently training --------------------"),
        TRAINEES_WAITING("-------------------- Number of trainees on the waiting list --------------------"),
        DATA_CHOICE("Do you want to print the data each month (M) or after completing the simulation (S)?: "),
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

    public static void printMessage(Message m) {
        System.out.println(m.message);
    }

    public static void printMessage(Message m, int intValue) {
        System.out.println(String.format(m.message, intValue));
    }

    public static void printSystemInfo(TraineeDAO tdao){
        tdao.getCentres().stream().map(c -> c.getCentreType()); // Getting centre types. Probably will have to use forEach()
        tdao.getCentres().stream().filter(c -> c.isOpen() == true).filter(c -> c.getTrainingCentreCapacity() > 0).count();
        tdao.getCentres().stream().filter(c -> c.isOpen() == true).filter(c -> c.getTrainingCentreCapacity() == 0).count();
        tdao.getCentres().stream().filter(c -> c.isOpen() == false).count();
        tdao.getTrainees().stream().map(t -> t.course());// Getting course types. Probably will have to use forEach()
        tdao.getTrainees().stream().filter(t -> t.getTrainingState().equals("TRAINING")).count();
        tdao.getTrainees().stream().filter(t -> t.getTrainingState().equals("WAITING")).count();

        System.out.println(Message.CENTRES_OPEN);
        System.out.println(Message.FULL_CENTRES);
        System.out.println(Message.CLOSED_CENTRES);
        System.out.println(Message.TRAINEES_TRAINING);
        System.out.println(Message.TRAINEES_WAITING);

    }

    public static void printException(Exception e){
        logger.error(e);
    }
}