package com.spartaglobal.spartasimulator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

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
    public void givenNonNumberInput_parseInt_ReturnsZero(String strTest){
        try {
            assertEquals(0, inputParser.parseInt(strTest, 0, 24));
        } catch (NumberFormatException e){
            System.out.println("String values cannot be converted to a number");
        }
    }

    @Test
    @DisplayName("Given a valid set of strings, parseOption, should return the input string")
    public void givenValidSetOfStrings_parseOption_ReturnsStringInput(){
        String stringTest = inputParser.parseOption("Test", new String[]{"Test", "Print", "Exit"});
        assertEquals("Test", stringTest);
    }

    @Test
    @DisplayName("Given a set of strings with non matching to string input, parseOption, should not return the correct string")
    public void givenSetOfStringsWithNoMatching_parseOption_ReturnsNull(){
        String stringTest = inputParser.parseOption("Test", new String[]{"Print", "Months", "Exit"});
        assertNotEquals("Test", stringTest);
        assertNull(stringTest);
    }

    @Test
    @DisplayName("Given an empty string array, parseOption, should return null")
    public void givenEmptyStringArray_parseOption_ReturnsNull(){
        String stringTest = inputParser.parseOption("Test", new String[]{});
        assertNull(stringTest);
    }
}
