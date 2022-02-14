package com.spartaglobal.spartasimulator;

import java.sql.Connection;

public class TraineeDAO {
    private Connection connection;

    public TraineeDAO(){

    }

    public void openConnection(){

    }

    public void closeConnection(){

    }

    public void createTables(){

    }

    public void addTrainee(Trainee t) {

    }

    public void addTrainingCentre(){

    }

    public int[] getCentreCapacities(){
        return null;
    }

    public Trainee[] getTrainingTrainees() {
        return null;
    }

    public Trainee[] getWaitingTrainees(boolean removeTrainees){
        return null;
    }

    private void removeTraineesFromWaitingList() {

    }
}
