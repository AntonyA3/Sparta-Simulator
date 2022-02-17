package com.spartaglobal.spartasimulator;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

public class TraineeDAO {
    private Connection connection = null;

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

    public void resetTables() {
        // drop all tables and recreate them
    }

    public ArrayList<Trainee> getTrainees() {
        // select * from trainees, create an arraylist of Trainee instances made from the query results
        return null;
    }

    public ArrayList<TrainingCentre> getCentres() {
        // same as getTrainees, but for centres
        return null;
    }

    public ArrayList<Client> getClients() {
        // same as getTrainees, but for clients
        return null;
    }

    public ArrayList<Requirement> getRequirements() {
        // same as getTrainees, but for Requirements - inner join with Clients table to get all values
        return null;
    }

    public void insertTrainee(Trainee t) {
        // if a trainee with t's id exists, update it to match t's other values, else create a new trainee entry with its values
    }

    public void insertCentre(TrainingCentre tc) {
        // same as insertTrainee but for centres
    }

    public void insertClient(Client c) {
        // same as insertTrainee but for clients
    }

    public void insertRequirement(Requirement r) {
        // insert a new row into the requirements table with client id equal to c's id. the requirement id autoincrements so you don't need to set that in this method.
    }


}
