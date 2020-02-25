package com.behavioral.command;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

public class command {
    public static void main(String[] args) {
        String input1 = "Command";
        String input2 = "Invoker";
        String input3 = "Receiver";
        String input4 = "ConcreteCommand";
        String packageName = "com.BehavioralDP.command";

//        Command interface declaration
        ClassName Command = ClassName.get("",input1);
        MethodSpec execute = MethodSpec.methodBuilder("execute")
                .addModifiers(Modifier.ABSTRACT,Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .build();
        TypeSpec command = TypeSpec.interfaceBuilder(Command)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("""
                        Command interface, declares an interface for executing an operation""")
                .addMethod(execute)
                .build();
        JavaFile class1 = JavaFile.builder(packageName,command)
                .skipJavaLangImports(true)
                .build();

//        Invoker class declaration
        ClassName Invoker = ClassName.get("",input2);
        TypeSpec invoker = TypeSpec.classBuilder(Invoker)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("""
                        Invoker class, asks the command to carry out the request""")
                .addField(Command, "command", Modifier.PRIVATE)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(Command,"command")
                        .addStatement("this.$N = command", "command")
                        .build())
                .addMethod(MethodSpec.methodBuilder(execute.name)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.VOID)
                        .addStatement("$N.$N()","command", execute.name)
                        .build())
                .build();
        JavaFile class2 = JavaFile.builder(packageName,invoker)
                .skipJavaLangImports(true)
                .build();

//        Receiver class declaration
        ClassName Receiver = ClassName.get("",input3);
        FieldSpec opPerf = FieldSpec.builder(Boolean.TYPE,"operationPerfomed")
                .addModifiers(Modifier.PRIVATE)
                .initializer("false")
                .build();
        TypeSpec receiver = TypeSpec.classBuilder(Receiver)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("""
                        Receiver class, knows how to perform the operations associated with carrying
                        out a request""")
                .addField(opPerf)
                .addMethod(MethodSpec.methodBuilder("action")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.VOID)
                        .addStatement("$N = true",opPerf.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder("isOperationPerfomed")
                        .addModifiers(Modifier.PROTECTED)
                        .returns(TypeName.BOOLEAN)
                        .addStatement("return $N",opPerf.name)
                        .build())
                .build();
        JavaFile class3 = JavaFile.builder(packageName,receiver)
                .skipJavaLangImports(true)
                .build();

//        ConcreteCommand class declaration
        ClassName ConcreteCommand = ClassName.get("",input4);
        TypeSpec concreteCommand = TypeSpec.classBuilder(ConcreteCommand)
                .addSuperinterface(Command)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("""
                        ConcreteCommand class, defines a binding between a Receiver object and an
                        operation""")
                .addField(Receiver, "receiver",Modifier.PRIVATE)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(Receiver, "receiver")
                        .addStatement("this.$N = receiver","receiver")
                        .build())
                .addMethod(MethodSpec.methodBuilder(execute.name)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.VOID)
                        .addStatement("this.$N.$N()","receiver","action")
                        .build())
                .build();
        JavaFile class4 = JavaFile.builder(packageName,concreteCommand)
                .skipJavaLangImports(true)
                .build();

        System.out.println(class1);
        System.out.println(class2);
        System.out.println(class3);
        System.out.println(class4);
    }
}
