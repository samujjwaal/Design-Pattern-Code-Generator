package com;

import com.behavioral.*;
import com.creational.*;
import com.structural.*;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Hw1DesignPatternGenerator extends DesignPatternGenerator {

    //Define a static logger variable so that it references the Logger instance
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Hw1DesignPatternGenerator.class);

    private static Hw1DesignPatternGenerator instance;

    private Hw1DesignPatternGenerator(){
        logger.info("Executing constructor Hw1DesignPatternGenerator()");
    }

    public static Hw1DesignPatternGenerator getInstance(){
        logger.info("Creating instance of {}",Hw1DesignPatternGenerator.class.getSimpleName());

        if (instance == null) {
            instance = new Hw1DesignPatternGenerator();
        }
        return instance;
    }

    @Override
    protected void generateDesignPattern() throws IOException {
        logger.info("Entering generateDesignPattern()");
        Scanner sc = new Scanner(System.in);
        char c;
        do {
            displayDesignPatterns();
            designPatternFactory();

            System.out.print("\nDo you want to try again ? ");
            c = sc.next().toLowerCase().charAt(0);
            if(c != 'y') {
                System.out.println("\nExiting . . .");
            }
        }while(c == 'y');
    }

    @Override
    protected void designPatternFactory() throws IOException {
        logger.info("Executing designPatternFactory()");
        int mode = 1;

        Scanner sc = new Scanner(System.in);
        int choice = 0 ;
        try {
            System.out.print("\nEnter your choice of design pattern(1 to 23): ");
            choice = sc.nextInt();
        }
        catch (InputMismatchException e){
            logger.error("Exception {} encountered ", InputMismatchException.class.getSimpleName());
//            System.out.println(e);
            System.out.println("\nInvalid choice! Enter a valid numeric value. Try Again! \n");
        }
        logger.info("Passing {} to chooseDesignPattern()",choice);

        chooseDesignPattern(choice, mode);
    }

    @Override
    protected void displayDesignPatterns(){
        logger.info("Entering displayDesignPatterns()");

        System.out.println("\nThe design patterns available are: ");

        for (int i = 0; i < 7; i++) {
            System.out.print((i+1)+"-" + designPatterns[i] + "\t   ");
        }
        System.out.println("\n");
        for (int i = 7; i < 14; i++) {
            System.out.print((i+1)+"-" + designPatterns[i] + "\t   ");
        }
        System.out.println("\n");
        for (int i = 15; i < 23; i++) {
            System.out.print((i+1)+"-" + designPatterns[i] + "\t   ");
        }
        System.out.println("\n");
    }

    protected DesignPattern chooseDesignPattern(int choice, int mode) throws IOException {
        logger.info("Entering chooseDesignPattern()");
        DesignPattern dp = null;
        switch (choice) {
            case 1 : dp = new Singleton(mode); break;
            case 2 : dp = new AbstractFactory(mode); break;
            case 3 : dp = new Builder(mode); break;
            case 4 : dp = new FactoryMethod(mode); break;
            case 5 : dp = new Prototype(mode); break;
            case 6 : dp = new Adapter(mode); break;
            case 7 : dp = new Bridge(mode); break;
            case 8 : dp = new Composite(mode); break;
            case 9 : dp = new Decorator(mode); break;
            case 10 : System.out.println("Facade"); break;
            case 11 : dp = new Flyweight(mode); break;
            case 12 : dp = new Proxy(mode); break;
            case 13 : dp = new ChainOfResponsibility(mode); break;
            case 14 : dp = new Command(mode); break;
            case 15 : dp = new Interpreter(mode); break;
            case 16 : dp = new Iterator(mode); break;
            case 17 : dp = new Mediator(mode); break;
            case 18 : dp = new Memento(mode); break;
            case 19 : dp = new Observer(mode); break;
            case 20 : dp = new State(mode); break;
            case 21 : dp = new Strategy(mode); break;
            case 22 : dp = new Visitor(mode); break;
            case 23 : dp = new TemplateMethod(mode); break;
            default : System.out.println("\nInvalid Choice! Input a choice from 1 to 23. Try again! \n");

            logger.info("Exiting chooseDesignPattern()");
        }
        return dp;

    }
}
