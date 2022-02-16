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
    private static int centreId = 0;

    //Training DAO Demo
    public static void main(String[] args) {
        TraineeDAO tdao = new TraineeDAO();
        tdao.openConnection();
        tdao.createTables();
        tdao.addTrainee(new Trainee(12, Course.JAVA.name));
        tdao.addTrainee(new Trainee(23, Course.BUSINESS.name));
        tdao.addTrainee(new Trainee(24, Course.DATA.name));
        tdao.addTrainee(new Trainee(25, Course.DEVOPS.name));

        tdao.addTrainingCentre(new TrainingHub(12) {
        });
        tdao.addTrainingCentre( new TrainingHub(15));
        tdao.addTrainingCentre( new TrainingHub(100));
        tdao.addTrainingCentre( new BootCamp(500));

        for (int i = 200; i < 300; i++) {
            tdao.addTrainee(i);
            tdao.updateTraineesTrainingCentre(i, 15);
        }
        tdao.updateTraineesTrainingCentre(25, 12);
        System.out.println(tdao.getWaitingTrainees(false).length);
        tdao.removeTraineesFromWaitingList();
        System.out.println("Waiting trainees " + tdao.getWaitingTrainees(false).length);
        System.out.println("All Training Centres full: " + tdao.isAllCentresFull());
        System.out.println(Arrays.toString(tdao.getIdsOfNoneFullTrainingCentres()));
        System.out.println("Removing The underoccupied training centres: ");
        tdao.removeCentresWithOccupancyBelow(25);
        System.out.println("Number Of trainees currently training: " + tdao.getTrainingTraineesCount());
        System.out.println("Number of trainees on waiting list: " + tdao.getWaitingTraineesCount());
        System.out.println("Number of Full Training centres: " + tdao.getFullTrainingCentreCount());
        System.out.println("Number of Open Training Centres: " + tdao.getOpenTrainingCentreCount());


        System.out.println("These Trainees can be moved " + Arrays.toString(tdao.getIdOfTraineesInFullCentres()));
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

    public void createTables() {
        Statement statement  = null;
        try {
            statement = this.connection.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS trainees;");
            statement.executeUpdate("DROP TABLE IF EXISTS training_centres;");
            statement.executeUpdate("DROP TABLE IF EXISTS clients;");



            //Create Training Centre Table
            String sql = """
                CREATE TABLE training_centres(
                    centre_id INT, 
                    catagory VARCHAR(50),
                    capacity INT, 
                    open BIT,
                    months_below_threshold INT,
                    PRIMARY KEY (centre_id)
                )    
            """;
            statement.executeUpdate(sql);

            //Create Clients Table
            sql = """
                CREATE TABLE clients(
                    client_id INT,
                    client_state VARCHAR(50),
                    course_requirement VARCHAR(50),
                    requirement_start_month INT,
                    trainees_required INT,
                    PRIMARY KEY (client_id)
                )
            """;
            statement.executeUpdate(sql);

            //Create Trainees Table
            sql = """
                    CREATE TABLE trainees (
                        trainee_id INT, 
                        centre_id int,
                        client_id int,
                        course VARCHAR(50),
                        training_state VARCHAR(50),
                        PRIMARY KEY (trainee_id),
                        FOREIGN KEY (centre_id) REFERENCES training_centres(centre_id)
                    );
            """;
            statement.executeUpdate(sql);
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int[] getIdOfTraineesInFullCentres(){

        createTrainingCentreWithOccupancyView();
        try {
            Statement statement = connection.createStatement();
            String sql ="""
                    SELECT trainee_id 
                    FROM training_centres_with_occupancy tcwo
                    INNER JOIN trainees t
                    ON tcwo.centre_id = t.centre_id
                    WHERE tcwo.occupancy = tcwo.capacity 
                """;
            ResultSet rs = statement.executeQuery(sql);
            ArrayList<Integer> intArrayList = new ArrayList<>();
            while (rs.next()){
                intArrayList.add(rs.getInt("trainee_id"));
            }
            int[] intArray = new int[intArrayList.size()];
            for (int i = 0; i < intArrayList.size(); i++) {
                intArray[i] = intArrayList.get(i);
            }
            return intArray;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        dropTrainingCentresWithOccupancyView();

        return new int[0];

    }

    public Connection getConnection(){
        return this.connection;
    }

    public int getOpenTrainingCentreCount() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet rs = null;
            rs = statement.executeQuery(
                    "SELECT COUNT(centre_id) AS centre_count FROM training_centres;"
            );

            while (rs.next()){
                return rs.getInt("centre_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getFullTrainingCentreCount() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet rs = null;
            String sql = """
                SELECT COUNT(centre_id) AS full_centre_count 
                FROM (
                    SELECT tc.centre_id, tc.capacity , COUNT(t.trainee_id) AS occupancy
                    FROM training_centres tc 
                    INNER JOIN trainees t 
                    ON t.centre_id = tc.centre_id
                    GROUP BY tc.centre_id, tc.capacity
                    HAVING COUNT(t.trainee_id) = tc.capacity
                 )nt;
            """;

            rs = statement.executeQuery(sql);


            while (rs.next()){
                return rs.getInt("full_centre_count");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getWaitingTraineesCount() {
        Statement statement = null;
        try {
            statement = this.connection.createStatement();
            ResultSet rs = null;

            rs = statement.executeQuery("SELECT COUNT(trainee_id) AS waiting_count FROM trainees WHERE centre_id IS NULL;");

            while (rs.next()){
                return rs.getInt("waiting_count");
            }
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public void closeConnection(){
        try {
            this.connection.close();
            this.connection = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void addTrainee(int traineeId){
        Trainee t = new Trainee(traineeId);
        t.setCourse(null);
        t.setCentreId(null);
        t.setClientId(null);
        t.setTrainingState("WAITING");
        addTrainee(t);
    }

    public void addTrainee(Trainee t) {
        if (t == null) return;
        String sql = """
            INSERT INTO trainees (trainee_id, centre_id ,client_id, course, training_state)
            VALUES (?, ?, ?, ?, ?);           
        """;
        PreparedStatement preparedStatement = null;
        try {

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, t.getTraineeID());
            preparedStatement.setObject(2, t.getCentreId());
            preparedStatement.setObject(3, t.getClientId());
            preparedStatement.setString(4, t.getTraineeCourse());
            preparedStatement.setString(5, t.getTrainingState());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTrainingCentre(TrainingCentre t){
        if (t == null) return;
        String sql = """
            INSERT INTO training_centres(centre_id, catagory, capacity, open, months_below_threshold)
            VALUES (?, ?, ?, ?, ?)
        """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, t.getTrainingCentreID());
            preparedStatement.setString(2, t.getTrainingCentreCatagory());
            preparedStatement.setInt(3, t.getTrainingCentreCapacity());
            preparedStatement.setBoolean(4, t.getTrainingCentreOpen());
            preparedStatement.setInt(5, t.getMonthsBelowThreshold());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateTraineesTrainingCentre(int traineeId, int trainingCentreId){
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("UPDATE trainees SET centre_id = ? WHERE trainee_id = ?;");
            preparedStatement.setInt(1, trainingCentreId);
            preparedStatement.setInt(2, traineeId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    boolean isAllCentresFull(){
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet rs = null;
            rs = statement.executeQuery(
                    "SELECT centre_id, capacity ,COUNT(*) AS occupancy FROM (" +
                            "SELECT tc.centre_id, tc.capacity, t.trainee_id " +
                            "FROM training_centres tc " +
                            "INNER JOIN trainees t " +
                            "ON t.centre_id = tc.centre_id " +
                            ") nt " +
                            "GROUP BY centre_id;"

            );

            //boolean full = true;
            while (rs.next()){

                //System.out.println(rs.getInt("centre_id") + ", " + rs.getInt("occupancy"));
                if( rs.getInt("occupancy") <  rs.getInt("capacity")){
                    return false;
                }
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    int getTrainingCentreOccupancies(){
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT capacity FROM training_centres, "
            );
            //preparedStatement.setInt(1, t.getTrainingCentreID());
            //preparedStatement.setInt(2, trainee.getTraineeID());
            ResultSet rs = preparedStatement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTrainingTraineesCount(){
        Statement statement  = null;
        try {
            statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT COUNT(trainee_id) AS trainee_count FROM trainees WHERE centre_id IS NOT NULL ");
            while (rs.next()){
                return rs.getInt("trainee_count");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public int[] getCentreCapacities(){
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT capacity, COUNT() FROM training_centres, ;");


        } catch (SQLException e) {
            e.printStackTrace();
        }


        return new int[0];
    }

    public Trainee[] getTrainingTrainees() {
        Statement statement  = null;
        try {
            statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT trainee_id, centre_id, course FROM trainees WHERE centre_id IS NOT NULL ");
            ArrayList<Trainee> trainees = new ArrayList<>();
            while (rs.next()){
                int id = rs.getInt("trainee_id");
                Trainee trainee = new Trainee(id);
                trainee.setCentreId(rs.getInt("centre_id"));
                trainee.setCourse(rs.getString("course"));
                trainees.add(trainee);
                trainees.add(new Trainee(id, Course.JAVA.name));
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

    public int[] getIdsOfNoneFullTrainingCentres(){
        Statement statement = null;
        try {
            statement = connection.createStatement();
            createTrainingCentreWithOccupancyView();
            ResultSet rs = null;
            String sql = """
                   SELECT centre_id FROM training_centres_with_occupancy WHERE occupancy < capacity;
                    """;
            rs = statement.executeQuery(sql);
            dropTrainingCentresWithOccupancyView();

            ArrayList<Integer> intArrayList = new ArrayList<Integer>();
            while (rs.next()){
                intArrayList.add(rs.getInt("centre_id"));
            }
            int[] intArray = new int[intArrayList.size()];
            for (int i = 0; i < intArrayList.size(); i++) {
                intArray[i] = intArrayList.get(i);
            }
            return  intArray;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new int[0];
    }

    public Trainee[] getWaitingTrainees(boolean removeTrainees){
        Statement statement = null;
        try {
            statement = this.connection.createStatement();
            ResultSet rs = null;

            rs = statement.executeQuery("SELECT trainee_id, course FROM trainees WHERE centre_id IS NULL;");

            ArrayList<Trainee> trainees = new ArrayList<>();
            while (rs.next()){
                int id = rs.getInt("trainee_id");
                String course = rs.getString("course");
                Trainee trainee = new Trainee(id);
                trainee.setCourse(course);

                trainees.add(trainee);

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

    public void moveTraineeToCentre(int traineeId, int centreId){

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("" +
                    "UPDATE trainees " +
                    "SET centre_id = ?" +
                    "WHERE trainee_id = ?;"
            );
            preparedStatement.setInt(1, centreId);
            preparedStatement.setInt(2, traineeId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTraineeToRandomCentre(){

    }

    public void addTraineeToWaitingList(int traineeId){
        try {
            String sql = """
                UPDATE trainees
                SET centre_id = NULL
                WHERE trainee_id = ?;
            """;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, traineeId);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void setRandomTrainingCentreForTheseTraineeIds(){

    }

    public int[] selectTraineesIdsInCentresWithLowOccupancy(int minOccupancy){
        int[] result;
        createTrainingCentreWithOccupancyView();
        String sql = """
               SELECT trainee_id
               FROM trainees t
               INNER JOIN training_centres_with_occupancy tcwo 
               ON t.centre_id = tcwo.centre_id
               WHERE tcwo.occupancy < ? 
                
         """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, minOccupancy);
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<Integer> resultsArrayList = new ArrayList<Integer>();
            while (rs.next()){
                resultsArrayList.add(rs.getInt("trainee_id"));
            }
            int[] resultsArray = new int[resultsArrayList.size()];
            for (int i = 0; i < resultsArrayList.size(); i++) {
                resultsArray[i] = resultsArrayList.get(i);
            }
            result = resultsArray;

        }catch (SQLException e){
            e.printStackTrace();
            result = new int[0];
        }
        dropTrainingCentresWithOccupancyView();
        return result;
    }

    private void createTrainingCentreWithOccupancyView(){
        try {
                Statement statement = connection.createStatement();

                String sql =
                    """
                       DROP VIEW IF EXISTS training_centres_with_occupancy;
                    """;
                statement = connection.createStatement();
                statement.executeUpdate(sql);
                statement.close();
                sql = """
                      CREATE VIEW training_centres_with_occupancy AS
                      SELECT tc.centre_id, tc.capacity, COUNT(t.trainee_id) AS occupancy
                      FROM training_centres tc 
                      INNER JOIN trainees t
                      ON t.centre_id = tc.centre_id
                      GROUP BY tc.centre_id, tc.capacity;
                  """;
            statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void closeTrainingCentresWithLowOccupancy(int minOccupancy){
        try {
            createTrainingCentreWithOccupancyView();
            String sql = """
                        UPDATE training_centres tc
                            INNER JOIN training_centres_with_occupancy tcwo
                            ON tc.centre_id = tcwo.centre_id
                        SET tc.open = 0
                        WHERE tcwo.occupancy < ?;
                    """;
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setInt(1, minOccupancy);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            dropTrainingCentresWithOccupancyView();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void dropTrainingCentresWithOccupancyView(){
        try {
            Statement statement = connection.createStatement();
            String sql = """
                DROP VIEW IF EXISTS training_centres_with_occupancy;
            """;
        statement.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void removeCentresWithOccupancyBelow(int minOccupancy){
        createTrainingCentreWithOccupancyView();
        closeTrainingCentresWithLowOccupancy(minOccupancy);
        dropTrainingCentresWithOccupancyView();
   }

    public void removeTraineesFromWaitingList() {
        Statement statement = null;
        try {
            statement = this.connection.createStatement();
            statement.executeUpdate("DELETE FROM trainees WHERE trainee_id IS NULL ");

            ArrayList<Trainee> trainees = new ArrayList<>();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //New Methods

    public void createANewTrainingCentre(TrainingCentre t){
        String sql = """
                INSERT INTO training_centres
                (centre_id, capacity, open, under_threshold)
                VALUES(?, ?, true, 0);
                """;
    }

    public void incrementMonthOfTrainingTrainees(){
        String sql = """
                UPDATE trainees
                SET months_training = months_training + 1;
                WHERE training_state = 'TRAINING';
                """;
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void benchTraineesTrainingForOverAYear(){
        String sql = """
                UPDATE trainees
                SET training_state = 'BENCHED'
                WHERE months_training >= 12;
                """;
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public int[] getIdsOfBenchedTrainees(){
        String sql = """
                   SELECT trainee_id 
                   FROM trainees
                   WHERE training_state = 'BENCHED'; 
                """;
        int[] result = new int[0];
        ResultSet rs = null;
        try{
            Statement statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            statement.close();
            ArrayList<Integer> intArrayList = new ArrayList<>();
            while (rs.next()){
                intArrayList.add(rs.getInt("trainee_id"));
            }
            int[] intArray = new int[intArrayList.size()];
            for (int i = 0; i < intArrayList.size(); i++) {
                intArray[i] = intArrayList.get(i);
            }
            result = intArray;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }



    public int[] getWaitingClientsOrderedByWaitTime(){
        String sql = """
            SELECT client_id
            FROM clients
            WHERE client_state = 'WAITING'
            ORDER BY wait_time DESC;        
        """;
        int[] result = new int[0];
        ResultSet rs = null;
        try{
            Statement statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            statement.close();
            ArrayList<Integer> intArrayList = new ArrayList<>();
            while (rs.next()){
                intArrayList.add(rs.getInt("client_id"));
            }
            int[] intArray = new int[intArrayList.size()];
            for (int i = 0; i < intArrayList.size(); i++) {
                intArray[i] = intArrayList.get(i);
            }
            result = intArray;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

//    public void assignTraineeToValidClient(int traineeId){
//        //Find First Valid Client
//        try {
//            Statement statement = connection.createStatement();
//            String sql = """
//                        DROP VIEW IF EXISTS c.course_requirement;
//                    """;
//            statement.executeUpdate(sql);
//            sql = """
//                        CREATE VIEW course_requirement AS
//                        SELECT c.client_id, c.course_requirement, c.client_state, COUNT(trainee_id) AS trainee_count
//                        FROM clients c
//                        INNER JOIN trainees t
//                        ON c.course_requirements = t.course
//                        GROUP BY c.client_id, c.course_requirement, c.client_state
//                        HAVING t.training_state = 'TRAINING';
//                    """;
//            statement.executeUpdate(sql);
//
//            sql = """
//
//                        SELECT c.client_id
//                        FROM clients_with_trainee_count c
//                        INNER JOIN trainees t
//                        ON t.course = c.course_requirement
//                        WHERE c.trainees_required > c.trainee_count AND
//                        c.client_state = "WAITING"
//                        ORDER BY requirement_start_month
//                    """;
//             ResultSet rs = statement.executeQuery(sql);
//             int clientInt = -1;
//             while (rs.next()){
//                 clientInt = rs.getInt("client_id");
//                 break;
//             }
//
//            if(clientInt != -1){
//
//            }
//            sql = """
//                        DROP VIEW clients_with_trainee_count;
//                    """;
//            statement.executeUpdate(sql);
//
//            statement.close();
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//
//    }
//
    public void assignTraineeToValidClient(Trainee t){
        assignTraineeToValidClient(t.getTraineeID());

    }

    public void addTraineedOrSetToWaiting(Trainee t){
        if (t == null) return;
        
        int[] ids = getIdsOfNoneFullTrainingCentres();
        Random random = new Random();

        Integer chosenCentre = null;
        if(ids.length > 0) {
            chosenCentre = ids[random.nextInt(ids.length)];
        }
        Trainee newTrainee = new Trainee(t.getTraineeID());
        newTrainee.setCentreId(chosenCentre);
        newTrainee.setClientId(null);
        newTrainee.setCourse(t.getTraineeCourse());
        newTrainee.setTrainingState((chosenCentre == null) ? "WAITING" : "TRAINING");
        addTrainee(newTrainee);

    }











}
