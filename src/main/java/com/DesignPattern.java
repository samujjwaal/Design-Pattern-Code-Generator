package com;

import com.squareup.javapoet.JavaFile;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public interface DesignPattern {

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
                        System.out.print("Enter class name for "+ oldClasses[i]+" :");
                        newClasses[i] = sc.next();
                        check = Character.isDigit(newClasses[i].toLowerCase().charAt(0));
                        if(check){
                            System.out.println("\nInvalid class name! Class name can't start with a digit.");
                            System.out.print("Enter the class name again.");
                        }
                    }while (check);
                }
            }else if (c == 'n'){
                newClasses = oldClasses;
            }
            else {
                System.out.print("\nInvalid choice! Enter only y/n. Please try again.");
            }

        }while(c != 'y'&& c!='n');

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
            } else if (c == 'n'){
                pckgName = defaultPckgName;

            }
            else {
                System.out.print("\nInvalid choice! Enter only y/n. Please try again.");
            }

        }while(c != 'y'&& c!='n');

        return pckgName;
    }

    JavaFile[] generateCode(String[] classes, String packageName);

    default void writeJavaFiles(JavaFile[] files) throws IOException {
        for(JavaFile file : files){
            System.out.println(file);
            file.writeTo(new File("outputs"));
        }
    }

    default void createDesignPattern(String[] oldClasses, String oldPackageName) throws IOException {
        System.out.println("The Design pattern will contain following classes:");
        displayClassNames(oldClasses);
        String[] newClasses = setClassNames(oldClasses);
//        System.out.println(newClasses[0]);
        String pckgName = setPackageName(oldPackageName);
        JavaFile[] code = generateCode(newClasses,pckgName);
//        System.out.println(code[0]);
        writeJavaFiles(code);
        System.out.println("The following java files have been created:");
        displayClassNames(newClasses);
        System.out.println("\nAt package name: " + pckgName);
    }


}
