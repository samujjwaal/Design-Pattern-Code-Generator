package com.behavioral.state;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

public class state {
    public static void main(String[] args) {
        String input1 = "State";
        String input2 = "ConcreteState";
        String input3 = "Context";
        String packageName = "com.BehavioralDP.state";

//        State interface declaration
        ClassName State = ClassName.get("",input1);
        MethodSpec handle = MethodSpec.methodBuilder("handle")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .returns(TypeName.VOID)
                .build();
        TypeSpec state = TypeSpec.interfaceBuilder(State)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("""
                        The interface for encapsulating the behavior associated with a particular
                        state of the Context.""")
                .addMethod(handle)
                .build();
        JavaFile class1 = JavaFile.builder(packageName,state)
                .skipJavaLangImports(true)
                .build();

//        ConcreteState class declaration
        ClassName ConcreteState = ClassName.get("",input2);
        FieldSpec handleInv = FieldSpec.builder(TypeName.BOOLEAN,"handleInvoked")
                .addModifiers(Modifier.PRIVATE)
                .initializer("false")
                .build();
        TypeSpec concreteState = TypeSpec.classBuilder(ConcreteState)
                .addSuperinterface(State)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("""
                        ConcreteState implements a behavior associated with a state of the Context.""")
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
        JavaFile class2 = JavaFile.builder(packageName,concreteState)
                .skipJavaLangImports(true)
                .build();

//        Context class declaration
        ClassName Context = ClassName.get("",input3);
        TypeSpec context = TypeSpec.classBuilder(Context)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("""
                        Context maintains an instance of a ConcreteState subclass that defines the
                        current state.""")
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
        JavaFile class3 = JavaFile.builder(packageName,context)
                .skipJavaLangImports(true)
                .build();

        System.out.println(class1);
        System.out.println(class2);
        System.out.println(class3);
    }
}
