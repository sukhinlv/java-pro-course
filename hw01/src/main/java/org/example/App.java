package org.example;

import java.lang.reflect.InvocationTargetException;

public class App {

    public static void main(String[] args) throws InvocationTargetException, InstantiationException,
            IllegalAccessException, NoSuchMethodException {

        TestRunner.runTests(TestClassOne.class);
    }
}
