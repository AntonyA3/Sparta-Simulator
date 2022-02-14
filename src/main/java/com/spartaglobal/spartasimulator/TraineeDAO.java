package com.spartaglobal.spartasimulator;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

public class TraineeDAO {
    private Connection connection;
    private static int centreId = 0;
    public TraineeDAO(){

    }

    public void openConnection(){
        Properties props = new Properties();
        try {
            props.load(new FileReader("mysql.properties"));
            this.connection = DriverManager.getConnection(
                    props.getProperty("dburl"),
                    props.getProperty("dbuserid"),
                    props.getProperty("dbpassword")
            );
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(){
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTables() {
        Statement statement  = null;
        try {

            statement = this.connection.createStatement();
            statement.executeQuery("CREATE TABLE training_centres (" +
                    "centre_id int PRIMARY KEY"
            );
            statement.close();

            statement = this.connection.createStatement();
            statement.executeQuery("CREATE TABLE trainees (" +
                    "trainee_id int PRIMARY KEY," +
                    "centre_id int FOREIGN KEY);"
            );
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTrainee(Trainee t) {
        if (t == null) return;

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("" +
                    "INSERT INTO trainees(trainee_id, centre_id)" +
                    "VALUES (?, null)"
            );
            preparedStatement.setInt(1, t.getTraineeID());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTrainingCentre(){
//        PreparedStatement preparedStatement = null;
//        try {
//            preparedStatement = connection.prepareStatement("" +
//                    "INSERT INTO training_centres(trainee_id)" +
//                    "VALUES (?,?)"
//            );
//            //preparedStatement.setInt(1, t.getTraineeID());
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        Random random = new Random();

    }

    public int[] getCentreCapacities(){
//
//        try {
//            Statement statement = connection.createStatement();
//            ResultSet rs = statement.executeQuery("SELECT capacity FROM training_centres;");
//            ArrayList<Integer> capacities = new ArrayList<>();
//            while (rs.next()){
//                capacities.add(rs.getInt("capacity"));
//            }
//            int[] capacityArray = new int[capacities.size()];
//            for (int i = 0; i < capacityArray.length; i++) {
//                capacityArray[i] = capacities.get(i);
//            }
//            return capacityArray;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return new int[0];
    }

    public Trainee[] getTrainingTrainees() {
        Statement statement  = null;
        try {
            statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT trainee_id FROM trainees WHERE centre_id IS NOT NULL;");
            ArrayList<Trainee> trainees = new ArrayList<>();
            while (rs.next()){
                int id = rs.getInt("trainee_id");
                trainees.add(new Trainee(id));
            }
            statement.close();
            Trainee[] traineesList = new Trainee[trainees.size()];
            for(int i = 0; i < traineesList.length; i++){
                traineesList[i] = trainees.get(i);
            }
            return traineesList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Trainee[0];
    }

    public Trainee[] getWaitingTrainees(boolean removeTrainees){
        Statement statement = null;
        try {
            statement = this.connection.createStatement();
            ResultSet rs = null;

            statement.executeQuery("SELECT trainee_id FROM trainees WHERE centre_id IS NULL;");

            ArrayList<Trainee> trainees = new ArrayList<>();
            while (rs.next()){
                int id = rs.getInt("trainee_id");
                trainees.add(new Trainee(id));
            }
            statement.close();
            Trainee[] traineesList = new Trainee[trainees.size()];
            for(int i = 0; i < traineesList.length; i++){
                traineesList[i] = trainees.get(i);
            }
            if(removeTrainees){
                statement.executeQuery("DELETE trainee WHERE centre_id IS NULL");
            }
            return traineesList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Trainee[0];
    }

    private void removeTraineesFromWaitingList() {
        Statement statement = null;
        try {
            statement = this.connection.createStatement();
            ResultSet rs = null;

            statement.executeQuery("DELETE trainee WHERE centre_id IS NULL");

            ArrayList<Trainee> trainees = new ArrayList<>();
            while (rs.next()){
                int id = rs.getInt("trainee_id");
                trainees.add(new Trainee(id));
            }
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
