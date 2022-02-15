package com.spartaglobal.spartasimulator;

public abstract class TrainingCentre {
    private int trainingCentreID;
    private int trainingCentreCapacity;

    public TrainingCentre(int trainingCentreID, int trainingCentreCapacity){
        this.trainingCentreID = trainingCentreID;
        this.trainingCentreCapacity = trainingCentreCapacity;
    }

    public int getTrainingCentreID() {
        return trainingCentreID;
    }

    public int getTrainingCentreCapacity() {
        return trainingCentreCapacity;
    }

}
