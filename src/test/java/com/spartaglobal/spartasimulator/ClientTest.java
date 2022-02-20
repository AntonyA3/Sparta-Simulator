package com.spartaglobal.spartasimulator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    @Test
    @DisplayName("Given a valid client ID, getClientID, should return the correct clientID")
    public void givenAValidClientID_GetClientID_ShouldReturnCorrectClientID() {
        Client clientTest = new Client(5,"State", "req",2, 20);
        assertEquals(5, clientTest.getClientID());
    }

    @Test
    @DisplayName("Given a valid state, get and setState, returns correct values")
    public void givenValidState_getSetState_ReturnsCorrectValues(){
        Client clientTest = new Client(5,"State", "req",2, 20);
        clientTest.setState("AnotherState");
        assertEquals("AnotherState", clientTest.getState());
    }

    @Test
    @DisplayName("Given an invalid state, get and setState, returns null pointer exception")
    public void givenInValidState_getSetState_ReturnsNullPointerException(){
        Client clientTest = new Client(5,"state", "req",2, 20);
        clientTest.setState(null);
        NullPointerException nEx = assertThrows(NullPointerException.class,
                () -> clientTest.getState().equals("state"));
    }

    @Test
    @DisplayName("Given a valid requirement type, getReqType, returns correct values")
    public void givenValidReqType_getReqType_ReturnsCorrectValues(){
        Client clientTest = new Client(5,"State", "req",2, 20);
        assertEquals("req", clientTest.getReqType());
    }

    @Test
    @DisplayName("Given an invalid requirement type, getReqType, returns null pointer exception")
    public void givenInValidReqType_getReqType_ReturnsNullPointerException(){
        Client clientTest = new Client(5,"state", null,2, 20);
        NullPointerException nEx = assertThrows(NullPointerException.class,
                () -> clientTest.getReqType().equals("req"));
    }

    @Test
    @DisplayName("Given a valid starting month, get and setReqStartMonth, returns correct values")
    public void givenValidStartingMonth_getAndSetReqStartMonth_ReturnsCorrectValues(){
        Client clientTest = new Client(5,"State", "req",2, 20);
        clientTest.setReqStartMonth(5);
        assertEquals(5, clientTest.getReqStartMonth());
    }

    @Test
    @DisplayName("Given a valid amount required by client, getReqQuantity, returns correct values")
    public void givenValidReqAmount_getReqQuantity_ReturnsCorrectValues() {
        Client clientTest = new Client(5,"State", "req",2, 20);
        assertEquals(20, clientTest.getReqQuantity());
    }
}
