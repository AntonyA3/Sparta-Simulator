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
}
