package com.spartaglobal.spartasimulator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CourseTest {
    @Test
    @DisplayName("Given a a valid course name, getRandomCourse, should return a random course")
    public void givenValidCourseName_getRandomCourse_ReturnARandomCourse() {
        boolean isgenerated = false;
        String s = Course.getRandomCourse();
        switch (s) {
            case ("Java"):
                isgenerated = true;
            case ("C#"):
                isgenerated = true;
            case ("Data"):
                isgenerated = true;
            case ("DevOps"):
                isgenerated = true;
            case ("Business"):
                isgenerated = true;
                Assertions.assertEquals(true, isgenerated);

        }
    }
}
