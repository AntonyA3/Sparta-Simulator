package com.spartaglobal.spartasimulator;


import org.apache.logging.log4j.Logger;

public class Main {
    public static Logger logger;
    public static void main(String[] args) {
        DisplayManager view = new DisplayManager();
        String userInput = view.promptUserInput(DisplayManager.Message.MONTHS);
        int numberOfMonths = 0;
        int min = 2;
        int max = 20;
        InputParser inputParser = new InputParser();
        while (!(numberOfMonths >= min && numberOfMonths <= max)){
            userInput = view.promptUserInput(DisplayManager.Message.INVALID_INPUT_MONTHS);
            numberOfMonths = inputParser.parseInt(userInput, min, max);
        }
    }

}
