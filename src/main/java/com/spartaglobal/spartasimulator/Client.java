package com.spartaglobal.spartasimulator;

public class Client {
    private int clientID;
    private int reqQuantity;
    private String reqType;

    public Client(int clientID, int reqQuantity, String reqType) {
        this.clientID = clientID;
        this.reqQuantity = reqQuantity;
        this.reqType = reqType;
    }

    public int getClientID() { return clientID; }
    public int getReqQuantity() { return reqQuantity; }
    private String getReqType() { return reqType; }


}
