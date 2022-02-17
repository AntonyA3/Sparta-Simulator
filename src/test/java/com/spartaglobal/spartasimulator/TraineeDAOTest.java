package com.spartaglobal.spartasimulator;

import org.junit.jupiter.api.*;

public class TraineeDAOTest {

    private static TraineeDAO tDAO;

    @BeforeAll
    public static void setUp(){
        tDAO = new TraineeDAO();
        tDAO.openConnection();
    }

    @AfterAll
    public static void close(){
        tDAO.closeConnection();
    }

}
