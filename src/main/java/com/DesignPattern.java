package com;

import com.squareup.javapoet.JavaFile;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public interface DesignPattern {

    //Define a static logger variable so that it references the Logger instance
    Logger logger = (Logger) LoggerFactory.getLogger(DesignPattern.class);

    // to display all available design pattern that can be generated
    default void displayClassNames(String[] classes){

        for (String name: classes) {
        System.out.print(name + " \t ");
        }
    }

    // to set custom class names from user input
    default String[] setClassNames(String[] oldClasses){

        Scanner sc = new Scanner(System.in);
        int n = oldClasses.length;
        String[] newClasses = new String[n];
        System.out.print("\n\nDo you want custom class names(y/n) ? ");
        char c ;
        // do-while loop to make user inputs y or n
        do {
            c = sc.next().toLowerCase().charAt(0);
            if (c == 'y'){
                for (int i = 0; i< n; i++) {
                    boolean check;
                    // do-while loop to ask for input again if user inputs invalid class name
                    do {
                        System.out.print("\nEnter class name for "+ oldClasses[i]+" : ");
                        // get class names from user
                        newClasses[i] = sc.next();
                        // checking if classname starts with a letter or not
                        check = Character.isAlphabetic(newClasses[i].toLowerCase().charAt(0));
                        logger.info("Checking if classname entered is valid");
                        if(!check){
                            logger.error("Invalid classname");
                            System.out.println("\nInvalid class name! Class name must start with a letter.");
                            System.out.print("Enter the class name again: \n");
                        }
                    }while (!check);
                }
            }else if (c == 'n'){
                // default class names set as new class names
                newClasses = oldClasses;
            }
            else {
                logger.error("Invalid input");
                System.out.print("\nInvalid choice! Enter only y/n. Please try again: ");
            }

        }while(c != 'y'&& c!='n');
        logger.info("Returning class names to createDesignPattern()");
        return newClasses;
    }

    // to set custom package name from user input
    default String setPackageName(String defaultPckgName){

        Scanner sc = new Scanner(System.in);
        String pckgName = null;
        System.out.print("\nDo you want custom package name for generated design pattern java files(y/n) ? ");
        char c ;
        // do-while loop to make user inputs y or n
        do {
            c = sc.next().toLowerCase().charAt(0);
            if (c == 'y'){
                System.out.print("\nPlease enter the package name: ");
                // get package name from user
                pckgName = sc.next();
                System.out.println("\n");
            } else if (c == 'n'){
                pckgName = defaultPckgName;
            }
            else {
                logger.error("Invalid input");
                System.out.print("\nInvalid choice! Enter only y/n. Please try again: ");
            }

        }while(c != 'y'&& c!='n');
        logger.info("Returning package name to createDesignPattern()");
        return pckgName;
    }

    // method implementation in child class which implements this interface
    JavaFile[] generateCode(String[] classes, String packageName);

    //method to write generated code as .java file
    default void writeJavaFiles(JavaFile[] files) throws IOException {
        for(JavaFile file : files){
//            System.out.println(file);
            file.writeTo(new File("outputs"));
        }
        logger.info("Written java code into output files ");
    }

    default void createDesignPattern(String[] oldClasses, String oldPackageName) throws IOException {
        logger.info("Entering createDesignPattern()");

        System.out.println("\nThe Design pattern will contain following classes: ");
        displayClassNames(oldClasses);
        String[] newClasses = setClassNames(oldClasses);
        String pckgName = setPackageName(oldPackageName);

        logger.info("Generating java code using JavaPoet");
        JavaFile[] code = generateCode(newClasses,pckgName);

        logger.info("Writing java code into output files");
        writeJavaFiles(code);
        System.out.println("\nThe following java files have been created: ");
        displayClassNames(newClasses);
        System.out.println("\n\nAt package name: " + pckgName);
        System.out.println("\n");

        logger.info("Exiting createDesignPattern()");

    }


}
