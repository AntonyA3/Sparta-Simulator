package com.spartaglobal.spartasimulator;

public class Trainee {

    private int traineeID;
    private Integer centreId;
    private Integer clientId;
    private String course;
    private String trainingState;

    public Trainee(int traineeID) {
        this.traineeID = traineeID;
        this.centreId = null;
        this.trainingState = "WAITING";
    }
    public Trainee(int traineeID, String course) {
        this.traineeID = traineeID;
        this.course = course;
        this.centreId = null;
        this.trainingState = "WAITING";
    }

    public int getTraineeID(){
        return traineeID;
    }
    public Integer getCentreId() {
        return centreId;
    }


    public void setCentreId(Integer centreId) {
        this.centreId = centreId;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTraineeCourse() { return course;}

    public String getTrainingState() {
        return trainingState;
    }

    public void setTrainingState(String trainingState) {
        this.trainingState = trainingState;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }
}
