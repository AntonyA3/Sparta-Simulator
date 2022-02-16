package com.spartaglobal.spartasimulator;

import javax.xml.transform.Result;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

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
//        tdao.removeCentresWithOccupancyBelow(25);
        System.out.println("Number Of trainees currently training: " + tdao.getTrainingTraineesCount());
        System.out.println("Number of trainees on waiting list: " + tdao.getWaitingTraineesCount());
        System.out.println("Number of Full Training centres: " + tdao.getFullTrainingCentreCount());
        System.out.println("Number of Open Training Centres: " + tdao.getOpenTrainingCentreCount());


        System.out.println("These Trainees can be moved " + Arrays.toString(tdao.getIdOfTraineesInFullCentres()));
        tdao.closeConnection();
    }

    public int[] getIdOfTraineesInFullCentres(){
         Statement statement = null;
        try {
            statement = connection.createStatement();

             String sql ="""
                SELECT trainee_id 
                FROM trainees
                WHERE centre_id IN (
                    SELECT centre_id FROM (
                        SELECT tc.centre_id, tc.capacity, COUNT(t.trainee_id) AS occupancy
                        FROM training_centres tc 
                        INNER JOIN trainees t 
                        ON t.centre_id = tc.centre_id
                        GROUP BY tc.centre_id, tc.capacity
                        HAVING COUNT(t.trainee_id) = tc.capacity
                    )fc
                );
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

        return new int[0];

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
            statement.executeUpdate("DROP TABLE IF EXISTS trainees;");
            statement.executeUpdate("DROP TABLE IF EXISTS training_centres;");

            String sql = "CREATE TABLE training_centres " +
                    "(centre_id int, capacity int," +
                    "PRIMARY KEY (centre_id))";

            statement.executeUpdate(sql);

            sql = """
                    CREATE TABLE trainees (
                    trainee_id int, 
                    centre_id int,
                    course VARCHAR(50),
                    PRIMARY KEY (trainee_id),
                    FOREIGN KEY (centre_id) REFERENCES training_centres(centre_id));
            """;
            statement.executeUpdate(sql);

            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTrainee(int traineeId){

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("" +
                    "INSERT INTO trainees (trainee_id, course ,centre_id)" +
                    "VALUES (?, null, null)"
            );
            preparedStatement.setInt(1, traineeId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTraineedOrSetToWaiting(Trainee t){
        if (t == null) return;

        PreparedStatement preparedStatement = null;
        try {
            int[] ids = getIdsOfNoneFullTrainingCentres();
            Random random = new Random();

            Integer chosenCentre = null;
            if(ids.length > 0) {
                chosenCentre = ids[random.nextInt(ids.length)];
            }
            preparedStatement = connection.prepareStatement("" +
                    "INSERT INTO trainees (trainee_id, centre_id, course)" +
                    "VALUES (?, ?, ?)"
            );
            preparedStatement.setInt(1, t.getTraineeID());
            preparedStatement.setObject(2, chosenCentre);
            preparedStatement.setString(3, t.getTraineeCourse());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTrainee(Trainee t) {
        if (t == null) return;

        PreparedStatement preparedStatement = null;
        try {

            preparedStatement = connection.prepareStatement("" +
                    "INSERT INTO trainees (trainee_id, centre_id, course)" +
                    "VALUES (?, ?, ?)"
            );
            preparedStatement.setInt(1, t.getTraineeID());
            preparedStatement.setObject(2, t.getCentreId());
            preparedStatement.setObject(3, t.getTraineeCourse());
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
            int changed = preparedStatement.executeUpdate();
            //Main.logger.info ("rows affected: "  +changed );

        } catch (SQLException e) {
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
            ResultSet rs = statement.executeQuery("SELECT centre_id, capacity FROM training_centres GROUP BY centre_id;");
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



    private String trainingCentreCapacityOccupancyQuery(){
        return """
                SELECT centre_id, capacity ,COUNT(*) AS occupancy FROM (
                        SELECT tc.centre_id, tc.capacity, t.trainee_id 
                        FROM training_centres tc 
                        INNER JOIN trainees t
                        ON t.centre_id = tc.centre_id
                    ) nt
                    GROUP BY centre_id
                
        """;
    }
    public int[] getIdsOfNoneFullTrainingCentres(){
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet rs = null;
            String sql = String.format( """
                   SELECT centre_id FROM (
                        %s
                     ) cid WHERE occupancy < capacity
                    
                    """, trainingCentreCapacityOccupancyQuery());
            rs = statement.executeQuery(
                    sql
            );

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

    /**Currently Implementing**/
//    public void removeCentresWithOccupancyBelow(int minOccupancy){
//        PreparedStatement preparedStatement = null;
//        try {
//
//            String sql =String.format("""
//                DELETE FROM training_centres tc
//                INNER JOIN %s
//                ON tc.centre_id =
//                 tc WHERE occupancy < ?;
//            """, trainingCentreCapacityOccupancyQuery());
//            preparedStatement = this.connection.prepareStatement(sql);
//            preparedStatement.setInt(1, minOccupancy);
//            preparedStatement.executeUpdate();
//            preparedStatement.close();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }

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


    public ResultSet getOpenCentresByCourse(){
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = this.connection.createStatement();
            rs = statement.executeQuery("SELECT COUNT(DISTINCT(training_centres.centre_id)) AS `Total Centres`, course"+
                    " FROM trainees JOIN training_centres ON trainees.centre_id = training_centres.centre_id" +
                    " WHERE training_centres.capacity > 0 GROUP BY course;");
            return rs;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return rs;
    }

    public ResultSet getFullCentresByCourse(){
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = this.connection.createStatement();
            rs = statement.executeQuery("SELECT COUNT(DISTINCT(training_centres.centre_id)) AS `Total Centres`, course"+
                    " FROM trainees JOIN training_centres ON trainees.centre_id = training_centres.centre_id" +
                    " WHERE training_centres.capacity = 0 GROUP BY course;");
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet getCloseCentresByCourse(){
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = this.connection.createStatement();
            rs = statement.executeQuery("SELECT COUNT(DISTINCT(training_centres.centre_id)) AS `Total Centres`, course"+
                    " FROM trainees JOIN training_centres ON trainees.centre_id = training_centres.centre_id" +
                    " WHERE training_centres.isOpen = false GROUP BY course;");
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
}
