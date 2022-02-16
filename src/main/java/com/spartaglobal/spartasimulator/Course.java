package com.spartaglobal.spartasimulator;

import java.util.Random;

public enum Course {
    JAVA("Java"),
    CSHARP("C#"),
    DATA("Data"),
    DEVOPS("DevOps"),
    BUSINESS("Business");

    public final String name;

    private static final Random rand = new Random();

    Course(String name) { this.name = name; }

    public static String getRandomCourse() {
        Course[] courses = Course.values();
        return courses[rand.nextInt(courses.length)].name;
    }

}
