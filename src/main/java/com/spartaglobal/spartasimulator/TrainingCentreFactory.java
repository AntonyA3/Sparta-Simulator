package com.spartaglobal.spartasimulator;

import java.util.Random;

public class TrainingCentreFactory {

    private int nextID;
    private static final int STARTING_ID = 0;
    private static final Random rand = new Random();
    private static final String[] CENTRE_TYPES = {"Training Hub", "Boot Camp", "Tech Centre"};

    public TrainingCentre makeCentre(String centreType) {
        TrainingCentre tc =  switch(centreType) {
            case "Training Hub" -> new TrainingHub(nextID);
            case "Boot Camp" -> new BootCamp(nextID);
            case "Tech Centre" -> new TechCentre(nextID);
            default -> null; // this should never happen
        };
        nextID++;
        return tc;
    }

    public TrainingCentre makeCentre() { return makeCentre(CENTRE_TYPES[rand.nextInt(CENTRE_TYPES.length)]); }

}
