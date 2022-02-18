package com.spartaglobal.spartasimulator;


import org.apache.logging.log4j.Logger;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static Logger logger;
    public static final int MIN_MONTHS = 1;
    public static final int MAX_MONTHS = 120;


    public static void main(String[] args) {
        TraineeDAO tdao = new TraineeDAO();
        tdao.openConnection();
        tdao.createTables();
        int months = getMonths();
        boolean infoGivenMonthly = getInfoGivenMonthly();
        DisplayManager.printMessage(DisplayManager.Message.SIMULATION_START, months);
        Simulation.simulate(months, infoGivenMonthly, tdao);
        DisplayManager.printMessage(DisplayManager.Message.SIMULATION_COMPLETE);
    }

    private static int getMonths() {
        int months;
        do{
            months = InputParser.parseInt(DisplayManager.promptUserInput(DisplayManager.Message.MONTHS), MIN_MONTHS, MAX_MONTHS);
            if(months != 0) return months; // else
            DisplayManager.printMessage(DisplayManager.Message.INVALID_INPUT);
        } while(true);
    }

    private static boolean getInfoGivenMonthly() {
        do{
            switch(InputParser.parseOption(DisplayManager.promptUserInput(DisplayManager.Message.DATA_CHOICE), new String[] {"M", "S"})) {
                case "M" -> { return true; }
                case "S" -> { return false; }
                default -> DisplayManager.printMessage(DisplayManager.Message.INVALID_INPUT);
            }
        }while(true);
    }
}
