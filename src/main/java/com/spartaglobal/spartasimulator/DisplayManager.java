package com.spartaglobal.spartasimulator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DisplayManager {
    public static void main(String[] args) throws SQLException {
        TraineeDAO tdao = new TraineeDAO();
        tdao.openConnection();
        tdao.createTables();
        TraineeFactory traineesF = new TraineeFactory();
        Trainee[] trainees = traineesF.getNewTrainees(20, 30);
        tdao.addTrainingCentre(new TrainingHub(1));
        tdao.addTrainingCentre( new TrainingHub(2));
        tdao.addTrainingCentre( new TrainingHub(3));
        Random rand = new Random();
        int i = 0;
        for (Trainee trainee : trainees) {
            tdao.addTrainee(trainee);
            if (i < trainees.length - 10) {
                tdao.updateTraineesTrainingCentre(trainee.getTraineeID(), rand.nextInt(1, 4));
            }
            i++;
        }
        DisplayManager.printSystemInfo(tdao);

    }

    private static Logger logger = LogManager.getLogger("Display Manager");

    public enum Message{
        SIMULATION_START("Simulation starting."),
        MONTHS("Please, enter the simulation length in months (Min. 1 - Max. 120): "),
        INFO_GIVEN_MONTHLY("Would you like the simulation to print information monthly (M), or only after the simulation ends (S)?"),
        INVALID_INPUT("Invalid input."),
        INVALID_INPUT_MONTHS("Invalid input. " + MONTHS.message),
        TOTAL_MONTHS("Run the System for %d months."),
        CENTRES_OPEN("Number of open centres open: %d"),
        FULL_CENTRES("Number of full centres: %d"),
        TRAINEES_TRAINING("Number of trainees currently training: %d"),
        TRAINEES_WAITING("Number of trainees on the waiting list: %d"),
        DATA_CHOICE("Do you want to print the data each month (M) or after completing the simulation (S)?: "),
        SIMULATION_COMPLETE("Simulation complete");
        public final String message;
        Message(String message) {
            this.message = message;
        }
    }

    public static String promptUserInput(Message m){
        System.out.print(m.message);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        return input;
    }

    public static void printMessage(Message m) {
        System.out.println(m.message);
    }

    public static void printMessage(Message m, int intValue) {
        System.out.println(String.format(m.message, intValue));
    }

    public static void printSystemInfo(TraineeDAO traineeDao){
        Map<String,Integer> traineesCourse = new HashMap<>();
        Map<String,Integer> traineesCourseWaiting = new HashMap<>();
        ResultSet openCentresCourse = traineeDao.getOpenCentresByCourse();
        ResultSet fullCentresCourse = traineeDao.getFullCentresByCourse();
        ResultSet closeCentresCourse = traineeDao.getCloseCentresByCourse();

        String traineeCourse;

        for (Trainee trainee : traineeDao.getTrainingTrainees())
        {
            traineeCourse = trainee.getTraineeCourse();
            if (traineesCourse.containsKey(traineeCourse)) {
                traineesCourse.put(traineeCourse,traineesCourse.get(traineeCourse) + 1);
            } else {
                traineesCourse.put(traineeCourse, 1);
            }
        }

        for (Trainee trainee : traineeDao.getWaitingTrainees(false))
        {
            traineeCourse = trainee.getTraineeCourse();
            if (traineesCourseWaiting.containsKey(traineeCourse)) {
                traineesCourseWaiting.put(traineeCourse,traineesCourseWaiting.get(traineeCourse) + 1);
            } else {
                traineesCourseWaiting.put(traineeCourse, 1);
            }
        }

        System.out.println("------------------ Open ------------------");

        while (true) {
            try {
                if (!openCentresCourse.next()) {
                    break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                System.out.print(openCentresCourse.getInt(1));
                System.out.print(" ");
                System.out.print(openCentresCourse.getString(2));
                System.out.println();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println("------------------ Full ------------------");
        while (true) {
            try {
                if (!fullCentresCourse.next()) {
                    break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                System.out.print(fullCentresCourse.getInt(1));
                System.out.print(" ");
                System.out.print(fullCentresCourse.getString(2));
                System.out.println();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("------------------ Close ------------------");
            while (true) {
                try {
                    if (!closeCentresCourse.next()) {
                        break;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    System.out.print(closeCentresCourse.getInt(1));
                    System.out.print(" ");
                    System.out.print(closeCentresCourse.getString(2));
                    System.out.println();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(traineesCourse);
            System.out.println(traineesCourseWaiting);
        }
    }

    public static void printException(Exception e){
        logger.error(e);
    }
}
//            System.out.println(String.format(String.format(Message.CENTRES_OPEN.message, Arrays.stream(traineeDao.getCentreCapacities()).filter(t -> t > 0))) + "\n" +
//                    String.format(Message.FULL_CENTRES.message, Arrays.stream(traineeDao.getCentreCapacities()).filter(t -> t == 0)) + "\n" +
//                    String.format(Message.TRAINEES_TRAINING.message, traineeDao.getTrainingTrainees().length) + "\n" +
//                    String.format(Message.TRAINEES_WAITING.message, traineeDao.getWaitingTrainees(false).length)