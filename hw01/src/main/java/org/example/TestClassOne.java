package org.example;

import org.example.annotations.AfterSuite;
import org.example.annotations.AfterTest;
import org.example.annotations.BeforeSuite;
import org.example.annotations.BeforeTest;
import org.example.annotations.CsvSource;
import org.example.annotations.Test;

public class TestClassOne {

    @BeforeSuite
    public static void beforeSuite() {
        System.out.println("beforeSuite");
    }

    @AfterSuite
    public static void afterSuit() {
        System.out.println("afterSuit");
    }

    @BeforeTest
    public void beforeTest() {
        System.out.println("beforeTest");
    }

    @AfterTest
    public void afterTest() {
        System.out.println("afterTest");
    }

    @Test
    public void testDefault() {
        System.out.println("Test, priority = default");
    }

    @Test(priority = 1)
    public void testOne() {
        System.out.println("Test, priority = 1");
    }

    @Test(priority = 6)
    public void testSix() {
        System.out.println("Test, priority = 6");
    }

    @Test(priority = 2)
    public void testTwo() {
        System.out.println("Test, priority = 2");
    }

    @Test
    @CsvSource("10, Java, 20, true")
    public void testSourceAnnotation(int a, String b, int c, boolean d) {
        System.out.printf("CsvSource: %s - %s - %s - %s%n", a, b, c, d);
    }
}
