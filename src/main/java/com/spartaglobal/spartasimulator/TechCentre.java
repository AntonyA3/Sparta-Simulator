package com.spartaglobal.spartasimulator;

public class TechCentre extends TrainingCentre{

    private String course;

    public TechCentre(int trainingCentreID, boolean isOpen, String course) {
        super(trainingCentreID, 200, "Tech Centre", isOpen);
        this.course = course;
    }

    public String getCourse() { return course; }

}
