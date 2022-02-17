package com.spartaglobal.spartasimulator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeTest {

    private static Trainee trainee;

    @BeforeAll
    public static void setUp(){
        trainee = new Trainee(12, "Java");
    }

    @Test
    @DisplayName("Given a trainee with a valid trainee ID, getTraineeID, should return the correct ID")
    public void givenTraineeID_getTraineeID_ReturnsCorrectID(){
        assertEquals(12, trainee.getTraineeID());
    }

    @Test
    @DisplayName("Given two trainees with a different traineeID, getTraineeID, should return different values")
    public void givenTwoTrainees_getTraineeID_ReturnsDifferentValues(){
        Trainee testTrainee = new Trainee(53, "Java");
        assertNotEquals(trainee.getTraineeID(), testTrainee.getTraineeID());
    }

    @Test
    @DisplayName("Given a valid trainee centre ID, setTraineeCentreId, should set the correct value")
    public void givenValidTraineeCentreId_setTraineeCentreId_SetsWithCorrectValue(){
        trainee.setCentreId(15);
        assertEquals(15, trainee.getCentreId());
    }

    @Test
    @DisplayName("Given a valid course, setCourse, should set the correct value")
    public void givenValidCourse_setCourse_SetsWithCorrectValue(){
        trainee.setCourse(Course.DEVOPS.name);
        assertEquals("DevOps", trainee.getTraineeCourse());
    }

    @Test
    @DisplayName("Given an invalid centreID, setCentreId, should return a NumberFormatException")
    public void givenInvalidCentreId_exceptionTest_ReturnsNumberFormatException(){
        NumberFormatException numEx = assertThrows(NumberFormatException.class, () -> trainee.setCentreId(Integer.valueOf("abc%$")));
    }
}
