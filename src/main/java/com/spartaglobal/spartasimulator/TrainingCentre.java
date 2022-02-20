package com.spartaglobal.spartasimulator;

public abstract class TrainingCentre {
    private int trainingCentreID;
    private String centreType;
    private int trainingCentreCapacity;
    private boolean isOpen;

    public TrainingCentre(int trainingCentreID, int trainingCentreCapacity, String centreType, boolean isOpen){
        this.trainingCentreID = trainingCentreID;
        this.trainingCentreCapacity = trainingCentreCapacity;
        this.centreType = centreType;
        this.isOpen = isOpen;
    }

    public int getTrainingCentreID() { return trainingCentreID; }
    public String getCentreType() { return centreType; }
    public int getTrainingCentreCapacity() { return trainingCentreCapacity; }
    public boolean getIsOpen() { return isOpen; }

    public void setIsOpen(boolean isOpen) { this.isOpen = isOpen; }
}