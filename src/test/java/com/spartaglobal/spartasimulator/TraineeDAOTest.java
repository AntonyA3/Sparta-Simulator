package com.spartaglobal.spartasimulator;

import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    public void givenCreatingATable_createTable_DoesNotThrowException(){    // Done
        assertDoesNotThrow(() -> tDAO.createTables());
    }
//
//    @Test
//    @DisplayName("Given adding a trainee in the table, addTrainee, should add the trainee in the database")
//    public void givenAddingTree_addTrainee_AddsTheTraineeInDatabase() throws SQLException {    // Done
//        boolean doesExists = false;
//        Trainee trainee = new Trainee(48);
//
//        Statement st = tDAO.getConnection().createStatement();
//        tDAO.createTables();
//        tDAO.addTrainee(trainee);
//
//        ResultSet rs = st.executeQuery("SELECT * FROM trainees");
//        while (rs.next()){
//            int traineeId = rs.getInt("trainee_id");
//            Integer centreId = rs.getInt("centre_id");
//            String courseName = rs.getString("course");
//            if (traineeId == 48 && centreId == 0 && courseName == null) doesExists = true;
//        }
//        st.close();
//        assertTrue(doesExists);
//    }
//
//    @Test
//    @DisplayName("Given adding a training centre, addTrainingCentre, should not throw an exception")
//    public void givenAddingTrainingCentre_addTrainingCentre_DoesNotThrowException() throws SQLException {    // Done
//        boolean doesExists = false;
//        TrainingHub trainingCentre = new TrainingHub(15);
//
//        Statement st = tDAO.getConnection().createStatement();
//        tDAO.createTables();
//        tDAO.addTrainingCentre(trainingCentre);
//
//        ResultSet rs = st.executeQuery("SELECT * FROM `training_centres`");
//        while (rs.next()){
//            int centreId = rs.getInt("centre_id");
//            int capacity = rs.getInt("capacity");
//            if (centreId == 15 && capacity == 100) doesExists = true;
//        }
//
//        st.close();
//        assertTrue(doesExists);
//    }

    // TODO
    /*@Test
    @DisplayName("selectTraineesIdsInCentresWithLowOccupancyTest")
    public void selectTraineesIdsInCentresWithLowOccupancy(){
        tDAO.createTables();
        tDAO.createTrainingCentreWithOccupancyView();
        tDAO.addTrainee(8);
        tDAO.selectTraineesIdsInCentresWithLowOccupancy(15);
    }*/

//    @Test
//    @DisplayName("Given getting the centre capacities, getCentreCapacities, should not throw an exception")
//    public void givenGettingCentreCapacities_getCentreCapacities_DoesNotThrowExceptions(){
//        assertDoesNotThrow(() -> tDAO.getCentreCapacities());
//    }
//
//    @Test
//    @DisplayName("Given getting the training trainees, getTrainingTrainees, should not throw an exception")
//    public void givenGettingTrainingTrainees_getTrainingTrainees_DoesNotThrowException(){
//        assertDoesNotThrow(() -> tDAO.getTrainingTrainees());
//    }
//
//    @Test
//    @DisplayName("Given getting the waiting trainees, getWaitingTrainees, should not throw an exception")
//    public void givenGettingWaitingTrainees_getWaitingTrainees_DoesNotThrowException(){
//        assertDoesNotThrow(() -> tDAO.getWaitingTrainees(false));
//    }
//
//    @Test
//    @DisplayName("Given getting ID of trainees in the full centres, getIdOfTraineesInFullCentres, should not throw an exception")
//    public void givenGettingTraineeIdInFullCentres_getIdOfTraineesInFullCentres_DoesNotThrowException(){
//        assertDoesNotThrow(() -> tDAO.getIdOfTraineesInFullCentres());
//    }
//
//    @Test
//    @DisplayName("Given getting the amount of open training centres, getOpenTrainingCentre, should not throw an exception")
//    public void givenGettingAmountOfOpenTrainingCentres_getOpenTrainingCentreCount_DoesNotThrowException(){
//        assertDoesNotThrow(() -> tDAO.getOpenTrainingCentreCount());
//    }
//
//    @Test
//    @DisplayName("Given getting the amount of full training centres, getFullTrainingCentreCount, should not throw an exception")
//    public void givenGettingAmountOfFullTrainingCentres_getFullTrainingCentreCount_DoesNotThrowException(){
//        assertDoesNotThrow(() -> tDAO.getFullTrainingCentreCount());
//    }
//
//    @Test
//    @DisplayName("Given getting the amount of waiting trainees, getWaitingTraineesCount, should not throw an exception")
//    public void givenGettingAmountOfWaitingTrainees_getWaitingTraineesCount_DoesNotThrowException(){
//        assertDoesNotThrow(() -> tDAO.getWaitingTraineesCount());
//    }
//
//    @Test
//    @DisplayName("Given removing trainees from waiting list, removeTraineesFromWaitingList, should not throw an exception")
//    public void givenRemovingTraineesFromWaitingList_removeTraineesFromWaitingList_DoesNotThrowException(){
//        assertDoesNotThrow(() -> tDAO.removeTraineesFromWaitingList());
//    }
//
//    @Test
//    @DisplayName("Given moving trainee to a different centre, moveTraineeToCentre, should not throw an exception")
//    public void givenMovingTraineeToDifferentCentre_moveTraineeToCentre_DoesNotThrowException(){
//        tDAO.addTrainee(7);
//        assertDoesNotThrow(() -> tDAO.moveTraineeToCentre(7, 15));
//    }
//
//    @Test
//    @DisplayName("Given getting IDs of none full training centres, getIdsOfNoneFullTrainingCentres, should not throw an exception")
//    public void givenGettingIdsOfNoneFullTrainingCentre_getIdsOfNoneFullTrainingCentres_DoesNotThrowException(){
//        assertDoesNotThrow(() -> tDAO.getIdsOfNoneFullTrainingCentres());
//    }
//
//    @Test
//    @DisplayName("Given getting the amount of training trainees, getTrainingTraineesCount, should not throw an exception")
//    public void givenGettingAmountOfTrainingTrainees_getTrainingTraineesCount_DoesNotThrowException(){
//        assertDoesNotThrow(() -> tDAO.getTrainingTraineesCount());
//    }
//
//    @Test
//    @DisplayName("Given a check if all centres are full, isAllCentresFull, should not throw an exception")
//    public void givenACheckIfAllCentresAreFull_isAllCentresFull_DoesNotThrowException(){
//        assertDoesNotThrow(() -> tDAO.isAllCentresFull());
//    }
//
//    @Test
//    @DisplayName("Given adding a trainee or set them to waiting, addTraineeOrSetToWaiting, should not throw an exception")
//    public void givenAddingTraineeOrSetToWaiting_addTraineeOrSetToWaiting_DoesNotThrowException(){
//        Trainee trainee = new Trainee(23);
//        assertDoesNotThrow(() -> tDAO.addTraineedOrSetToWaiting(trainee));
//    }
//
//    @Test
//    @DisplayName("Given updating trainees training centre, updateTraineesTrainingCentre, should not throw an exception")
//    public void givenUpdatingTraineesTrainingCentre_updateTraineesTrainingCentre_DoesNotThrowException(){
//        assertDoesNotThrow(() -> tDAO.updateTraineesTrainingCentre(2, 15));
//    }
}
