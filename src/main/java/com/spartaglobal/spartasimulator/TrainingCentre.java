package com.spartaglobal.spartasimulator;

public class TrainingCentre {
    private int trainingCentreID;
    private int trainingCentreCapacity;
    public TrainingCentre(int traineeCentreID, int trainingCentreCapacity){
        this.trainingCentreID = traineeCentreID;
        this.trainingCentreCapacity = trainingCentreCapacity;
    }

    public int getTrainingCentreID() {
        return trainingCentreID;
    }

    public int getTrainingCentreCapacity() {
        return trainingCentreCapacity;
    }
}
