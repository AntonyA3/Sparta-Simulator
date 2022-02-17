package com.spartaglobal.spartasimulator;

public class BootCamp extends TrainingCentre{

//<<<<<<< HEAD
//    public BootCamp(int trainingCentreID) { super(trainingCentreID, 500); }
//
//    @Override
//    public String getTrainingCentreCatagory() {
//        return "BOOT CAMP";
//    }
//=======
    private int monthsBelowThreshold;

    public BootCamp(int trainingCentreID, boolean isOpen, int monthsBelowThreshold) {
        super(trainingCentreID, 500, "Boot Camp", isOpen);
        this.monthsBelowThreshold = monthsBelowThreshold;
    }

    public int getMonthsBelowThreshold() { return monthsBelowThreshold; }

    public void setMonthsBelowThreshold(int monthsBelowThreshold) { this.monthsBelowThreshold = monthsBelowThreshold; }
//>>>>>>> 7c2b994c422d8fe04eb4819360d81d12b7770007
}
