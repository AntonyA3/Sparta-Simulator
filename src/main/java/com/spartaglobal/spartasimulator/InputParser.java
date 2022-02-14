package com.spartaglobal.spartasimulator;

public class InputParser {

    public int parseInt(String s, int min, int max){
        int number;
        number = Integer.parseInt(s);
        if (number >= min && number <= max) {
            return number;
        }
        return number;
    }
}
