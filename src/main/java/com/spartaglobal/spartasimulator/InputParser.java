package com.spartaglobal.spartasimulator;

import java.util.Arrays;

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

    public static String parseOption(String s, String[] options){
        for(String o : options) if(o.equalsIgnoreCase(s)) return o; // else
        return "INVALID";
    }
}
