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

    /**
     * Carries out the simulation.
     * @param months How many months the simulation should run for.
     * @param infoGivenMonthly Whether the simulation should print information monthly (true) or after the entire simulation (false)
     * @param tdao The object through which the simulation accesses the database.
     */
    public static void simulate(int months, boolean infoGivenMonthly, TraineeDAO tdao){
        TraineeFactory tf = new TraineeFactory();
        TrainingCentreFactory tcf = new TrainingCentreFactory();
        ClientFactory cf = new ClientFactory();
        RequirementFactory rf = new RequirementFactory();
        for(int i = 0; i < months; i++) {
            loop(i, tdao, tf, tcf, cf, rf);
            if(infoGivenMonthly) DisplayManager.printSystemInfo(tdao, i);
            System.gc();
        }
        if(!infoGivenMonthly) DisplayManager.printSystemInfo(tdao, months);
    }

    /**
     * Carries out one loop of the simulation.
     * @param month Which month the simulation is on.
     * @param tdao The object through which the simulation accesses the database.
     * @param tf Factory object for creating new trainees.
     * @param tcf Factory object for creating new training centres.
     * @param cf Factory object for creating new clients.
     * @param rf Factory object for creating new requirements.
     */
    private static void loop(int month, TraineeDAO tdao, TraineeFactory tf, TrainingCentreFactory tcf, ClientFactory cf, RequirementFactory rf){
        TrainingCentre newCentre;
        if((month % 2) == 1) {
            do {
                newCentre = tcf.makeCentre();
            } while((newCentre.getCentreType().equals("BOOTCAMP")) && (maxBootCampsExist(tdao)));
            tdao.insertCentre(newCentre);
            if(newCentre.getCentreType().equals("TRAININGHUB")) for(int i = 0; i < 2; i++) tdao.insertCentre(tcf.makeCentre("TRAININGHUB"));
        }

        // for all happy clients that have been waiting for over a year, create a new requirement
        tdao.getClients().stream()
                .filter(c -> (c.getState().equals("HAPPY") && ((month - c.getReqStartMonth()) >= 12)))
                .forEach(c -> {
                    tdao.insertRequirement(rf.makeRequirement(c));
                    c.setState("WAITING");
                    c.setReqStartMonth(month);
                    tdao.insertClient(c);
                });


        if((month >= 12) && (rand.nextDouble() < CLIENT_CREATION_CHANCE)) tdao.insertClient(cf.makeClient(month));

        // trainees that have been training for three months become benched
        tdao.getTrainees("WHERE months_training >= 3 AND training_state = 'TRAINING'").stream()
                //.filter(t -> ((t.getMonthsTraining() >= 3) && (t.getTrainingState().equals("TRAINING"))))
                .forEach(t -> {
                    t.setTrainingState("BENCH");
                    tdao.insertTrainee(t);
                });
        // get benched trainees
        // get waiting clients ordered by wait time
        // for each benched trainee, try to assign to a client, with priority to clients earlier in list
        tdao.getTrainees("WHERE training_state = 'BENCH'").stream()
                //.filter(t -> (t.getTrainingState().equals("BENCH")))
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

        tdao.getTrainees("WHERE training_state = 'WAITING'").stream()
                //.filter(t -> t.getTrainingState().equals("WAITING"))
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

        tdao.getTrainees("WHERE training_state = 'TRAINING'").stream()
                //.filter(t -> t.getTrainingState().equals("TRAINING"))
                .filter(t -> inClosedCentre(t, tdao))
                .forEach(t -> assignTraineeToCentre(t, tdao));

        tdao.getTrainees("WHERE training_state = 'TRAINING'").stream()
                //.filter(t -> t.getTrainingState().equals("TRAINING"))
                .forEach(t -> t.incrementMonthsTraining());
    }

    /**
     * Checks if the maximum number of boot camps already exists in the simulation.
     * @param tdao The object through which the simulation accesses the database.
     * @return true if the maximum number of boot camps already exists.
     */
    private static boolean maxBootCampsExist(TraineeDAO tdao) {
        return (tdao.getCentres().stream()
                .filter(c -> (c instanceof BootCamp))
                .filter(c -> (c.getIsOpen()))
                .count() >= MAX_BOOT_CAMPS);
    }

    /**
     * Assigns a given trainee to a valid requirement.
     * @param t The trainee to be assigned.
     * @param tdao The object through which the simulation accesses the database.
     */
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

    /**
     * Retrieves the given client's current requirement.
     * @param c The client for which to find the current requirement.
     * @param tdao The object through which the simulation accesses the database.
     * @return The client's current requirement.
     */
    private static Requirement getCurrentReq(Client c, TraineeDAO tdao) {
        return tdao.getRequirements().stream()
                .filter(r -> (r.getClientID() == c.getClientID()))
                .max(Comparator.comparing(Requirement::getReqID)).orElse(null);
    }

    /**
     * Checks if the client's current requirement has been met.
     * @param c The client for which to check the requirement of.
     * @param tdao The object through which the simulation accesses the database.
     * @return Whether the client's requirement has been met (true) or not (false)
     */
    private static boolean isRequirementMet(Client c, TraineeDAO tdao) {
        Requirement currentReq = getCurrentReq(c, tdao);
        if(currentReq != null) {
            return (currentReq.getAssignedTrainees() == currentReq.getReqQuantity());
        } else return true; // this should never be possible
    }

    /**
     * Unassigns all trainees assign to the given client's current requirement.
     * @param c The client for which to retrieve the current requirement of.
     * @param tdao The object through which the simulation accesses the database.
     */
    private static void unassignTraineesFromReq(Client c, TraineeDAO tdao) {
        int currentReqID = getCurrentReq(c, tdao).getReqID();
        tdao.getTrainees(String.format("WHERE req_id = %d", currentReqID)).stream()
                //.filter(t -> (t.getReqID() == currentReqID))
                .forEach(t -> {
                    t.setReqID(null);
                    tdao.insertTrainee(t);
                });
    }

    /**
     * Assigns the given trainee to the first valid training centre.
     * @param t The trainee to assign to a training centre.
     * @param tdao The object through which the simulation accesses the database.
     */
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

    /**
     * Checks whether or not the given training centre is full.
     * @param tc The training centre for which to check the fullness of.
     * @param tdao The object through which the simulation accesses the database.
     * @return Whether the training centre is full (true) or not (false).
     */
    private static boolean isCentreFull(TrainingCentre tc, TraineeDAO tdao) {
        return (tdao.getTrainees(String.format("WHERE centre_id = %d", tc.getTrainingCentreID())).stream()
                //.filter(t -> t.getCentreID().equals(tc.getTrainingCentreID()))
                .count() >= tc.getTrainingCentreCapacity()); // should never be greater than
    }

    /**
     * Checks whether or not the given training centre is low attendance.
     * @param tc The training centre for which to check the attendance of.
     * @param tdao The object through which the simulation accesses the database.
     * @return Whether the training centre is low attendance (true) or not (false).
     */
    private static boolean isCentreLowAttendance(TrainingCentre tc, TraineeDAO tdao) {
        return (tdao.getTrainees(String.format("WHERE centre_id = %d", tc.getTrainingCentreID())).stream()
                //.filter(t -> t.getCentreID().equals(tc.getTrainingCentreID()))
                .count() < CENTRE_ATTENDANCE_THRESHOLD);
    }

    /**
     * If the given training centre is a boot camp, checks how long it has been low-attendance,
     * closing it if it's the third consecutive month of low attendance or incrementing the count for it otherwise.
     * If the given training centre is not a boot camp, it closes it.
     * @param tc The training centre to possibly close.
     * @param tdao The object through which the simulation accesses the database.
     */
    private static void closeCentreOrIncrementMonths(TrainingCentre tc, TraineeDAO tdao) {
        if(tc instanceof BootCamp) {
            if(((BootCamp) tc).getMonthsBelowThreshold() > 2) tc.setIsOpen(false);
            else ((BootCamp) tc).setMonthsBelowThreshold(((BootCamp) tc).getMonthsBelowThreshold() + 1);
        } else tc.setIsOpen(false);
        tdao.insertCentre(tc);
    }

    /**
     * Checks whether the given trainee is in a closed centre or not.
     * @param t The trainee to check the centre status of.
     * @param tdao The object through which the simulation accesses the database.
     * @return Whether the trainee is in a closed centre (true) or not (false).
     */
    private static boolean inClosedCentre(Trainee t, TraineeDAO tdao) {
        TrainingCentre tc = tdao.getCentres().stream()
                .filter(c -> (c.getTrainingCentreID() == t.getCentreID()))
                .findFirst()
                .orElse(null);
        if((tc == null) || (tc.getIsOpen())) return false; //else
        return true;
    }
}
