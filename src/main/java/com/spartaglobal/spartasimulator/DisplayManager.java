package com.spartaglobal.spartasimulator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Scanner;


public class DisplayManager {
    private static Logger logger = LogManager.getLogger("Display Manager");

    public enum Message{
        SIMULATION_START("Simulation starting"),
        MONTHS(String.format("Please, enter the simulation length in months (Min. %d - Max. %d): ", Main.MIN_MONTHS, Main.MAX_MONTHS)),
        INVALID_INPUT("Invalid input."),
        CURRENT_MONTH("------------------------- CURRENT MONTH - %d -------------------------"),
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

    public static void printSystemInfo(TraineeDAO tdao, int month){
        System.out.println(String.format(String.valueOf(Message.CURRENT_MONTH.message), month));
        String[] typeOfCentres = new String[]{"BOOTCAMP", "TRAININGHUB", "TECHCENTRE"};
        System.out.println(Message.CENTRES_OPEN.message);
        for (String typeOfCentre : typeOfCentres) {
            System.out.print(
                typeOfCentre.substring(0, 1).toUpperCase() + typeOfCentre.substring(1).toLowerCase() + ": " +
                tdao.getCentres().stream().
                        filter(c -> c.getCentreType().equals(typeOfCentre)).
                        filter(c -> c.getIsOpen() == true).
                        filter(c -> c.getTrainingCentreCapacity() > 0)
                        .count() + " ");
        }
        System.out.println("\n");
        System.out.println(Message.FULL_CENTRES.message);
        for (String typeOfCentre : typeOfCentres) {
            System.out.print(
                typeOfCentre.substring(0, 1).toUpperCase() + typeOfCentre.substring(1).toLowerCase() + ": " +
                tdao.getCentres().stream().
                        filter(c -> c.getCentreType().equals(typeOfCentre)).
                        filter(c -> c.getIsOpen() == true).
                        filter(c -> c.getTrainingCentreCapacity() == 0)
                        .count()  + " ");
        }
        System.out.println("\n");
        System.out.println(Message.CLOSED_CENTRES.message);
        for (String typeOfCentre : typeOfCentres) {
            System.out.print(
                typeOfCentre.substring(0, 1).toUpperCase() + typeOfCentre.substring(1).toLowerCase() + ": " +
                tdao.getCentres().stream().
                        filter(c -> c.getCentreType().equals(typeOfCentre))
                        .filter(c -> c.getIsOpen() == false)
                        .count() + " ");
        }
        System.out.println("\n");
        System.out.println(Message.TRAINEES_TRAINING.message);
        for (Course course : Course.values()) {
            System.out.print(
                course.name + ": " +
                tdao.getTrainees().stream()
                        .filter(t -> t.getTrainingState().equals("TRAINING"))
                        .filter(t -> t.getTraineeCourse().equals(course.name))
                        .count() + " ");
        }
        System.out.println("\n");
        System.out.println(Message.TRAINEES_WAITING.message);
        for (Course course : Course.values()) {
            System.out.print(
                course.name + ": " +
                tdao.getTrainees().stream()
                        .filter(t -> t.getTrainingState().equals("WAITING"))
                        .filter(t -> t.getTraineeCourse().equals(course.name))
                        .count() + " ");
        }
        System.out.println("\n");
    }

    public static void printException(Exception e){
        logger.error(e);
    }
}