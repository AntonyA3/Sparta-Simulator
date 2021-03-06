package com.spartaglobal.spartasimulator;

import static com.spartaglobal.spartasimulator.Main.logger;

public class RequirementFactory {
    private int nextID;
    private final int STARTING_ID = 0;

    public RequirementFactory() { this.nextID = STARTING_ID; }

    public Requirement makeRequirement(Client c) {
        Requirement r = new Requirement(nextID, c);
        nextID++;
        return r;
    }

}
