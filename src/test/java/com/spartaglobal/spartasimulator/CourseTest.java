package com.spartaglobal.spartasimulator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {

    @Test
    @DisplayName("Given a a valid course name, getRandomCourse, should return a random course")
    public void givenValidCourseName_getRandomCourse_ReturnARandomCourse() {
        boolean isGenerated = false;
        String s = Course.getRandomCourse();
        switch (s) {
            case ("Java"):
                isGenerated = true;
            case ("C#"):
                isGenerated = true;
            case ("Data"):
                isGenerated = true;
            case ("DevOps"):
                isGenerated = true;
            case ("Business"):
                isGenerated = true;
                assertTrue(isGenerated);
        }
    }
}
