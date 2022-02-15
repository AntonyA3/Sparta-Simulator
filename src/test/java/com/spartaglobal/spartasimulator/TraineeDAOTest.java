package com.spartaglobal.spartasimulator;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledOnOs;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeDAOTest {

    private static TraineeDAO tDAO;

    @BeforeAll
    public static void setUp(){
        tDAO = new TraineeDAO();
        tDAO.openConnection();
    }

    @AfterAll
    public static void close(){
        tDAO.closeConnection();
    }

    @Test
    @DisplayName("Given creating a database table, createTable, should not throw an exception")
    public void givenCreatingATable_createTable_DoesNotThrowException(){
        assertDoesNotThrow(() -> tDAO.createTables());
    }

    @Test
    @DisplayName("Given adding a trainee in the table, addTrainee, should not throw an exception")
    public void givenAddingTree_addTrainee_DoesNotThrowException(){
        Trainee trainee = new Trainee(48);
        assertDoesNotThrow(() -> tDAO.addTrainee(trainee));
    }

    @Test
    @DisplayName("Given adding a training centre, addTrainingCentre, should not throw an exception")
    public void givenAddingTrainingCentre_addTrainingCentre_DoesNotThrowException(){
        TrainingHub trainingCentre = new TrainingHub(100);
        assertDoesNotThrow(() -> tDAO.addTrainingCentre(trainingCentre));
    }

    @Test
    @DisplayName("Given getting the centre capacities, getCentreCapacities, should not throw an exception")
    public void givenGettingCentreCapacities_getCentreCapacities_DoesNotThrowExceptions(){
        assertDoesNotThrow(() -> tDAO.getCentreCapacities());
    }

    @Test
    @DisplayName("Given getting the training trainees, getTrainingTrainees, should not throw an exception")
    public void givenGettingTrainingTrainees_getTrainingTrainees_DoesNotThrowException(){
        assertDoesNotThrow(() -> tDAO.getTrainingTrainees());
    }

    @Test
    @DisplayName("Given getting the waiting trainees, getWaitingTrainees, should not throw an exception")
    public void givenGettingWaitingTrainees_getWaitingTrainees_DoesNotThrowException(){
        assertDoesNotThrow(() -> tDAO.getWaitingTrainees(false));
    }
}
