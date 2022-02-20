package com.spartaglobal.spartasimulator;

public class Requirement {
    private int reqID;
    private int clientID;
    private String reqType;
    private int reqStartMonth;
    private int reqQuantity;
    private int assignedTrainees;

    public Requirement(int reqID, int clientID, String reqType, int reqStartMonth, int reqQuantity, int assignedTrainees) {
        this.reqID = reqID;
        this.clientID = clientID;
        this.reqType = reqType;
        this.reqStartMonth = reqStartMonth;
        this.reqQuantity = reqQuantity;
        this.assignedTrainees = assignedTrainees;
    }

    public Requirement(int reqID, Client c) {
        this.reqID = reqID;
        this.clientID = c.getClientID();
        this.reqType = c.getReqType();
        this.reqStartMonth = c.getReqStartMonth();
        this.reqQuantity = c.getReqQuantity();
        this.assignedTrainees = 0;
    }

    public int getReqID() { return reqID; }
    public int getClientID() { return clientID; }
    public String getReqType() { return reqType; }
    public int getReqStartMonth() { return reqStartMonth; }
    public int getReqQuantity() { return reqQuantity; }
    public int getAssignedTrainees() { return assignedTrainees; }

    public void incrementAssignedTrainees() { assignedTrainees++; }

}
