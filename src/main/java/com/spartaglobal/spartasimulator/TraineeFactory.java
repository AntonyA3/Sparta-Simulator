package com.spartaglobal.spartasimulator;

import java.util.Random;

public class TraineeFactory {
    private int nextID;
    private final int STARTING_ID = 0;
    private final Random rand = new Random();

    public TraineeFactory(){
        nextID = STARTING_ID;
    }

    public int getNextID(){
        return nextID;
    }

    public Trainee makeTrainee(){
        Trainee trainee = new Trainee(nextID, getRandomCourse());
        nextID++;
        return trainee;
    }

    public Trainee[] getNewTrainees(int min, int max){
        Trainee[] trainees = new Trainee[(rand.nextInt(min, (max + 1)))];
        for(int i = 0; i < trainees.length; i++) trainees[i] = makeTrainee();
        return trainees;
    }

    private String getRandomCourse() {
        Course[] courses = Course.values();
        return courses[rand.nextInt(courses.length)].name;
    }
}
