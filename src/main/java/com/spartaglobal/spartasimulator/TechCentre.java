package com.spartaglobal.spartasimulator;

public class TechCentre extends TrainingCentre{

    public TechCentre(int trainingCentreID) { super(trainingCentreID, 200); }

    @Override
    public String getTrainingCentreCatagory() {
        return "TECH CENTRE";
    }
}
