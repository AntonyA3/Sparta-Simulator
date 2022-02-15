package com.spartaglobal.spartasimulator;

import java.util.HashMap;
import java.util.Random;

public class TraineeFactory {
    private int nextID;
    private HashMap<String, Integer> courseCounts;
    private final int STARTING_ID = 0;
    private final Random rand = new Random();

    public TraineeFactory(){
        nextID = STARTING_ID;
        courseCounts = new HashMap<>();
    }

    public int getNextID(){
        return nextID;
    }

    public Trainee makeTrainee(){
        Trainee trainee = new Trainee(nextID, getRandomCourse());
        nextID++; incrementCourseCount(trainee.getCourse());
        return trainee;
    }

    private void incrementCourseCount(String course) {
        if(!courseCounts.containsKey(course)) courseCounts.put(course, 1);
        else courseCounts.put(course, (courseCounts.get(course) + 1));
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
