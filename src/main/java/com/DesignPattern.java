package com;

import com.squareup.javapoet.JavaFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public interface DesignPattern {

    //Define a static logger variable so that it references the Logger instance
    static final Logger logger = LoggerFactory.getLogger(DesignPattern.class);


    default void displayClassNames(String[] classes){

        for (String name: classes) {
        System.out.print(name + " \t ");
        }
    }

    default String[] setClassNames(String[] oldClasses){

        Scanner sc = new Scanner(System.in);
        int n = oldClasses.length;
        String[] newClasses = new String[n];
        System.out.print("\n\nDo you want custom class names(y/n) ? ");
        char c ;
        do {
            c = sc.next().toLowerCase().charAt(0);
            if (c == 'y'){
                for (int i = 0; i< n; i++) {
                    boolean check;
                    do {
                        System.out.print("\nEnter class name for "+ oldClasses[i]+" :");
                        newClasses[i] = sc.next();
                        check = Character.isAlphabetic(newClasses[i].toLowerCase().charAt(0));
                        logger.info("Checking if classname entered is valid");
                        if(!check){
                            logger.error("Invalid classname");
                            System.out.println("\nInvalid class name! Class name must start with a letter.");
                            System.out.print("Enter the class name again.\n");
                        }
                    }while (!check);
                }
            }else if (c == 'n'){
                newClasses = oldClasses;
            }
            else {
                logger.error("Invalid input");
                System.out.print("\nInvalid choice! Enter only y/n. Please try again.");
            }

        }while(c != 'y'&& c!='n');
        logger.info("Returning classnames to createDesignPattern()");
        return newClasses;
    }

    default String setPackageName(String defaultPckgName){

        Scanner sc = new Scanner(System.in);
        String pckgName = null;
        System.out.print("\nDo you want custom package name for generated design pattern java files(y/n) ? ");
        char c ;
        do {
            c = sc.next().toLowerCase().charAt(0);
            if (c == 'y'){
                System.out.print("\nPlease enter the package name:");
                pckgName = sc.next();
                System.out.println("\n");
            } else if (c == 'n'){
                pckgName = defaultPckgName;
            }
            else {
                logger.error("Invalid input");
                System.out.print("\nInvalid choice! Enter only y/n. Please try again.");
            }

        }while(c != 'y'&& c!='n');
        logger.info("Returning package name to createDesignPattern()");
        return pckgName;
    }

    JavaFile[] generateCode(String[] classes, String packageName);

    default void writeJavaFiles(JavaFile[] files) throws IOException {
        for(JavaFile file : files){
//            System.out.println(file);
            file.writeTo(new File("outputs"));
        }
    }

    default void createDesignPattern(String[] oldClasses, String oldPackageName) throws IOException {
        logger.info("Entering createDesignPattern()");

        System.out.println("\nThe Design pattern will contain following classes:");
        displayClassNames(oldClasses);
        String[] newClasses = setClassNames(oldClasses);
        String pckgName = setPackageName(oldPackageName);

        logger.info("Generating java code using JavaPoet");
        JavaFile[] code = generateCode(newClasses,pckgName);

        logger.info("Writing java code into output files");
        writeJavaFiles(code);
        System.out.println("\nThe following java files have been created:");
        displayClassNames(newClasses);
        System.out.println("\n\nAt package name: " + pckgName);
        System.out.println("\n");

        logger.info("Exiting createDesignPattern()");

    }


}
