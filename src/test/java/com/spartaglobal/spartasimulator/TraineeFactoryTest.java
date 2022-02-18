package com.spartaglobal.spartasimulator;

import com.spartaglobal.spartasimulator.Course;
import com.spartaglobal.spartasimulator.Trainee;
import com.spartaglobal.spartasimulator.TraineeFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeFactoryTest {

    @Test
    @DisplayName("Given a trainee with a valid ID, makeTrainee, should return a valid Trainee")
    public void GivenValidID_MakeTrainee_ReturnATrainee() {
        TraineeFactory traineeFactory = new TraineeFactory();
        Trainee trainee = new Trainee(0, Course.JAVA.name,2);
        Trainee trainee2 = traineeFactory.makeTrainee(2);
        assertEquals(trainee.getTraineeID(), trainee2.getTraineeID());

    }

    @Test
    @DisplayName("Given a trainee with a valid ID, getNewTrainees, should return new trainees")
    public void GivenValidID_GetNewTrainees_ReturnNewTraineess() {
        TraineeFactory traineeFactory = new TraineeFactory();
        Trainee trainee = new Trainee(0,Course.CSHARP.name, 3);
        Trainee[] trainee2 = traineeFactory.getNewTrainees(3,0,20);
        assertEquals(trainee.getTraineeID(), trainee2[0].getTraineeID());
    }
}
