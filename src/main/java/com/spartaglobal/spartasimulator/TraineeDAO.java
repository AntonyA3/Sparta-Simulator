package com.spartaglobal.spartasimulator;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.stream.Stream;

import static com.spartaglobal.spartasimulator.Main.logger;

import static com.spartaglobal.spartasimulator.Main.logger;

public class TraineeDAO {
    private Connection connection = null;
    private static int centreId = 0;
    //Training DAO Demo
    public TraineeDAO(){

    }

    /**
     * Opens the connection to the Trainees Database
     */
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
            DisplayManager.printException(DisplayManager.Message.CONNECTION_FAILED, e);
        }logger.info("Database connection open");
    }

    /**
     * Closes the connection to the Trainees Database
     */
    public void closeConnection(){
        try {
            this.connection.close();
            this.connection = null;
            logger.debug("Database connection close");
        } catch (SQLException e) {
            DisplayManager.printException(DisplayManager.Message.DISCONNECTION_FAILED, e);
        }
    }

    /**
     * Drops the tables requirements, trainees, training_centres and clients from the database
     * Then recreates the empty tables in the relational database.
     */
    public void createTables() {
        Statement statement  = null;
        try {
            statement = this.connection.createStatement();

            statement.executeUpdate("DROP TABLE IF EXISTS requirements;");
            statement.executeUpdate("DROP TABLE IF EXISTS trainees;");

            statement.executeUpdate("DROP TABLE IF EXISTS training_centres;");
            statement.executeUpdate("DROP TABLE IF EXISTS clients;");

            logger.info("Successfully dropped all tables if exist in the database");

            //Create Training Centre Table
            String sql = """
                CREATE TABLE training_centres(
                    centre_id INT, 
                    training_centre_type VARCHAR(50),
                    training_centre_capacity INT, 
                    training_centre_open BIT,
                    months_below_threshold INT, 
                    course VARCHAR (50),
                    PRIMARY KEY (centre_id)
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
                        course VARCHAR(50),
                        centre_id INT,
                        req_id INT,
                        training_state VARCHAR(50),
                        months_training int,
                        PRIMARY KEY (trainee_id)
                    );
            """;
            statement.executeUpdate(sql);


            sql = """
                SET FOREIGN_KEY_CHECKS=0;
            """;
            statement.executeUpdate(sql);

            //Create Requirements Table
            sql = """
                    CREATE TABLE requirements (
                       req_id   INT,
                       client_id    INT,
                       assigned_trainees  INT,
                       PRIMARY KEY (req_id),
                       FOREIGN KEY (client_id) REFERENCES clients(client_id)

                    );
            """;
            statement.executeUpdate(sql);
            statement.close();
            logger.info("Tables created in the database.");
        } catch (SQLException e) {
            DisplayManager.printException(DisplayManager.Message.TABLES_CREATION_FAILED, e);
        }
    }

    /***
     * Inserts the provided training Centre into the database.
     * @param trainingCentre The Training centre to Insert into the database
     *
     * **/
    public void insertCentre(TrainingCentre trainingCentre) {
        long trainingCentreExists = getCentres().stream().filter(c -> c.getTrainingCentreID() == trainingCentre.getTrainingCentreID()).count();
        String sql = "";
        if (trainingCentreExists > 0) {
            sql = """
                  UPDATE training_centres SET training_centre_open = ?, months_below_threshold = ? WHERE centre_id = ?
                    """;
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setBoolean(1, trainingCentre.getIsOpen());
                preparedStatement.setInt(3, trainingCentre.getTrainingCentreID());

                switch (trainingCentre.getCentreType()){
                    case "TRAININGHUB", "TECHCENTRE" ->{
                        preparedStatement.setInt(2, 0);
                    }
                    case "BOOTCAMP" -> {
                        preparedStatement.setInt(2, ((BootCamp)trainingCentre).getMonthsBelowThreshold());
                    }
                }
                preparedStatement.executeUpdate();
            preparedStatement.closeOnCompletion();
            } catch (SQLException e){
                DisplayManager.printException(DisplayManager.Message.UPDATE_FAILED, "training centre", e);
            }

        } else {
            sql = """
            INSERT INTO training_centres
            (centre_id, training_centre_type, training_centre_capacity, training_centre_open, months_below_threshold, course)
            VALUES(?, ?, ?, ?, ?, ?)
        """;
            try{
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, trainingCentre.getTrainingCentreID());
                preparedStatement.setString(2, trainingCentre.getCentreType());
                preparedStatement.setInt(3, trainingCentre.getTrainingCentreCapacity());
                preparedStatement.setBoolean(4, trainingCentre.getIsOpen());

                switch (trainingCentre.getCentreType()){
                    case "TRAININGHUB" ->{
                        preparedStatement.setInt(5, 0);
                        preparedStatement.setString(6, null);
                    }
                    case "BOOTCAMP" -> {
                        preparedStatement.setInt(5, ((BootCamp)trainingCentre).getMonthsBelowThreshold());
                        preparedStatement.setString(6, null);
                    }
                    case "TECHCENTRE" ->{
                        preparedStatement.setInt(5, 0);
                        preparedStatement.setString(6, ((TechCentre)trainingCentre).getCourse());
                    }
                }
                preparedStatement.executeUpdate();
            preparedStatement.closeOnCompletion();
            } catch (SQLException e){
                DisplayManager.printException(DisplayManager.Message.INSERTION_FAILED, "training centre", e);
            }
        }
    }

    /**
     *
     * @return Array of clients that were retrieved from the database
     */
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
            rs.close();
            preparedStatement.closeOnCompletion();
        }catch (SQLException e){
            DisplayManager.printException(DisplayManager.Message.GET_FAILED, "clients", e);
        }
        return clients;
    }

    /**
     * This returns all trainees from the relational database
     * @return all trainees within the trainees table in the database
     */
    public ArrayList<Trainee> getTrainees() { return getTrainees(""); }


    /**
     *
     * @param where The Sql string for the where clause.
     * @return All the trainees from the relational database that satisfies the where clause
     */
    public ArrayList<Trainee> getTrainees(String where) {
        ArrayList<Trainee> trainees = new ArrayList<>();
        String sql = """
            SELECT trainee_id, course, centre_id, req_id, training_state, months_training
            FROM trainees 
        """ + where + ";";
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
            rs.close();
            preparedStatement.closeOnCompletion();
        } catch (SQLException e) {
            DisplayManager.printException(DisplayManager.Message.GET_FAILED, "trainees" , e);
        }
        return trainees;
    }

    /**
     * This gets all the training centres that currently exist in the database
     * @return All the training centres inside the relational database
     */
    public ArrayList<TrainingCentre> getCentres() {
        ArrayList<TrainingCentre> trainingCentres = new ArrayList<>();


        String sql = """
            SELECT centre_id, training_centre_type, training_centre_capacity, training_centre_open,  months_below_threshold, course
            FROM training_centres;
        """;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                TrainingCentre trainingCentre = null;
                int centreId = rs.getInt("centre_id");
                String trainingCentreType = rs.getString("training_centre_type");
                boolean trainingCentreOpen = rs.getBoolean("training_centre_open");
                int monthsBelowThreshold = rs.getInt("months_below_threshold");
                String course = rs.getString("course");

                switch (trainingCentreType){
                    case "TRAININGHUB" -> trainingCentre = new TrainingHub(centreId, trainingCentreOpen);
                    case "BOOTCAMP" -> trainingCentre = new BootCamp(centreId, trainingCentreOpen, monthsBelowThreshold);
                    case "TECHCENTRE" -> trainingCentre = new TechCentre(centreId, trainingCentreOpen, course);
                }
                trainingCentres.add(trainingCentre);
            }
            rs.close();
            preparedStatement.closeOnCompletion();
        } catch (SQLException e) {
            DisplayManager.printException(DisplayManager.Message.GET_FAILED, "training centres" , e);
        }
        return trainingCentres;
    }

    /**
     * This gets the requirements that are associated with it's client
     * @return An array of all of the requirments
     */
    public ArrayList<Requirement> getRequirements() {

        ArrayList<Requirement> requirements = new ArrayList<>();
        String sql = """
            SELECT r.req_id, r.client_id, r.assigned_trainees, c.client_req_type, c.client_req_start_month, c.client_req_quantity
            FROM requirements r
            LEFT JOIN clients c
            ON r.client_id = c.client_id;
        """;

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                int reqId = rs.getInt("req_id");
                int clientId = rs.getInt("client_id");
                String reqType = rs.getString("client_req_type");
                int reqStartMonth = rs.getInt("client_req_start_month");
                int reqQuantity = rs.getInt("client_req_quantity");
                int assignedTrainees = rs.getInt("assigned_trainees");
                Requirement requirement = new Requirement(reqId, clientId, reqType, reqStartMonth, reqQuantity, assignedTrainees);
                requirements.add(requirement);
            }
            rs.close();
            preparedStatement.closeOnCompletion();
        } catch (SQLException e) {
            DisplayManager.printException(DisplayManager.Message.GET_FAILED, "requirements" , e);
        }
        return requirements;
    }

    /**
     * Inserts the provided requirement into the database, or updates the other fields if the requirement already exists
     * @param requirement The Requirement to Insert or Update
     *
     * **/
    public void insertRequirement(Requirement requirement) {
        String sql = """
            INSERT INTO requirements
            (req_id, client_id, assigned_trainees)
            VALUES (?, ?, ?) 
            ON DUPLICATE KEY UPDATE
            client_id = ?, 
            assigned_trainees = ?;
        """;

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,requirement.getReqID());
            preparedStatement.setInt(2, requirement.getClientID());
            preparedStatement.setInt(3, requirement.getAssignedTrainees());
            preparedStatement.setInt(4, requirement.getClientID());
            preparedStatement.setInt(5, requirement.getAssignedTrainees());
            preparedStatement.executeUpdate();
            preparedStatement.closeOnCompletion();
        } catch (SQLException e) {
            DisplayManager.printException(DisplayManager.Message.INSERTION_FAILED, "requirement" , e);
        }

    }

    /**
     * Inserts the provided client into the database, or updates the other fields if the client already exists
     * @param c The Client to Insert or Update
     *
     ***/
    public void insertClient(Client c) {
        String sql = """
                INSERT INTO clients
                (client_id, client_state, client_req_type, client_req_start_month, client_req_quantity)
                VALUES (?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE 
                client_state = ?, client_req_type = ?, 
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
            preparedStatement.closeOnCompletion();
        } catch (SQLException e) {
            DisplayManager.printException(DisplayManager.Message.INSERTION_FAILED, "client" , e);
        }
    }

    /***
     * Inserts the provided trainee into the database, or updates the other fields if the trainee already exists
     * @param t The Trainee to Insert or Update
     *
     * **/
    public void insertTrainee(Trainee t) {
        String sql = """
            INSERT INTO trainees
            (trainee_id, course, centre_id, req_id, training_state, months_training )
            VALUES (?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
            centre_id = ?, course = ?, 
            req_id = ?, training_state = ?, 
            months_training = ?;
        """;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, t.getTraineeID());
            preparedStatement.setString(2, t.getTraineeCourse());
            preparedStatement.setObject(3, t.getCentreID());
            preparedStatement.setObject(4, t.getReqID());
            preparedStatement.setString(5, t.getTrainingState());
            preparedStatement.setInt(6, t.getMonthsTraining());

            preparedStatement.setObject(7, t.getCentreID());
            preparedStatement.setString(8, t.getTraineeCourse());
            preparedStatement.setObject(9, t.getReqID());
            preparedStatement.setString(10, t.getTrainingState());
            preparedStatement.setInt(11, t.getMonthsTraining());
            preparedStatement.executeUpdate();
            preparedStatement.closeOnCompletion();
        } catch (SQLException e) {
            DisplayManager.printException(DisplayManager.Message.INSERTION_FAILED, "trainee" , e);
        }
    }

    public int[] getCentreCapacities() {
        return null;
    }

    public ArrayList<Trainee> getTrainingTrainees() {
        return null;
    }

    public ArrayList<Trainee> getWaitingTrainees(boolean b) {
        return null;
    }

    /**
     * Gets the current connection instance to the database
     * @return The database connection object
     */
    public Connection getConnection() {
        return this.connection;
    }
}
