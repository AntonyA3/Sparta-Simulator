package com.spartaglobal.spartasimulator;

import static com.spartaglobal.spartasimulator.Main.logger;

public class InputParser {

    public static int parseInt(String s, int min, int max){
        try {
            int number = Integer.parseInt(s);
            if (number >= min && number <= max) {
                logger.info("User input casted into an Integer");
                return number;
            }
        } catch (NumberFormatException e) {
            DisplayManager.printException(DisplayManager.Message.INPUT_PARSER_FAILED, e);
        }
        return 0;
    }

    public static String parseOption(String s, String[] options){
        for(String o : options) {
            if(o.equalsIgnoreCase(s)) {
                logger.info("Valid user input option parsed to display information.");
                return o;
            }
        } // else
        logger.warn("Invalid user input option. The user will have to try again.");
        return "INVALID";
    }
}
