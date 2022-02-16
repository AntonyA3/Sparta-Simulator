package com.spartaglobal.spartasimulator;

public class Client {
    private int clientID;
    private String state;
    private String reqType;
    private Integer reqStartMonth;
    private Integer reqQuantity;

    public Client(int clientID, String state, String reqType, Integer reqStartMonth, Integer reqQuantity) {
        this.clientID = clientID;
        this.state = state;
        this.reqType = reqType;
        this.reqStartMonth = reqStartMonth;
        this.reqQuantity = reqQuantity;
    }

    public int getClientID() { return clientID; }
    public String getState() { return state; }
    public String getReqType() { return reqType; }
    public int getReqStartMonth() { return reqStartMonth; }
    public int getReqQuantity() { return reqQuantity; }

    public void setState(String state) { this.state = state; }
    public void setReqStartMonth(Integer reqStartMonth) { this.reqStartMonth = reqStartMonth; }


}
