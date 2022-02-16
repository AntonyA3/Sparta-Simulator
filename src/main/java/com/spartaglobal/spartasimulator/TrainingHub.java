package com.spartaglobal.spartasimulator;

public class TrainingHub extends TrainingCentre{

    public TrainingHub(int trainingCentreID) { super(trainingCentreID, 100); }

    @Override
    public String getTrainingCentreCatagory() {
        return "TRAINING HUB";
    }
}
