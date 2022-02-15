package com.spartaglobal.spartasimulator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputParserTest {

    private static InputParser inputParser;

    @BeforeAll
    public static void setUp(){
        inputParser = new InputParser();
    }

    @Test
    @DisplayName("Given a valid integer and in the range, parseInt, should return the string input into an integer")
    public void givenValidIntegerAndInRange_parseInt_ReturnsIntValue(){
        int testInt = inputParser.parseInt("9", 0, 24);
        assertEquals(9, testInt);
    }

    @ParameterizedTest
    @ValueSource(strings = {"-23", "-12", "59", "123"})
    @DisplayName("Given an integer which is not in the range, parseInt, should return 0")
    public void givenIntegerNotInRange_parseInt_ReturnsZero(String strTest){
        assertEquals(0, inputParser.parseInt(strTest, 0, 45));
    }

    @ParameterizedTest
    @ValueSource(strings = {"asdf", "%^&", "34af$£"})
    @DisplayName("Given an input which is not a number, parseInt, should return 0")
    public void test(String strTest){
        assertEquals(0, inputParser.parseInt(strTest, 0, 24));
    }
}
