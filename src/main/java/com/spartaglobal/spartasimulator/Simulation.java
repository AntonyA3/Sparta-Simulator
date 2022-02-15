package com.spartaglobal.spartasimulator;

import java.util.Arrays;
import java.util.Random;

public class Simulation {

//    private static final int MIN_GENERATED_TRAINEES = 50;
//    private static final int MAX_GENERATED_TRAINEES = 100;
//    private static final int DEFAULT_CENTRE_CAPACITY = 100;
//    private static final int MAX_TRAINEES_CENTRE_TAKE = 50;
//
//    public static void simulate(int months, TraineeDAO tdao){
//        TraineeFactory tf = new TraineeFactory();
//        for(int i = 0; i < months; i++) loop(i, tdao, tf);
//    }

//    private static void loop(int month, TraineeDAO tdao, TraineeFactory tf){
//        if((month % 2) == 1) tdao.addTrainingCentre(DEFAULT_CENTRE_CAPACITY);
//        Arrays.stream(tdao.getWaitingTrainees(true)).forEach(t -> tdao.addTrainee(t, MAX_TRAINEES_CENTRE_TAKE));
//        Arrays.stream(tf.getNewTrainees(MIN_GENERATED_TRAINEES, MAX_GENERATED_TRAINEES)).forEach(t -> tdao.addTrainee(t, MAX_TRAINEES_CENTRE_TAKE));
//    }
}
