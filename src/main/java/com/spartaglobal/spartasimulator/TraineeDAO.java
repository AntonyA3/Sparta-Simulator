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
    public static void main(String[] args) {
        TraineeDAO tdao = new TraineeDAO();
        tdao.openConnection();
        tdao.createTables();
        tdao.addTrainee(new Trainee(12));
        tdao.addTrainee(new Trainee(23));
        tdao.addTrainee(new Trainee(24));
        tdao.addTrainee(new Trainee(25));

        tdao.addTrainingCentre( new TrainingCentre(12, 100));
        System.out.println(tdao.getWaitingTrainees(false).length);
        tdao.removeTraineesFromWaitingList();
        System.out.println("Waiting trainees " + tdao.getWaitingTrainees(false).length);
        tdao.closeConnection();
    }
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
            statement.executeUpdate("DROP TABLE IF EXISTS trainees_training_centres");
            statement.executeUpdate("DROP TABLE IF EXISTS trainees;");
            statement.executeUpdate("DROP TABLE IF EXISTS training_centres;");

            String sql = "CREATE TABLE training_centres " +
                    "(centre_id int PRIMARY KEY, capacity int)";
            statement.executeUpdate(sql);

            statement.executeUpdate("CREATE TABLE trainees (" +
                    "trainee_id int," +
                    "PRIMARY KEY (trainee_id));"
            );
            statement.executeUpdate("CREATE TABLE trainees_training_centres" +
                    "(centre_id int, trainee_id int, PRIMARY KEY (trainee_id))"
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
                    "INSERT INTO trainees (trainee_id)" +
                    "VALUES (?)"
            );
            preparedStatement.setInt(1, t.getTraineeID());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTrainingCentre(TrainingCentre t){
        if (t == null) return;

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("" +
                    "INSERT INTO training_centres(centre_id, capacity)" +
                    "VALUES (?, ?)"
            );
            preparedStatement.setInt(1, t.getTrainingCentreID());
            preparedStatement.setInt(2, t.getTrainingCentreCapacity());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTraineeToTrainingCentre(int traineeId){

    }
//    public void addTrainingCentre(){
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
//    }

    public int[] getCentreCapacities(){
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT capacity, COUNT() FROM training_centres, ;");


        } catch (SQLException e) {
            e.printStackTrace();
        }


//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement();
//
//
//        }
//
//        try {
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
            ResultSet rs = statement.executeQuery("SELECT trainee_id FROM trainees_training_centres");
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

            rs = statement.executeQuery("SELECT trainee_id FROM trainees;");

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
//            if(removeTrainees){
//                statement.executeQuery("DELETE trainee WHERE centre_id IS NULL");
//            }
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
            //ResultSet rs = null;
            statement.executeUpdate("DELETE FROM trainees WHERE trainee_id NOT IN " +
                    "(SELECT trainee_id FROM trainees_training_centres)");

            ArrayList<Trainee> trainees = new ArrayList<>();
//            while (rs.next()){
//                int id = rs.getInt("trainee_id");
//                trainees.add(new Trainee(id));
//            }
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
