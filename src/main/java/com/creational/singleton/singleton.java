package com.creational.singleton;

import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class singleton {

    public static void main(String[] args) throws IOException{

//        Scanner sc = new Scanner(System.in);
//        System.out.println("Enter class name:");
//        String input = sc.next();
//        System.out.println(input);
        String input = "Singleton";
        ClassName Singleton = ClassName.get("",input);

        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .build();

        FieldSpec instance = FieldSpec.builder(Singleton, "INSTANCE")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .build();

        MethodSpec getInstance = MethodSpec.methodBuilder("getInstance")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(Singleton)
                .beginControlFlow("if ($N == null)", instance.name)
                .addStatement("$N = new $T()",instance.name, Singleton)
                .endControlFlow()
                .addStatement("return $N", instance.name)
                .build();

        TypeSpec singleton = TypeSpec.classBuilder(Singleton)
                .addModifiers(Modifier.PUBLIC)
                .addField(instance)
                .addMethod(constructor)
                .addMethod(getInstance)
                .build();

        JavaFile javaFile = JavaFile.builder("com.CreationalDP.singleton", singleton)
                .skipJavaLangImports(true)
                .build();

        System.out.println(javaFile);
        javaFile.writeTo(new File("outputs"));
//        javaFile.writeTo(new File("C:\\Users\\Samujjwaal Dey\\Desktop\\CS 474 OOLE\\outputs/singleton.java"));


    }
}
