package com.spartaglobal.spartasimulator;

import java.util.Random;

public class ClientFactory {
    private int nextID;
    private final int STARTING_ID = 0;
    private final int MIN_REQ_QUANTITY = 15;
    private final int MAX_REQ_QUANTITY = 50;
    private final Random rand = new Random();

    public ClientFactory() { nextID = STARTING_ID; }

    public Client makeClient(int reqStartMonth) {
        Client client = new Client(nextID, "WAITING", Course.getRandomCourse(), reqStartMonth, rand.nextInt(MIN_REQ_QUANTITY, (MAX_REQ_QUANTITY + 1)));
        nextID++;
        return client;
    }
}
