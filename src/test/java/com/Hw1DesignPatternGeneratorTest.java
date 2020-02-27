package com;

import com.structural.Flyweight;
import org.junit.jupiter.api.Test;

import java.io.IOException;


import static org.junit.jupiter.api.Assertions.*;

class Hw1DesignPatternGeneratorTest {


    @Test
    void test_getInstance() {

        // Test Case to verify only 1 instance of Hw1DesignPatternGenerator is created each time

        Hw1DesignPatternGenerator firstInstance = Hw1DesignPatternGenerator.getInstance();
        Hw1DesignPatternGenerator secondInstance = Hw1DesignPatternGenerator.getInstance();

        assertEquals(firstInstance,secondInstance);
    }


    @Test
    void chooseDesignPattern() throws IOException {

        //Test Case to verify correct design pattern file is executed when user inputs the choice of design pattern
        // Here Flyweight( 11 ) is selected.

        DesignPattern design_pattern2 = Hw1DesignPatternGenerator.getInstance().chooseDesignPattern(11,0);
        assertEquals(Flyweight.class,design_pattern2.getClass());

        // Test Case to check null is returned on selecting incorrect design pattern choice
        int choice = 75;
        DesignPattern design_pattern = Hw1DesignPatternGenerator.getInstance().chooseDesignPattern(choice,0);
        assertNull(design_pattern);

    }
}