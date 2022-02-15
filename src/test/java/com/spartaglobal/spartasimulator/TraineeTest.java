package com.spartaglobal.spartasimulator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeTest {

    private static Trainee trainee;

    @BeforeAll
    public static void setUp(){
        trainee = new Trainee(12);
    }

    @Test
    @DisplayName("Given a trainee with a valid trainee ID, getTraineeID, should return the correct ID")
    public void givenTraineeID_getTraineeID_ReturnsCorrectID(){
        assertEquals(12, trainee.getTraineeID());
    }

    @Test
    @DisplayName("Given two trainees with a different traineeID, getTraineeID, should return different values")
    public void givenTwoTrainees_getTraineeID_ReturnsDifferentValues(){
        Trainee testTrainee = new Trainee(53);
        assertNotEquals(trainee.getTraineeID(), testTrainee.getTraineeID());
    }
}
