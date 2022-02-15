package com.spartaglobal.spartasimulator;

public class TrainingCentreFactory {

    public TrainingCentre makeCentre(int centreID, String centreType) {
        return switch(centreType) {
            case "traininghub" -> new TrainingHub(centreID);
            case "bootcamp" -> new BootCamp(centreID);
            case "techcentre" -> new TechCentre(centreID);
            default -> null; // this should never happen
        };
    }
}
