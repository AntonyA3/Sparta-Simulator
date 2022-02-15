package com.spartaglobal.spartasimulator;


import org.apache.logging.log4j.Logger;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static Logger logger;
    public static void main(String[] args) {
        boolean appShouldContinue = true;
        TraineeDAO tDAO = new TraineeDAO();
        tDAO.openConnection();

        do {
            Integer months = null;
            while (months == null) {
                System.out.println("Please Enter sim length in months\n\t<number>");
                try {
                    months = Integer.valueOf(readInt());

                } catch (InputMismatchException e) {
                    System.out.println("This is not a valid input");
                }finally {
                    if(months <= 0){
                        months = null;
                        System.out.println("You must enter a month that is greater than 0");
                    }
                }
            }
            Simulation simulation = new Simulation();
            System.out.println(String.format("Run the System for %d months", months.intValue()));
            simulation.simulate(months.intValue(), tDAO);
            System.out.println("Simulation complete");

            System.out.println("continue: yes or no:\n" +
                    "\tyes\tno |default option is close");

            switch (readString().toLowerCase()){
                case "yes", "y" ->  appShouldContinue = true;
                case "no", "n" -> appShouldContinue = false;
                default -> appShouldContinue = true;
            }

        }while (appShouldContinue);
        System.out.println("Goodbye");
    }

    public static int readInt() throws InputMismatchException {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    public static String readString(){
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }
}
