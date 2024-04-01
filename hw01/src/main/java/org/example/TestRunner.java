package org.example;

import org.example.annotations.AfterSuite;
import org.example.annotations.AfterTest;
import org.example.annotations.BeforeSuite;
import org.example.annotations.BeforeTest;
import org.example.annotations.CsvSource;
import org.example.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public final class TestRunner {

    private TestRunner() {
    }

    public static void runTests(Class<?> c) throws InstantiationException,
            IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Object classInstance = c.getDeclaredConstructor().newInstance();

        Optional<Method> beforeSuiteMethod = getMethodWithUniqueAnnotation(c, BeforeSuite.class);
        beforeSuiteMethod.ifPresent(TestRunner::checkMethodShouldBeStatic);
        Optional<Method> afterSuiteMethod = getMethodWithUniqueAnnotation(c, AfterSuite.class);
        afterSuiteMethod.ifPresent(TestRunner::checkMethodShouldBeStatic);
        Optional<Method> beforeTestMethod = getMethodWithUniqueAnnotation(c, BeforeTest.class);
        Optional<Method> afterTestMethod = getMethodWithUniqueAnnotation(c, AfterTest.class);

        List<Method> testMethods = getSortedTestMethods(classInstance);

        invokeIfPresent(beforeSuiteMethod, classInstance);
        for (Method method : testMethods) {
            invokeIfPresent(beforeTestMethod, classInstance);
            invokeTestMethod(method, classInstance);
            invokeIfPresent(afterTestMethod, classInstance);
        }
        invokeIfPresent(afterSuiteMethod, classInstance);
    }

    private static void invokeIfPresent(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Method> method,
                                        Object instance) throws IllegalAccessException, InvocationTargetException {

        if (method.isPresent()) {
            invokeTestMethod(method.get(), instance);
        }
    }

    private static void invokeTestMethod(Method method, Object instance) throws IllegalAccessException, InvocationTargetException {

        Object instanceToInvoke = isNull(instance) && Modifier.isStatic(Modifier.methodModifiers()) ? null : instance;
        if (method.isAnnotationPresent(CsvSource.class)) {
            CsvSource csvSource = method.getAnnotation(CsvSource.class);
            Object[] values = Arrays.stream(csvSource.value().split(","))
                    .map(String::trim)
                    .map(TestRunner::convertStringToParameterValue)
                    .toArray();
            method.invoke(instanceToInvoke, values);
            return;
        }
        method.invoke(instanceToInvoke);
    }

    private static Object convertStringToParameterValue(String value) {
        if (value.matches("\\d+")) {
            return Integer.parseInt(value);
        }
        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            return Boolean.parseBoolean(value);
        }
        return value;
    }

    private static List<Method> getSortedTestMethods(Object obj) {
        Class<?> clazz = obj.getClass();
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Test.class))
                .sorted(Collections.reverseOrder(Comparator.comparingInt(m -> m.getAnnotation(Test.class).priority())))
                .toList();
    }

    private static Optional<Method> getMethodWithUniqueAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        boolean alreadyFound = false;
        Method foundedMethod = null;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                if (alreadyFound) {
                    throw new IllegalArgumentException("Class %s - annotation %s used over more then one method"
                            .formatted(clazz.getSimpleName(), annotation.getSimpleName()));
                } else {
                    foundedMethod = method;
                    alreadyFound = true;
                }
            }
        }
        return Optional.ofNullable(foundedMethod);
    }

    private static void checkMethodShouldBeStatic(Method method) {
        requireNonNull(method);
        if (!Modifier.isStatic(method.getModifiers())) {
            throw new IllegalArgumentException("Class %s - method %s must be static"
                    .formatted(method.getDeclaringClass().getSimpleName(), method.getName()));
        }
    }
}
