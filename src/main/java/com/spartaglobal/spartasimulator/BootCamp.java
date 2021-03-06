package com.spartaglobal.spartasimulator;

public class BootCamp extends TrainingCentre{
    private int monthsBelowThreshold;

    public BootCamp(int trainingCentreID, boolean isOpen, int monthsBelowThreshold) {
        super(trainingCentreID, 500, "BOOTCAMP", isOpen);
        this.monthsBelowThreshold = monthsBelowThreshold;
    }

    public int getMonthsBelowThreshold() { return monthsBelowThreshold; }

    public void setMonthsBelowThreshold(int monthsBelowThreshold) { this.monthsBelowThreshold = monthsBelowThreshold; }
}
