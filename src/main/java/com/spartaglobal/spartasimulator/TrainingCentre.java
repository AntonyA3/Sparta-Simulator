package com.spartaglobal.spartasimulator;

public abstract class TrainingCentre {
    private int trainingCentreID;
    private int trainingCentreCapacity;
    private boolean trainingCentreOpen;
    private int monthsBelowThreshold;
    public TrainingCentre(int trainingCentreID, int trainingCentreCapacity){
        this.trainingCentreID = trainingCentreID;
        this.trainingCentreCapacity = trainingCentreCapacity;
        this.trainingCentreOpen = true;
        this.monthsBelowThreshold = 0;

    }

    public int getTrainingCentreID() { return trainingCentreID; }

    public int getTrainingCentreCapacity() { return trainingCentreCapacity; }

    public abstract String getTrainingCentreCatagory();

    public boolean getTrainingCentreOpen() {
        return this.trainingCentreOpen;
    }


    public int getMonthsBelowThreshold() {
        return this.monthsBelowThreshold;
    }
}
