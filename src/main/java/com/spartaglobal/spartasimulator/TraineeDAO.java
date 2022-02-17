package com.spartaglobal.spartasimulator;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class TraineeDAO {
    private Connection connection = null;
    private static int centreId = 0;
    //Training DAO Demo
    public TraineeDAO(){

    }
    public void openConnection(){
        try {
            if(this.connection == null) {
                Properties props = new Properties();
                props.load(new FileReader("mysql.properties"));
                connection = DriverManager.getConnection(
                        props.getProperty("dburl"),
                        props.getProperty("dbuserid"),
                        props.getProperty("dbpassword"));
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(){
        try {
            this.connection.close();
            this.connection = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTables() {
        Statement statement  = null;
        try {
            statement = this.connection.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS requirements;");

            statement.executeUpdate("DROP TABLE IF EXISTS trainees;");
            statement.executeUpdate("DROP TABLE IF EXISTS training_centres;");
            statement.executeUpdate("DROP TABLE IF EXISTS clients;");


            //Create Training Centre Table
            String sql = """
                CREATE TABLE training_centres(
                    training_centre_id INT, 
                    training_centre_type VARCHAR(50),
                    training_centre_capacity INT, 
                    training_centre_open BIT,
                    PRIMARY KEY (training_centre_id)
                )    
            """;
            statement.executeUpdate(sql);

            //Create Clients Table
            sql = """
                CREATE TABLE clients(
                    client_id INT,
                    client_state VARCHAR(50),
                    client_req_type VARCHAR(50),
                    client_req_start_month INT,
                    client_req_quantity INT,
                    PRIMARY KEY (client_id)
                )
            """;
            statement.executeUpdate(sql);

            //Create Trainees Table
            sql = """
                    CREATE TABLE trainees (
                        trainee_id INT, 
                        centre_id INT,
                        course VARCHAR(50),
                        req_id INT,
                        training_state VARCHAR(50),
                        months_training int,
                        PRIMARY KEY (trainee_id),
                        FOREIGN KEY (centre_id) REFERENCES training_centres(training_centre_id)
                    );
            """;
            statement.executeUpdate(sql);

            //Create Requirements Table
            sql = """
                    CREATE TABLE requirements (
                       req_id   int,
                       client_id    int,
                       req_type     VARCHAR(50),
                       req_start_mont   INT,
                       req_quantity     INT,
                       assigned_trainees    INT   
                    );
            """;
            statement.executeUpdate(sql);

            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void insertCentre(TrainingCentre trainingCentre) {
        String sql = """
            INSERT INTO training_centres
            (training_centre_id, training_centre_type, training_centre_capacity, training_centre_open)
            VALUES(?, ?, ?, ?)       
        """;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, trainingCentre.getTrainingCentreID());
            preparedStatement.setString(2, trainingCentre.getCentreType());
            preparedStatement.setInt(3, trainingCentre.getTrainingCentreCapacity());
            preparedStatement.setBoolean(4, trainingCentre.getIsOpen());
            preparedStatement.executeUpdate();
        }catch (SQLException e){

        }
    }

    public ArrayList<Client> getClients() {
        ArrayList<Client> clients = new ArrayList<>();
        String sql = """
            SELECT client_id, client_state, client_req_type, client_req_start_month, client_req_quantity
            FROM clients;
        """;

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                int id = rs.getInt("client_id");
                String state = rs.getString("client_state");
                String reqType = rs.getString("client_req_type");
                Integer reqStartMonth = (rs.getObject("client_req_start_month") != null) ?
                            rs.getInt("client_req_start_month") : null;
                Integer reqQuantity = (rs.getObject("client_req_quantity") != null) ?
                        rs.getInt("client_req_quantity") : null;
                Client client = new Client(id, state, reqType, reqStartMonth, reqQuantity);
                clients.add(client);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return clients;
    }

    public ArrayList<Trainee> getTrainees() {
        ArrayList<Trainee> trainees = new ArrayList<>();
        String sql = """
            SELECT trainee_id, course, centre_id, req_id, training_state, months_training
            FROM trainees;
        """;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                int traineeId = rs.getInt("trainee_id");
                String course = rs.getString("course");
                Integer centreId = (rs.getObject("centre_id") != null) ?
                        rs.getInt("centre_id") : null;
                Integer reqId = (rs.getObject("req_id") != null) ?
                        rs.getInt("req_id") : null;
                String trainingState = rs.getString("training_state");
                int monthsTraining = rs.getInt("months_training");
                Trainee trainee = new Trainee(traineeId, course, centreId, reqId, trainingState, monthsTraining);
                trainees.add(trainee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trainees;
    }

    public ArrayList<TrainingCentre> getCentres() {
        ArrayList<TrainingCentre> trainingCentres = new ArrayList<>();
        String sql = """
            SELECT training_centre_id, training_centre_type, training_centre_capacity, training_centre_open
            FROM training_centres;
        """;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                TrainingCentre trainingCentre = new TrainingCentreFactory().makeCentre(rs.getString("training_centre_type"));
                trainingCentre.setIsOpen(rs.getBoolean("training_centre_open"));
                trainingCentres.add(trainingCentre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trainingCentres;
    }

    public ArrayList<Requirement> getRequirements() {
        ArrayList<Requirement> requirements = new ArrayList<Requirement>();
        String sql = """
            SELECT req_id, client_id, req_type, req_start_month, req_quantity, assigned_trainees     
            FROM requirements;
        """;

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                int reqId = rs.getInt("req_id");
                int clientId = rs.getInt("client_id");
                String reqType = rs.getString("req_type");
                int reqStartMonth = rs.getInt("req_start_month");
                int reqQuantity = rs.getInt("req_quantity");
                int assignedTrainees = rs.getInt("assigned_trainees");
                Requirement requirement = new Requirement(reqId, clientId, reqType, reqStartMonth, reqQuantity, assignedTrainees);
                requirements.add(requirement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requirements;
    }

    /***
     * @param requirement The Requirement to Insert or Update
     *
     * Inserts the provided requirement into the database, or updates the other fields if the requirement already exists**/
    public void insertRequirement(Requirement requirement) {
        String sql = """
            INSERT INTO requirements
            (req_id, client_id, req_type, req_start_month, req_quantity, assigned_trainees)
            VALUES (?, ?, ?, ?, ?, ?) 
            ON DUPLICATE KEY UPDATE
            client_id = ?, req_type = ?, 
            req_start_month = ?, req_quantity = ?, 
            assigned_trainees = ?;
        """;

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,requirement.getReqID());
            preparedStatement.setInt(2, requirement.getClientID());
            preparedStatement.setString(3, requirement.getReqType());
            preparedStatement.setInt(4, requirement.getReqStartMonth());
            preparedStatement.setInt(5, requirement.getReqQuantity());
            preparedStatement.setInt(6, requirement.getAssignedTrainees());

            preparedStatement.setInt(7, requirement.getClientID());
            preparedStatement.setString(8, requirement.getReqType());
            preparedStatement.setInt(9, requirement.getReqStartMonth());
            preparedStatement.setInt(10, requirement.getReqQuantity());
            preparedStatement.setInt(11, requirement.getAssignedTrainees());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /***
     * @param c The Client to Insert or Update
     *
     * Inserts the provided client into the database, or updates the other fields if the client already exists**/
    public void insertClient(Client c) {
        String sql = """
                INSERT INTO clients
                (client_id, client_state, client_req_type, client_req_start_month, client_req_quantity)
                VALUES (?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE 
                client_state = ?, client_req_type = ? 
                client_req_start_month = ?, client_req_quantity = ?;
        """;

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,c.getClientID());
            preparedStatement.setString(2, c.getState());
            preparedStatement.setString(3,c.getReqType());
            preparedStatement.setInt(4, c.getReqStartMonth());
            preparedStatement.setInt(5, c.getReqQuantity());

            preparedStatement.setString(6, c.getState());
            preparedStatement.setString(7,c.getReqType());
            preparedStatement.setInt(8, c.getReqStartMonth());
            preparedStatement.setInt(9, c.getReqQuantity());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /***
     * @param t The Trainee to Insert or Update
     *
     * Inserts the provided trainee into the database, or updates the other fields if the trainee already exists**/
    public void insertTrainee(Trainee t) {
        String sql = """
            INSERT INTO trainees
            (trainee_id, centre_id, course, req_id, training_state, months_training )
            VALUES (?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
            centre_id = ?, course = ?, 
            req_id = ?, training_state = ?, 
            months_training = ?;
        """;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, t.getTraineeID());
            preparedStatement.setObject(2, t.getCentreID());
            preparedStatement.setString(3, t.getTraineeCourse());
            preparedStatement.setObject(4, t.getReqID());
            preparedStatement.setString(5, t.getTrainingState());
            preparedStatement.setInt(6, t.getMonthsTraining());

            preparedStatement.setObject(7, t.getCentreID());
            preparedStatement.setString(8, t.getTraineeCourse());
            preparedStatement.setObject(9, t.getReqID());
            preparedStatement.setString(10, t.getTrainingState());
            preparedStatement.setInt(11, t.getMonthsTraining());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
