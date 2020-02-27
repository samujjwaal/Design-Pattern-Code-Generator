package com.structural;

import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import com.DesignPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bridge implements DesignPattern {

    //Define a static logger variable so that it references the Logger instance
    private static final Logger logger = LoggerFactory.getLogger(Bridge.class);


    String[] defaultClasses = {"Implementor", "ConcreteImplementorA","ConcreteImplementorB","Abstraction","RefinedAbstraction"};
    String packageName = "com.StructuralDP.bridge";
    JavaFile[] generatedCode = new JavaFile[defaultClasses.length];

    public Bridge()throws IOException{
        logger.info("Executing Bridge()");
        createDesignPattern(defaultClasses,packageName);
    }

    public JavaFile[] generateCode(String[] classes, String packageName){
        int i = 0 ;
        logger.info("Executing generateCode()");


//        Implementor interface declaration
        ClassName Implementor = ClassName.get("",classes[i]);
        MethodSpec implementation = MethodSpec.methodBuilder("implementation")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT).returns(String.class)
                .build();
        TypeSpec implementor = TypeSpec.interfaceBuilder(Implementor)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Implementor, defines interface for implementation")
                .addMethod(implementation)
                .build();
        generatedCode[i] = JavaFile.builder(packageName,implementor).skipJavaLangImports(true).build();
        i += 1;

//        ConcreteImplementorA class declaration
        ClassName ConcreteImplementorA = ClassName.get("",classes[i]);
        TypeSpec concImplementorA = TypeSpec.classBuilder(ConcreteImplementorA)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Implementor)
                .addJavadoc("ConcreteImplementorA, implements Implementor interface")
                .addMethod(MethodSpec.methodBuilder(implementation.name)
                        .addModifiers(Modifier.PUBLIC).returns(String.class)
                        .addStatement("return this.getClass().getName()")
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concImplementorA).skipJavaLangImports(true).build();
        i += 1;

//        ConcreteImplementorB class declaration
        ClassName ConcreteImplementorB = ClassName.get("",classes[i]);
        TypeSpec concImplementorB = TypeSpec.classBuilder(ConcreteImplementorB)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Implementor)
                .addJavadoc("ConcreteImplementorB, implements Implementor interface")
                .addMethod(MethodSpec.methodBuilder(implementation.name)
                        .addModifiers(Modifier.PUBLIC).returns(String.class)
                        .addStatement("return this.getClass().getName()")
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concImplementorB).skipJavaLangImports(true).build();
        i += 1;

//        Abstraction abstract class declaration
        ClassName Abstraction = ClassName.get("",classes[i]);
        FieldSpec implement = FieldSpec.builder(Implementor,"implementor",Modifier.PROTECTED).build();
        MethodSpec oper = MethodSpec.methodBuilder("operation")
                .addModifiers(Modifier.ABSTRACT).returns(String.class)
                .build();
        TypeSpec abstraction = TypeSpec.classBuilder(Abstraction)
                .addModifiers(Modifier.ABSTRACT)
                .addJavadoc("Abstraction, defines abstraction interface, maintains a reference to object of type Implementor")
                .addField(implement)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(Implementor,"implementor")
                        .addStatement("this.$N = implementor",implement.name)
                        .build())
                .addMethod(oper)
                .build();
        generatedCode[i] = JavaFile.builder(packageName,abstraction).skipJavaLangImports(true).build();
        i += 1;

//        RefinedAbstraction class declaration
        ClassName RefinedAbstraction = ClassName.get("",classes[i]);
        TypeSpec refAbstraction = TypeSpec.classBuilder(RefinedAbstraction)
                .addModifiers(Modifier.PUBLIC)
                .superclass(Abstraction)
                .addJavadoc("Refined Abstraction, extends the interface defined by Abstraction")
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(Implementor,"implementor")
                        .addStatement("super(implementor)",implement.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder("operation")
                        .addModifiers(Modifier.PUBLIC).returns(String.class)
                        .addStatement("return this.$N.$N()",implement.name,implementation.name)
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,refAbstraction).skipJavaLangImports(true).build();

        logger.info("Returning generated java code to be written in files");

        return generatedCode;
    }
}
