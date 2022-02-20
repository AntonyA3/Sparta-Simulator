package com.spartaglobal.spartasimulator;

import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeDAOTest {

    private static TraineeDAO tDAO;

    @BeforeAll
    public static void setUp() {
        tDAO = new TraineeDAO();
        tDAO.openConnection();
    }

    @AfterAll
    public static void close() {
        tDAO.closeConnection();
    }

    @Test
    @DisplayName("Given a valid client, insertClients, inserts the record correctly in the clients database")
    public void givenValidClient_insertClients_InsertsTheRecordCorrectly() throws SQLException {    // Done
        boolean doesExist = false;
        Statement st = tDAO.getConnection().createStatement();
        tDAO.createTables();
        Client client = new Client(12, "state", "req", 2, 20);
        tDAO.insertClient(client);

        ResultSet rs = st.executeQuery("SELECT * FROM clients");
        while (rs.next()) {
            int clientId = rs.getInt("client_id");
            String clientState = rs.getString("client_state");
            String clientReqType = rs.getString("client_req_type");
            int startMonth = rs.getInt("client_req_start_month");
            int reqQuantity = rs.getInt("client_req_quantity");

            if (clientId == 12 && clientState.equals("state") && clientReqType.equals("req")
                    && startMonth == 2 && reqQuantity == 20) doesExist = true;
        }
        st.close();
        assertTrue(doesExist);
    }

    @Test
    @DisplayName("Given a valid client, getClients, returns the correct client values")
    public void givenValidClient_getClients_ReturnsCorrectClientValues(){
        boolean doesExist = false;

        tDAO.createTables();
        Client client = new Client(12, "state", "req", 2, 20);
        tDAO.insertClient(client);

        ArrayList<Client> clients = tDAO.getClients();

        if (clients.get(0).getClientID() == 12 && clients.get(0).getState().equals("state")
                && clients.get(0).getReqType().equals("req") && clients.get(0).getReqStartMonth() == 2
                && clients.get(0).getReqQuantity() == 20)
            doesExist = true;

        assertTrue(doesExist);
    }

    @Test
    @DisplayName("Given a invalid client, getClients, returns a null pointer exception")
    public void givenInvalidClient_getClients_ReturnsNullPointerException(){
        tDAO.createTables();
        Client client = new Client(0, null, null, 0, 0);
        tDAO.insertClient(client);
        ArrayList<Client> clients = tDAO.getClients();

        NullPointerException nEx = assertThrows(NullPointerException.class, () -> clients.get(0).getState().equals("state"));
    }

    @Test
    @DisplayName("Given a valid trainee, insertTrainee, inserts the record correctly in the trainee database")
    public void givenValidTrainee_insertTrainee_InsertsTheRecordCorrectly() throws SQLException {
        boolean doesExist = false;
        Statement st = tDAO.getConnection().createStatement();

        tDAO.createTables();
        Trainee trainee = new Trainee(32, Course.DEVOPS.name, 15, 2, "state", 4);
        tDAO.insertTrainee(trainee);

        ResultSet rs = st.executeQuery("SELECT * FROM trainees");
        while (rs.next()) {
            int traineeId = rs.getInt("trainee_id");
            String course = rs.getString("course");
            Integer centreId = rs.getInt("centre_id");
            Integer reqId = rs.getInt("req_id");
            String trainingState = rs.getString("training_state");
            int monthsTraining = rs.getInt("months_training");
            if (traineeId == 32 && course.equals("DevOps") && centreId == 15 && reqId == 2
                    && trainingState.equals("state") && monthsTraining == 4) doesExist = true;
        }
        st.close();
        assertTrue(doesExist);
    }

    @Test
    @DisplayName("Given a valid trainee, getTrainee, returns the correct trainee values")
    public void givenValidTrainee_getTrainee_ReturnsCorrectTraineeValues(){
        boolean doesExist = false;

        tDAO.createTables();
        Trainee trainee = new Trainee(32, Course.DEVOPS.name, 15, 2, "state", 4);
        tDAO.insertTrainee(trainee);

        ArrayList<Trainee> trainees = tDAO.getTrainees();

        if (trainees.get(0).getTraineeID() == 32 && trainees.get(0).getTraineeCourse().equals("DevOps") &&
                trainees.get(0).getCentreID() == 15 && trainees.get(0).getReqID() == 2 &&
                trainees.get(0).getTrainingState().equals("state") && trainees.get(0).getMonthsTraining() == 4)
            doesExist = true;

        assertTrue(doesExist);
    }

    @Test
    @DisplayName("Given a invalid trainee, getTrainee, returns a null pointer exception")
    public void givenInvalidTrainee_getTrainee_ReturnsNullPointerException(){
        tDAO.createTables();
        Trainee trainee = new Trainee(0, null, 0, 0, null, 0);
        tDAO.insertTrainee(trainee);
        ArrayList<Trainee> trainees = tDAO.getTrainees();

        NullPointerException nEx = assertThrows(NullPointerException.class, () -> trainees.get(0).getTraineeCourse().equals("Java"));
    }

    @Test
    @DisplayName("Given a valid centre, insertCentre, inserts the record correctly in the centre database")
    public void givenValidCentre_insertCentre_InsertsTheRecordCorrectly() throws SQLException {
        boolean doesExist = false;
        Statement st = tDAO.getConnection().createStatement();

        tDAO.createTables();
        TrainingCentre trainingHub = new TrainingHub(15, true);
        tDAO.insertCentre(trainingHub);

        ResultSet rs = st.executeQuery("SELECT * FROM training_centres");
        while (rs.next()) {
            int centreId = rs.getInt("centre_id");
            String type = rs.getString("training_centre_type");
            int capacity = rs.getInt("training_centre_capacity");
            boolean isOpen = rs.getBoolean("training_centre_open");
            if (centreId == 15 && type.equals("TRAININGHUB") && capacity == 100
                    && isOpen == true)
                doesExist = true;
        }
        st.close();
        assertTrue(doesExist);
    }

    @Test
    @DisplayName("Given a valid centre, getCentre, returns the correct centre values")
    public void givenValidCentre_getCentre_ReturnsCorrectCentreValues(){
        boolean doesExist = false;

        tDAO.createTables();
        TrainingCentre trainingHub = new TrainingHub(0, true);
        tDAO.insertCentre(trainingHub);

        ArrayList<TrainingCentre> centres = tDAO.getCentres();

        if (centres.get(0).getTrainingCentreID() == 0 && centres.get(0).getCentreType().equals("TRAININGHUB")
                && centres.get(0).getTrainingCentreCapacity() == 100 && centres.get(0).getIsOpen() == true)
            doesExist = true;

        assertTrue(doesExist);
    }

    @Test
    @DisplayName("Given a valid requirement, insertRequirement, inserts the record correctly in the requirement database")
    public void givenValidRequirement_insertRequirement_InsertsTheRecordCorrectly() throws SQLException {
        boolean doesExist = false;
        Statement st = tDAO.getConnection().createStatement();

        tDAO.createTables();
        Client client = new Client(12, "state", "req", 2, 20);
        Requirement requirement = new Requirement(1, client);

        tDAO.insertRequirement(requirement);

        ResultSet rs = st.executeQuery("SELECT * FROM requirements");
        while (rs.next()) {
            int requirementId = rs.getInt("req_id");
            int clientId = rs.getInt("client_id");
            int assignedTrainees = rs.getInt("assigned_trainees");
            if (requirementId == 1 && clientId == 12 && assignedTrainees == 0);
                doesExist = true;
        }
        st.close();
        assertTrue(doesExist);
    }

    @Test
    @DisplayName("Given a valid requirement, getRequirement, returns the correct centre values")
    public void givenValidRequirement_getRequirement_ReturnsCorrectCentreValues(){
        boolean doesExist = false;

        tDAO.createTables();
        Client client = new Client(12, "state", "req", 2, 20);
        Requirement requirement = new Requirement(1, client);
        tDAO.insertRequirement(requirement);

        ArrayList<Requirement> requirements = tDAO.getRequirements();

        if (requirements.get(0).getReqID() == 1 && requirements.get(0).getClientID() == 12
                && requirements.get(0).getAssignedTrainees() == 0)
            doesExist = true;

        assertTrue(doesExist);
    }

    @Test
    @DisplayName("Given a invalid requirement, getRequirement, returns a null pointer exception")
    public void givenInvalidRequirement_getRequirement_ReturnsNullPointerException(){
        tDAO.createTables();
        Client client = new Client(0, null, null, 2, 20);
        Requirement requirement = new Requirement(1, client);
        tDAO.insertRequirement(requirement);
        ArrayList<Requirement> requirements = tDAO.getRequirements();

        NullPointerException nEx = assertThrows(NullPointerException.class, () -> requirements.get(0).getReqType().equals("type"));
    }
}
