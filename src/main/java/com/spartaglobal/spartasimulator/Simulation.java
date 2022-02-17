package com.spartaglobal.spartasimulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;

public class Simulation {

    private static final int MIN_GENERATED_TRAINEES = 50;
    private static final int MAX_GENERATED_TRAINEES = 100;
    private static final int CENTRE_ATTENDANCE_THRESHOLD = 25;
    private static final int MAX_BOOT_CAMPS = 2;
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
        TrainingCentre newCentre;
        if((month % 2) == 1) {
            do {
                newCentre = tcf.makeCentre();
            } while((!newCentre.getCentreType().equals("BOOTCAMP")) || (!maxBootCampsExist(tdao))); //Infinite Loop
            tdao.insertCentre(newCentre);
            if(newCentre.getCentreType().equals("TRAININGHUB")) for(int i = 0; i < 2; i++) tdao.insertCentre(tcf.makeCentre("TRAININGHUB"));
        }
        System.out.println("Created new centre");
        // for all happy clients that have been waiting for over a year, create a new requirement
        tdao.getClients().stream()
                .filter(c -> (c.getState().equals("HAPPY") && ((month - c.getReqStartMonth()) >= 12)))
                .forEach(c -> {
                    tdao.insertRequirement(new Requirement(c));
                    c.setState("WAITING");
                    c.setReqStartMonth(month);
                    tdao.insertClient(c);
                });


        if((month >= 12) && (rand.nextDouble() < CLIENT_CREATION_CHANCE)) tdao.insertClient(cf.makeClient(month));

        // trainees that have been training for three months become benched
        tdao.getTrainees().stream()
                .filter(t -> ((t.getMonthsTraining() >= 3) && (t.getTrainingState().equals("TRAINING"))))
                .forEach(t -> {
                    t.setTrainingState("BENCH");
                    tdao.insertTrainee(t);
                });

        // get benched trainees
        // get waiting clients ordered by wait time
        // for each benched trainee, try to assign to a client, with priority to clients earlier in list
        tdao.getTrainees().stream()
                .filter(t -> (t.getTrainingState().equals("BENCH")))
                .forEach(t -> assignTraineeToReq(t, tdao));

        // set clients with requirements met to "happy"
        tdao.getClients().stream()
                .filter(c -> ((c.getState().equals("WAITING")) && (isRequirementMet(c, tdao))))
                .forEach(c -> {
                    c.setState("HAPPY");
                    tdao.insertClient(c);
                });

        // set clients that have been waiting for over a year and have not met their requirements to "unhappy"
        // and bench any trainees assigned to their most recent requirement
        tdao.getClients().stream()
                .filter(c -> ((c.getState().equals("WAITING")) && (!isRequirementMet(c, tdao))))
                .forEach(c -> {
                    c.setState("UNHAPPY");
                    unassignTraineesFromReq(c, tdao);
                });

        tdao.getTrainees().stream()
                .filter(t -> t.getTrainingState().equals("WAITING"))
                .forEach(t -> assignTraineeToCentre(t, tdao));


        Arrays.stream(tf.getNewTrainees(month, MIN_GENERATED_TRAINEES, MAX_GENERATED_TRAINEES))
                .forEach(t -> assignTraineeToCentre(t, tdao));

        // potentially close centres and redistribute trainees

        tdao.getCentres().stream()
                .filter(c -> c.getIsOpen())
                .filter(c -> isCentreLowAttendance(c, tdao))
                .forEach(c -> closeCentreOrIncrementMonths(c, tdao));

        tdao.getCentres().stream()
                .filter(c -> (c instanceof BootCamp))
                .filter(c -> c.getIsOpen())
                .filter(c -> !isCentreLowAttendance(c, tdao))
                .forEach(c -> {
                    ((BootCamp) c).setMonthsBelowThreshold(0);
                    tdao.insertCentre(c);
                });

        tdao.getTrainees().stream()
                .filter(t -> t.getTrainingState().equals("TRAINING"))
                .filter(t -> inClosedCentre(t, tdao))
                .forEach(t -> assignTraineeToCentre(t, tdao));

