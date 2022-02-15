package com.spartaglobal.spartasimulator;

public enum Course {
    JAVA("Java"),
    CSHARP("C#"),
    DATA("Data"),
    DEVOPS("DevOps"),
    BUSINESS("Business");

    public final String name;

    Course(String name) { this.name = name; }

}
