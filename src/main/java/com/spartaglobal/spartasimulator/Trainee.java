package com.spartaglobal.spartasimulator;

public class Trainee {

    private int traineeID;
    private Integer centreId;
    public Trainee(int traineeID){
        this.traineeID = traineeID;
        this.centreId = null;
    }

    public int getTraineeID(){
        return this.traineeID;
    }

    public Integer getCentreId() {
        return centreId;
    }

    public void setCentreId(Integer centreId) {
        this.centreId = centreId;
    }
}
