package com.behavioral;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import com.DesignPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class State implements DesignPattern {

    //Define a static logger variable so that it references the Logger instance
    private static final Logger logger = LoggerFactory.getLogger(State.class);


    String[] defaultClasses = {"State", "ConcreteState","Context"};
    String packageName = "com.BehavioralDP.state";
    JavaFile[] generatedCode = new JavaFile[defaultClasses.length];

    public State()throws IOException{
        logger.info("Executing State()");
        createDesignPattern(defaultClasses,packageName);
    }

    public JavaFile[] generateCode(String[] classes, String packageName){
        logger.info("Executing generateCode()");

        int i = 0;

//        State interface declaration
        ClassName State = ClassName.get("",classes[i]);
        MethodSpec handle = MethodSpec.methodBuilder("handle")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .returns(TypeName.VOID)
                .build();
        TypeSpec state = TypeSpec.interfaceBuilder(State)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("The interface for encapsulating the behavior associated with a particular\n" +
                            "state of the Context.")
                .addMethod(handle)
                .build();
        generatedCode[i] = JavaFile.builder(packageName,state)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        ConcreteState class declaration
        ClassName ConcreteState = ClassName.get("",classes[i]);
        FieldSpec handleInv = FieldSpec.builder(TypeName.BOOLEAN,"handleInvoked")
                .addModifiers(Modifier.PRIVATE)
                .initializer("false")
                .build();
        TypeSpec concreteState = TypeSpec.classBuilder(ConcreteState)
                .addSuperinterface(State)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("ConcreteState implements a behavior associated with a state of the Context.")
                .addField(handleInv)
                .addMethod(MethodSpec.methodBuilder(handle.name)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.VOID)
                        .addStatement("this.$N = true",handleInv.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder("isHandleInvoked")
                        .addModifiers(Modifier.PROTECTED)
                        .returns(TypeName.BOOLEAN)
                        .addStatement("return $N", handleInv)
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concreteState)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        Context class declaration
        ClassName Context = ClassName.get("",classes[i]);
        TypeSpec context = TypeSpec.classBuilder(Context)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Context maintains an instance of a ConcreteState subclass that defines the\n" +
                            "current state.")
                .addField(ConcreteState, "state",Modifier.PRIVATE)
                .addMethod(MethodSpec.methodBuilder("request")
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("$N.$N()","state",handle.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder("setState")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.VOID)
                        .addParameter(State, "state")
                        .addStatement("this.$N = state", "state")
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,context)
                .skipJavaLangImports(true)
                .build();

        logger.info("Returning generated java code to be written in files");

        return generatedCode;
    }
}
