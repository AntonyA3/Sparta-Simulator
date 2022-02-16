package com.spartaglobal.spartasimulator;

public abstract class TrainingCentre {
    private int trainingCentreID;
    private int trainingCentreCapacity;
    private String centreType;

    public TrainingCentre(int trainingCentreID, int trainingCentreCapacity, String centreType){
        this.trainingCentreID = trainingCentreID;
        this.trainingCentreCapacity = trainingCentreCapacity;
        this.centreType = centreType;
    }

    public int getTrainingCentreID() { return trainingCentreID; }

    public int getTrainingCentreCapacity() { return trainingCentreCapacity; }

    public String getCentreType() { return centreType; }
}
