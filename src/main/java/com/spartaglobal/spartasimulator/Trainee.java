package com.spartaglobal.spartasimulator;

public class Trainee {

    private int traineeID;
    private String course;
    private Integer centreID;
    private Integer clientID;
    private String trainingState;
    private int trainingStartMonth;

    public Trainee(int traineeID, String course, int trainingStartMonth) {
        this(traineeID, course, null, null, "WAITING", trainingStartMonth);
    }

    public Trainee(int traineeID, String course, Integer centreID, Integer clientID, String trainingState, int trainingStartMonth) {
        this.traineeID = traineeID;
        this.course = course;
        this.centreID = centreID;
        this.clientID = clientID;
        this.trainingState = trainingState;
        this.trainingStartMonth = trainingStartMonth;
    }

    public int getTraineeID(){ return traineeID; }
    public Integer getCentreID() {
        return centreID;
    }
    public String getTraineeCourse() { return course;}
    public int getTrainingStartMonth() { return trainingStartMonth; }

    public void setCourse(String course) {
        this.course = course;
    }
    public void setCentreID(Integer centreID) {
        this.centreID = centreID;
    }
    public void setTrainingState(String trainingState) { this.trainingState = trainingState; }
}
