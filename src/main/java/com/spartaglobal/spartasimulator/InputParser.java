package com.spartaglobal.spartasimulator;

public class InputParser {

    public static int parseInt(String s, int min, int max){
        try {
            int number = Integer.parseInt(s);
            if (number >= min && number <= max) {
                return number;
            }
        } catch (NumberFormatException e) {
            DisplayManager.printException(e);
        }
        return 0;
    }

    public static boolean parseBoolean(String s, String[] trueFalseValues) throws InvalidInputException {
        if(s.equalsIgnoreCase(trueFalseValues[0])) return true;
        if(s.equalsIgnoreCase(trueFalseValues[1])) return false; // else
        throw new InvalidInputException();
    }
}
