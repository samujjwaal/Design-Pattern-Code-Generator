package com;

import com.squareup.javapoet.JavaFile;
import com.structural.Flyweight;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class DesignPatternTest {
    String[] classNames = {"Flyweight", "ConcreteFlyweight","UnsharedConcreteFlyweight","FlyweightFactory"};
    String packageName = "com.test_generateCode.flyweight";

    @Test
    void test_generateCode() throws IOException {

        // Test Case to verify  generateCode() returns JavaFile[] object

        assertEquals(JavaFile[].class,new Flyweight(0).generateCode(classNames, packageName).getClass());
    }

    @Test
    void test_writeJavaFiles() throws IOException {

        // Test Case to verify the generated java files are written successfully in the output folder
        int mode = 0;
        Flyweight test = new Flyweight(mode);
        JavaFile[] test_files = test.generateCode(classNames, packageName);
        test.writeJavaFiles(test_files);
        Path path = Paths.get("outputs/com/test_generateCode/flyweight");
        assertTrue(Files.exists(path));


    }

}