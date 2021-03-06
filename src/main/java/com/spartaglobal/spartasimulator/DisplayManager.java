package com.spartaglobal.spartasimulator;

import java.util.Scanner;
import static com.spartaglobal.spartasimulator.Main.logger;


public class DisplayManager {
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
        SIMULATION_COMPLETE("Simulation complete"),
        INPUT_PARSER_FAILED("The program failed parsing the user's input into an Integer."),
        CONNECTION_FAILED("The program couldn't create a connection between it and the database."),
        DISCONNECTION_FAILED("The program couldn't close the connection between the program and the database."),
        TABLES_CREATION_FAILED("Tables weren't either dropped or/and created."),
        GET_FAILED("The current list of %S wasn't retrieved."),
        INSERTION_FAILED("The new %s wasn't inserted in the table."),
        UPDATE_FAILED("The attemp to update %s in the table failed.");

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

    public static void printSystemInfo(int month){
        TraineeDAO tdao = new TraineeDAO();
        tdao.openConnection();
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
        tdao.closeConnection();
    }

    public static void printException(Message m, Exception e){
        logger.error(m.message + ". " + e);
    }

    public static void printException(Message m, String type, Exception e){
        logger.error(String.format(m.message, type) + e);
    }
}