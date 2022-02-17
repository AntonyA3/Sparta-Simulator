package com.spartaglobal.spartasimulator;

import java.util.Random;

public class TrainingCentreFactory {

    private int nextID;
    private static final int STARTING_ID = 0;
    private static final Random rand = new Random();
    private static final String[] CENTRE_TYPES = {"TRAININGHUB", "BOOTCAMP", "TECHCENTRE"};

    public TrainingCentre makeCentre(String centreType) {
        TrainingCentre tc =  switch(centreType) {
            case "TRAININGHUB" -> new TrainingHub(nextID, true);
            case "BOOTCAMP" -> new BootCamp(nextID, true, 0);
            case "TECHCENTRE" -> new TechCentre(nextID, true, Course.getRandomCourse());
            default -> null; // this should never happen
        };
        nextID++;
        return tc;
    }

    public TrainingCentre makeCentre() { return makeCentre(CENTRE_TYPES[rand.nextInt(CENTRE_TYPES.length)]); }

}
