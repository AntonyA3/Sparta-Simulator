package com.spartaglobal.spartasimulator;

import java.util.Random;

public class TraineeFactory {
    private int nextID;
    private final int STARTING_ID = 0;

    public TraineeFactory(){
        nextID = 0;
    }

    public int getNextID(){
        return this.nextID;
    }

    public Trainee makeTrainee(){
        Trainee trainee = new Trainee(nextID);
        nextID++;
        return trainee;
    }

    public Trainee[] getNewTrainees(int min, int max){
        Random random = new Random();
        Trainee[] trainees = new Trainee[(random.nextInt(min, (max + 1))];
        for(int i = 0; i < trainees.length; i++) trainees[i] = makeTrainee();
        return trainees;
    }
}
