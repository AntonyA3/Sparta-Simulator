package com.spartaglobal.spartasimulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Simulation {

    private static final int MIN_GENERATED_TRAINEES = 50;
    private static final int MAX_GENERATED_TRAINEES = 100;
    private static final double CLIENT_CREATION_CHANCE = 0.5;
    private static final Random rand = new Random();

    public static void simulate(int months, boolean infoGivenMonthly, TraineeDAO tdao){
        TraineeFactory tf = new TraineeFactory();
        TrainingCentreFactory tcf = new TrainingCentreFactory();
        ClientFactory cf = new ClientFactory();
        for(int i = 0; i < months; i++) {
            loop(i, tdao, tf, tcf, cf);
            if(infoGivenMonthly) DisplayManager.printSystemInfo(tdao);
        }
        if(!infoGivenMonthly) DisplayManager.printSystemInfo(tdao);
    }

    private static void loop(int month, TraineeDAO tdao, TraineeFactory tf, TrainingCentreFactory tcf, ClientFactory cf){
        if((month % 2) == 1) {
            TrainingCentre centre = tcf.makeCentre();
            tdao.insertCentre(centre);
            if(centre.getCentreType().equals("BOOTCAMP")) for(int i = 0; i < 2; i++) tdao.insertCentre(tcf.makeCentre("BOOTCAMP"));
        }

        // for all happy clients that have been waiting for over a year, create a new requirement
        tdao.getClients().stream()
                .filter(c -> (c.getState().equals("HAPPY") && ((month - c.getReqStartMonth()) >= 12)))
                .forEach(c -> tdao.insertRequirement(c))
                .forEach(c -> c.setState("WAITING"))
                .forEach(c -> c.setReqStartMonth(month))
                .forEach(c -> tdao.insertClient(c));


        if((month >= 12) && (rand.nextDouble() < CLIENT_CREATION_CHANCE)) tdao.insertClient(cf.makeClient(month));

        // trainees that have been training for three months become benched
        tdao.getTrainees().stream()
                .filter(t -> ((t.getMonthsTraining() >= 3) && (t.getTrainingState().equals("TRAINING"))))
                .forEach(t -> setTrainingState("BENCH"))
                .forEach(t -> tdao.insertTrainee(t));
        // get benched trainees
        // get waiting clients ordered by wait time
        // for each benched trainee, try to assign to a client, with priority to clients earlier in list
        tdao.getTrainees().stream()
                .filter(t -> (t.getTrainingState().equals("BENCH")))
                .forEach(t -> assignTraineeToReq(t, tdao));
        // set clients with requirements met to "happy"
        tdao.getClients().stream()
                .filter(c -> ((c.getState().equals("WAITING")) && (isRequirementMet(c, tdao))))
                .forEach(c -> c.setState("HAPPY"))
                .forEach(c -> tdao.insertClient(c));

        tdao.setSatisfiedClientsToHappy();
        // set clients that have been waiting for over a year and have not met their requirements to "unhappy"
        // and bench any trainees assigned to their most recent requirement
        Client[] unsatisfiedClients = tdao.getUnsatisfiedClients();
        for(Client c : unsatisfiedClients) {
            tdao.setClientUnhappy(c);
            for(Trainee t : getWorkingTrainees(c)) {
                tdao.setTraineeBenched(t);
            }
        }

        Arrays.stream(tdao.getWaitingTrainees(true)).forEach(t -> tdao.addTrainee(t));
        Arrays.stream(tf.getNewTrainees(month, MIN_GENERATED_TRAINEES, MAX_GENERATED_TRAINEES)).forEach(t -> tdao.addTrainee(t));

        // potentially close centres and redistribute trainees
        Arrays.stream(tdao.getLowAttendanceCentres()).forEach(tc -> tdao.closeCentre(tc));
        tdao.reassignTraineesInClosedCentres();
    }

    private static void assignTraineeToReq(Trainee t, TraineeDAO tdao) {
        Requirement firstValidReq = tdao.getRequirements().stream()
                .filter(r -> r.getReqType().equals(t.getTraineeCourse()))
                .findFirst(r -> (r.getReqQuantity() > r.assignedTrainees()))
                .orElse(null);
        if(firstValidReq != null) {
            t.setReqID(firstValidReq.getReqID());
            t.setTrainingState("WORKING");
            firstValidReq.incrementAssignedTrainees();
            tdao.insertTrainee(t);
            tdao.insertReq(firstValidReq);
        }
    }

    private static boolean isRequirementMet(Client c, TraineeDAO tdao) {
        Requirement currentReq = tdao.getRequirements().stream()
                .filter(r -> (r.getClientID() == c.getClientID()))
                .max(Comparator.comparing(Requirement::getReqID)).orElse(null);
        if(currentReq != null) {
            return (currentReq.getAssignedTrainees() == currentReq.getReqQuantity());
        } else return true; // this should never be possible
    }
}
