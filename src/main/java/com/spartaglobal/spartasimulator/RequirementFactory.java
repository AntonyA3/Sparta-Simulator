package com.spartaglobal.spartasimulator;

public class RequirementFactory {
    private int nextID;
    private final int STARTING_ID = 0;

    public RequirementFactory() { this.nextID = STARTING_ID; }

    public Requirement makeRequirement(Client c) { return (new Requirement(nextID, c)); }

}
