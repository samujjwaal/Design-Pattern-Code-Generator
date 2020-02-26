package com;

import com.behavioral.*;
import com.creational.*;
import com.structural.*;

import java.io.IOException;
import java.util.Scanner;

public class Hw1DesignPatternGenerator extends DesignPatternGenerator {

    @Override
    public void generateDesignPattern() throws IOException {
        Scanner sc = new Scanner(System.in);
        char c;
        do {
            displayDesignPatterns();
            designPatternFactory();

            System.out.print("\nDo you want to try again ?");
            c = sc.next().toLowerCase().charAt(0);
            if(c != 'y') {
                System.out.println("\nExiting . . .");
                System.exit(0);
            }
        }while(c == 'y');
    }

    @Override
    protected void designPatternFactory() {
        Scanner sc = new Scanner(System.in);
        int choice;
        try {
            System.out.print("\n\nEnter your choice of design pattern(1 to 23): ");
            choice = sc.nextInt();
            chooseDesignPattern(choice);
        }
        catch (Exception e){
            System.out.println("Invalid choice! Enter a valid numeric value. Try Again! ");
        }
    }

    @Override
    protected void displayDesignPatterns(){
        System.out.println("The design patterns available are:");

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
    }

    protected void chooseDesignPattern(int choice) throws IOException {
        switch (choice) {
            case 1 -> new Singleton();
            case 2 -> new AbstractFactory();
            case 3 -> new Builder();
            case 4 -> new FactoryMethod();
            case 5 -> new Prototype();
            case 6 -> new Adapter();
            case 7 -> new Bridge();
            case 8 -> new Composite();
            case 9 -> new Decorator();
            case 10 -> System.out.println("Facade");
            case 11 -> new Flyweight();
            case 12 -> new Proxy();
            case 13 -> new ChainOfResponsibility();
            case 14 -> new Command();
            case 15 -> new Interpreter();
            case 16 -> new Iterator();
            case 17 -> new Mediator();
            case 18 -> new Memento();
            case 19 -> new Observer();
            case 20 -> new State();
            case 21 -> new Strategy();
            case 22 -> new Visitor();
            case 23 -> new TemplateMethod();
            default -> System.out.println("Invalid Choice! Input a choice from 1 to 23. ");
        }

    }
}
