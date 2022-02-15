package com.spartaglobal.spartasimulator;

public class Trainee {

    private int traineeID;
    private String course;

    public Trainee(int traineeID, String course) {
        this.traineeID = traineeID;
        this.course = course;
    }

    public int getTraineeID(){
        return traineeID;
    }
    public String getTraineeCourse() { return course;}
}
