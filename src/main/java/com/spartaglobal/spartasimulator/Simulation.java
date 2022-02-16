package com.spartaglobal.spartasimulator;

import java.util.Arrays;
import java.util.Random;

public class Simulation {

    private static final int MIN_GENERATED_TRAINEES = 50;
    private static final int MAX_GENERATED_TRAINEES = 100;
    private static final double CLIENT_CREATION_CHANCE = 0.5;
    private static final Random rand = new Random();

    public static void simulate(int months, boolean infoGivenMonthly, TraineeDAO tdao){
        TraineeFactory tf = new TraineeFactory();
        TrainingCentreFactory tcf = new TrainingCentreFactory();
        ClientFactory cf = new ClientFactory();
        for(int i = 0; i < months; i++) {
            loop(i, tdao, tf, tcf, cf);
            if(infoGivenMonthly) DisplayManager.printSystemInfo(tdao);
        }
        if(!infoGivenMonthly) DisplayManager.printSystemInfo(tdao);
    }

    private static void loop(int month, TraineeDAO tdao, TraineeFactory tf, TrainingCentreFactory tcf, ClientFactory cf){
        if((month % 2) == 1) {
            TrainingCentre centre = tcf.makeCentre();
            tdao.addTrainingCentre(centre);
            if(centre.getCentreType().equals("Boot Camp")) for(int i = 0; i < 2; i++) tdao.addTrainingCentre(tcf.makeCentre("Boot Camp"));
        }

        if((month >= 12) && (rand.nextDouble() < CLIENT_CREATION_CHANCE))

        Arrays.stream(tdao.getWaitingTrainees(true)).forEach(t -> tdao.addTrainee(t));
        Arrays.stream(tf.getNewTrainees(MIN_GENERATED_TRAINEES, MAX_GENERATED_TRAINEES)).forEach(t -> tdao.addTrainee(t));
    }
}
