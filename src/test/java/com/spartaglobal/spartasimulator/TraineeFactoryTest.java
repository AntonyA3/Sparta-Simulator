package com.spartaglobal.spartasimulator;

import com.spartaglobal.spartasimulator.Course;
import com.spartaglobal.spartasimulator.Trainee;
import com.spartaglobal.spartasimulator.TraineeFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeFactoryTest {

    public static TraineeFactory traineeFactory;

    @BeforeEach
    public void setUp(){
        traineeFactory = new TraineeFactory();
    }

    @Test
    @DisplayName("Given a trainee with a valid ID, makeTrainee, should return a valid Trainee")
    public void GivenValidID_MakeTrainee_ReturnAValidTrainee() {
        Trainee trainee = new Trainee(0, Course.JAVA.name,2);
        Trainee newTrainee = traineeFactory.makeTrainee(2);

        assertEquals(trainee.getTraineeID(), newTrainee.getTraineeID());
    }

    @Test
    @DisplayName("Given a trainee with a valid ID, getNewTrainees, should return new trainees")
    public void GivenValidID_GetNewTrainees_ReturnNewTrainees() {
        Trainee trainee = new Trainee(0,Course.CSHARP.name, 3);
        Trainee[] trainee2 = traineeFactory.getNewTrainees(3,0,20);

        assertEquals(trainee.getTraineeID(), trainee2[0].getTraineeID());
    }

    @Test
    @DisplayName("Given a valid number of trainees, makeTrainee, should make valid trainees")
    public void givenValidNumberTrainees_makeTrainee_ReturnsValidTrainees(){
        ArrayList<Trainee> trainees = new ArrayList<>();

        for (int i = 0; i < 40; i++){
            trainees.add(traineeFactory.makeTrainee(2));
        }
        assertEquals(39, trainees.get(39).getTraineeID());
        assertEquals(40, trainees.size());
    }

    @Test
    @DisplayName("Given a valid number of trainees, getNewTrainees, returns a number of valid trainees")
    public void givenValidNumberTrainees_getNewTrainees_ReturnsValidTrainees(){
        boolean isInRange = false;

        Trainee[] trainees = traineeFactory.getNewTrainees(1, 1, 15);

        if (trainees.length >= 1 && trainees.length <= 15)
            isInRange = true;

        assertTrue(isInRange);
    }
}
