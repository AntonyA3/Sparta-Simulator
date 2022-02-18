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

    // getClients
    // insertClient
    @Test
    @DisplayName("insertClients")
    public void insertClients() throws SQLException {    // Done
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
            /*System.out.println(clientId + " " + clientState + " " + clientReqType + " " +
                    startMonth + " " + reqQuantity);*/
            if (clientId == 12 && clientState.equals("state") && clientReqType.equals("req")
                    && startMonth == 2 && reqQuantity == 20) doesExist = true;
        }
        st.close();
        assertTrue(doesExist);
    }

    @Test
    @DisplayName("getClients")
    public void getClients(){
        boolean doesExist = false;

        tDAO.createTables();
        Client client = new Client(12, "state", "req", 2, 20);
        tDAO.insertClient(client);

        ArrayList<Client> clients = tDAO.getClients();

        if (clients.get(0).getClientID() == 12 && clients.get(0).getState().equals("state") &&
        clients.get(0).getReqType().equals("req") && clients.get(0).getReqStartMonth() == 2 &&
        clients.get(0).getReqQuantity() == 20)
            doesExist = true;

        assertTrue(doesExist);
    }

    // getTrainees
    // insertTraineee

    @Test
    @DisplayName("insertTrainees")
    public void insertTrainees() throws SQLException {
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
    @DisplayName("getTrainees")
    public void getTrainees(){
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

    // getCentres
    // insertCentre

    @Test
    @DisplayName("insertCentre")
    public void insertCentre() throws SQLException {
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
            if (centreId == 15 ) doesExist = true;
        }
        st.close();
        assertTrue(doesExist);
    }

    // getRequirements
    // insertRequirements
}
