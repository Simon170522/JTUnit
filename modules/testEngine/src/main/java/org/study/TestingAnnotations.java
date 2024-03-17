package org.study;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Test {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Before {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface After {
}
public class TestingAnnotations {
    public void runTests(Class<?> testClass) {
        int testsCount = 0;
        int passedTestsCount = 0;

        java.lang.reflect.Method[] methods = testClass.getDeclaredMethods();

        for (java.lang.reflect.Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {
                testsCount++;

                try {
                    Object testObject = testClass.getDeclaredConstructor().newInstance();

                    invokeBeforeMethods(testObject, methods);

                    method.invoke(testObject);

                    passedTestsCount++;

                    invokeAfterMethods(testObject, methods);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Total tests: " + testsCount);
        System.out.println("Passed tests: " + passedTestsCount);
        System.out.println("Failed tests: " + (testsCount - passedTestsCount));
    }

    private void invokeBeforeMethods(Object testObject, java.lang.reflect.Method[] methods) throws Exception {
        for (java.lang.reflect.Method method : methods) {
            if (method.isAnnotationPresent(Before.class)) {
                method.invoke(testObject);
            }
        }
    }

    private void invokeAfterMethods(Object testObject, java.lang.reflect.Method[] methods) throws Exception {
        for (java.lang.reflect.Method method : methods) {
            if (method.isAnnotationPresent(After.class)) {
                method.invoke(testObject);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java -cp JTUnit.jar cwt.TestRunner <testClass>");
            return;
        }

        String testClassName = args[0];

        try {
            Class<?> testClass = Class.forName(testClassName);
            TestingAnnotations testFramework = new TestingAnnotations();
            testFramework.runTests(testClass);
        } catch (ClassNotFoundException e) {
            System.out.println("Test class not found: " + testClassName);
        }
    }

}
class MyTestClass {

    @Before
    public void setUp() {
        // Метод, который будет выполнен перед каждым тестом
        System.out.println("Setting up test environment...");
    }

    @Test
    public void test1() {
        // Тестовый метод
        System.out.println("Executing Test 1...");
    }

    @Test
    public void test2() {
        // Тестовый метод
        System.out.println("Executing Test 2...");
    }

    @After
    public void tearDown() {
        // Метод, который будет выполнен после каждого теста
        System.out.println("Tearing down test environment...");
    }
}
