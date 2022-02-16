package com.spartaglobal.spartasimulator;

public class BootCamp extends TrainingCentre{

    public BootCamp(int trainingCentreID) { super(trainingCentreID, 500); }

    @Override
    public String getTrainingCentreCatagory() {
        return "BOOT CAMP";
    }
}