        tdao.getTrainees().stream()
                .filter(t -> t.getTrainingState().equals("TRAINING"))
                .forEach(t -> t.incrementMonthsTraining());

    }

    private static boolean maxBootCampsExist(TraineeDAO tdao) {
        return (tdao.getCentres().stream()
                .filter(c -> (c instanceof BootCamp))
                .filter(c -> (c.getIsOpen()))
                .count() >= MAX_BOOT_CAMPS);
    }

    private static void assignTraineeToReq(Trainee t, TraineeDAO tdao) {
        Requirement firstValidReq = tdao.getRequirements().stream()
                .filter(r -> r.getReqType().equals(t.getTraineeCourse()))
                .filter(r -> (r.getReqQuantity() > r.getAssignedTrainees()))
                .findFirst()
                .orElse(null);
        if(firstValidReq != null) {
            t.setReqID(firstValidReq.getReqID());
            t.setTrainingState("WORKING");
            firstValidReq.incrementAssignedTrainees();
            tdao.insertTrainee(t);
            tdao.insertRequirement(firstValidReq);

        }
    }

    private static Requirement getCurrentReq(Client c, TraineeDAO tdao) {
        return tdao.getRequirements().stream()
                .filter(r -> (r.getClientID() == c.getClientID()))
                .max(Comparator.comparing(Requirement::getReqID)).orElse(null);
    }

    private static boolean isRequirementMet(Client c, TraineeDAO tdao) {
        Requirement currentReq = getCurrentReq(c, tdao);
        if(currentReq != null) {
            return (currentReq.getAssignedTrainees() == currentReq.getReqQuantity());
        } else return true; // this should never be possible
    }

    private static void unassignTraineesFromReq(Client c, TraineeDAO tdao) {
        int currentReqID = getCurrentReq(c, tdao).getReqID();
        tdao.getTrainees().stream()
                .filter(t -> (t.getReqID() == currentReqID))
                .forEach(t -> {
                    t.setReqID(null);
                    tdao.insertTrainee(t);
                });
    }

    private static void assignTraineeToCentre(Trainee t, TraineeDAO tdao) {
        ArrayList<TrainingCentre> nonFullCentres = new ArrayList<>(tdao.getCentres().stream()
                .filter(c -> !isCentreFull(c, tdao))
                .collect(Collectors.toList()));
        for(TrainingCentre c : nonFullCentres) {
            if(c instanceof TechCentre) {
                if(((TechCentre) c).getCourse().equals(t.getTraineeCourse())) {
                    t.setCentreID(c.getTrainingCentreID());
                    t.setTrainingState("TRAINING");
                    tdao.insertTrainee(t);
                    return;
                }
            } else {
                t.setCentreID(c.getTrainingCentreID());
                t.setTrainingState("TRAINING");
                tdao.insertTrainee(t);
                return;
            }
        }
    }

    private static boolean isCentreFull(TrainingCentre tc, TraineeDAO tdao) {
        return (tdao.getTrainees().stream()
                .filter(t -> t.getCentreID().equals(tc.getTrainingCentreID()))
                .count() >= tc.getTrainingCentreCapacity()); // should never be greater than
    }

    private static boolean isCentreLowAttendance(TrainingCentre tc, TraineeDAO tdao) {
        return (tdao.getTrainees().stream()
                .filter(t -> t.getCentreID().equals(tc.getTrainingCentreID()))
                .count() < CENTRE_ATTENDANCE_THRESHOLD);
    }

    private static void closeCentreOrIncrementMonths(TrainingCentre tc, TraineeDAO tdao) {
        if(tc instanceof BootCamp) {
            if(((BootCamp) tc).getMonthsBelowThreshold() > 2) tc.setIsOpen(false);
            else ((BootCamp) tc).setMonthsBelowThreshold(((BootCamp) tc).getMonthsBelowThreshold() + 1);
        } else tc.setIsOpen(false);
        tdao.insertCentre(tc);
    }

    private static boolean inClosedCentre(Trainee t, TraineeDAO tdao) {
        TrainingCentre tc = tdao.getCentres().stream()
                .filter(c -> (c.getTrainingCentreID() == t.getCentreID()))
                .findFirst()
                .orElse(null);
        if((tc == null) || (tc.getIsOpen())) return false; //else
        return true;
    }
}
