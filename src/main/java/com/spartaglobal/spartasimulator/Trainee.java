package com.spartaglobal.spartasimulator;

public class Trainee {

    private int traineeID;
    private String course;
    private Integer centreID;
    private Integer reqID;
    private String trainingState;
    private int monthsTraining;

    public Trainee(int traineeID, String course, int trainingStartMonth) {
        this(traineeID, course, null, null, "WAITING", trainingStartMonth);
    }

    public Trainee(int traineeID, String course, Integer centreID, Integer reqID, String trainingState, int monthsTraining) {
        this.traineeID = traineeID;
        this.course = course;
        this.centreID = centreID;
        this.reqID = reqID;
        this.trainingState = trainingState;
        this.monthsTraining = monthsTraining;
    }

    public int getTraineeID(){ return traineeID; }
    public Integer getCentreID() {
        return centreID;
    }
    public String getTraineeCourse() { return course;}
    public String getTrainingState() { return trainingState; }
    public int getMonthsTraining() { return monthsTraining; }

    public void setCourse(String course) {
        this.course = course;
    }
    public void setCentreID(Integer centreID) {
        this.centreID = centreID;
    }
    public void setReqID(Integer reqID) { this.reqID = reqID; }
    public void setTrainingState(String trainingState) { this.trainingState = trainingState; }
    public void incrementMonthsTraining() { monthsTraining++; }
}
