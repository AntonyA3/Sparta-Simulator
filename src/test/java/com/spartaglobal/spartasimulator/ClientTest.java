package com.spartaglobal.spartasimulator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {
    @Test
    @DisplayName("Given a client ID, getClientID, should return the correct clientID")
    public void givenAClientID_GetClientID_ShouldReturnCorrectClientID() {
        Client clientTest = new Client(5,"State", "req",2, 20);
        assertEquals(5, clientTest.getClientID());
    }

    @Test
    @DisplayName("Given the amount required by client, getReqQuantity, should return that same amount")
    public void givenRequiredAmountByClient_GetReqQuantity_ShouldReturnSameAmount() {
        Client clientTest = new Client(5,"State", "req",2, 20);
        assertEquals(20, clientTest.getReqQuantity());

    }
}
