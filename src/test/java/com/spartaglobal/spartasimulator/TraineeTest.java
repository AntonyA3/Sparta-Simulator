package com.spartaglobal.spartasimulator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeTest {

    @Test
    @DisplayName("Given a trainee with a valid trainee ID, getTraineeID, should return the correct ID")
    public void givenTraineeID_getTraineeID_ReturnsCorrectID(){
        Trainee trainee = new Trainee(12);
        assertEquals(12, trainee.getTraineeID());
    }
}
