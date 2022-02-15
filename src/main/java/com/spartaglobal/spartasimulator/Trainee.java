package com.spartaglobal.spartasimulator;

public class Trainee {

    private int traineeID;
    private Integer centreId;
    private String course;

    public Trainee(int traineeID){
        this.traineeID = traineeID;
        this.centreId = null;

    public Trainee(int traineeID, String course) {
        this.traineeID = traineeID;
        this.course = course;
        this.centreId = null;
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
      
    public String getTraineeCourse() { return course;}
}
