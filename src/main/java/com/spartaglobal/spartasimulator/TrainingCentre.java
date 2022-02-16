package com.spartaglobal.spartasimulator;

public abstract class TrainingCentre {
    private int trainingCentreID;
    private String centreType;
    private int trainingCentreCapacity;
    private boolean isOpen;
    private int monthsBelowThreshold;

    public TrainingCentre(int trainingCentreID, int trainingCentreCapacity, String centreType){
        this.trainingCentreID = trainingCentreID;
        this.trainingCentreCapacity = trainingCentreCapacity;
        this.centreType = centreType;
        this.isOpen = true;
        this.monthsBelowThreshold = 0;
    }

    public int getTrainingCentreID() { return trainingCentreID; }
    public String getCentreType() { return centreType; }
    public int getTrainingCentreCapacity() { return trainingCentreCapacity; }
    public boolean getIsOpen() { return isOpen; }
    public int getMonthsBelowThreshold() { return monthsBelowThreshold; }

}
