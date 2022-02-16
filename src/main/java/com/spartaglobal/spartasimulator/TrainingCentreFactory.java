package com.spartaglobal.spartasimulator;

public class TrainingCentreFactory {

    public TrainingCentre makeCentre(int centreID, String centreType) {
        return switch(centreType) {
            case "Training Hub" -> new TrainingHub(centreID);
            case "Boot Camp" -> new BootCamp(centreID);
            case "Tech Centre" -> new TechCentre(centreID);
            default -> null; // this should never happen
        };
    }
}
