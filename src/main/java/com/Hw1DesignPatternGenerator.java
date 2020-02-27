package com;

import com.behavioral.*;
import com.creational.*;
import com.structural.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Hw1DesignPatternGenerator extends DesignPatternGenerator {

    //Define a static logger variable so that it references the Logger instance
    private static final Logger logger = LoggerFactory.getLogger(Hw1DesignPatternGenerator.class);

    @Override
    public void generateDesignPattern() {
        logger.info("Entering generateDesignPattern()");
        Scanner sc = new Scanner(System.in);
        char c;
        do {
            displayDesignPatterns();
            designPatternFactory();

            System.out.print("\nDo you want to try again ?");
            c = sc.next().toLowerCase().charAt(0);
            if(c != 'y') {
                System.out.println("\nExiting . . .");
            }
        }while(c == 'y');
    }

    @Override
    protected void designPatternFactory() {
        logger.info("Executing designPatternFactory()");

        Scanner sc = new Scanner(System.in);
        int choice;
        try {
            System.out.print("\nEnter your choice of design pattern(1 to 23): ");
            choice = sc.nextInt();
            logger.info("Passing {} to chooseDesignPattern()",choice);
            chooseDesignPattern(choice);
        }
        catch (Exception e){
            logger.error("Exception {} encountered ", InputMismatchException.class.getSimpleName());
            System.out.println("Invalid choice! Enter a valid numeric value. Try Again! ");
        }
    }

    @Override
    protected void displayDesignPatterns(){
        logger.info("Entering displayDesignPatterns()");

        System.out.println("\nThe design patterns available are:");

        for (int i = 0; i < 7; i++) {
            System.out.print((i+1)+"-" + designPatterns[i] + "\t\t");
        }
        System.out.println("\n");
        for (int i = 7; i < 14; i++) {
            System.out.print((i+1)+"-" + designPatterns[i] + "\t\t");
        }
        System.out.println("\n");
        for (int i = 15; i < 23; i++) {
            System.out.print((i+1)+"-" + designPatterns[i] + "\t\t");
        }
        System.out.println("\n");
    }

    protected void chooseDesignPattern(int choice) throws IOException {
        logger.info("Entering chooseDesignPattern()");
        switch (choice) {
            case 1 : new Singleton(); break;
            case 2 : new AbstractFactory(); break;
            case 3 : new Builder(); break;
            case 4 : new FactoryMethod(); break;
            case 5 : new Prototype(); break;
            case 6 : new Adapter(); break;
            case 7 : new Bridge(); break;
            case 8 : new Composite(); break;
            case 9 : new Decorator(); break;
            case 10 : System.out.println("Facade"); break;
            case 11 : new Flyweight(); break;
            case 12 : new Proxy(); break;
            case 13 : new ChainOfResponsibility(); break;
            case 14 : new Command(); break;
            case 15 : new Interpreter(); break;
            case 16 : new Iterator(); break;
            case 17 : new Mediator(); break;
            case 18 : new Memento(); break;
            case 19 : new Observer(); break;
            case 20 : new State(); break;
            case 21 : new Strategy(); break;
            case 22 : new Visitor(); break;
            case 23 : new TemplateMethod(); break;
            default : System.out.println("Invalid Choice! Input a choice from 1 to 23. ");

            logger.info("Exiting chooseDesignPattern()");

        }

    }
}
