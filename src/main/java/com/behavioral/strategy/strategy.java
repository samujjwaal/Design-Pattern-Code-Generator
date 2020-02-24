package com.behavioral.strategy;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

public class strategy {
    public static void main(String[] args) {
        String input1 = "Strategy";
        String input2 = "Context";
        String input3 = "ConcreteStrategyA";
        String input4 = "ConcreteStrategyB";
        String input5 = "ConcreteStrategyC";
        String packageName = "com.BehavioralDP.strategy";

//        Strategy interface declaration
        ClassName Strategy = ClassName.get("",input1);
        MethodSpec algoInterface = MethodSpec.methodBuilder("algorithmInterface")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .returns(String.class)
                .build();
        TypeSpec strategy = TypeSpec.interfaceBuilder(Strategy)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("""
                Declares an interface common to all supported algorithms. Context uses this interface to call the algorithm
                defined by a ConcreteStrategy.""")
                .addMethod(algoInterface)
                .build();
        JavaFile class1 = JavaFile.builder(packageName,strategy)
                .skipJavaLangImports(true)
                .build();

//        Context class declaration
        ClassName Context = ClassName.get("",input2);
        FieldSpec strategyField = FieldSpec.builder(Strategy,"strategy",Modifier.PRIVATE).build();
        TypeSpec context = TypeSpec.classBuilder(Context)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc(" Maintains a reference to a Strategy object. Invokes algorithm implemented in ConcreteStrategy.")
                .addField(strategyField)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(Strategy, "strategy")
                        .addStatement("this.$N = strategy",strategyField.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder("contextInterface")
                        .addModifiers(Modifier.PROTECTED).returns(String.class)
                        .addStatement("return this.$N.$N()",strategyField.name,algoInterface.name)
                        .build())
                .build();
        JavaFile class2 = JavaFile.builder(packageName,context)
                .skipJavaLangImports(true)
                .build();

//        ConcreteStrategyA class declaration
        ClassName ConcreteStrategyA = ClassName.get("",input3);
        TypeSpec concStratA = TypeSpec.classBuilder(ConcreteStrategyA)
                .addSuperinterface(Strategy)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Implements the algorithm defined in Strategy interface.")
                .addMethod(MethodSpec.methodBuilder(algoInterface.name)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addStatement("return $S","Inside ConcreteStrategyA to invoke Algorithm A")
                        .build())
                .build();
        JavaFile class3 = JavaFile.builder(packageName,concStratA)
                .skipJavaLangImports(true)
                .build();

//        ConcreteStrategyB class declaration
        ClassName ConcreteStrategyB = ClassName.get("",input4);
        TypeSpec concStratB = TypeSpec.classBuilder(ConcreteStrategyB)
                .addSuperinterface(Strategy)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Implements the algorithm defined in Strategy interface.")
                .addMethod(MethodSpec.methodBuilder(algoInterface.name)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addStatement("return $S","Inside ConcreteStrategyB to invoke Algorithm B")
                        .build())
                .build();
        JavaFile class4 = JavaFile.builder(packageName,concStratB)
                .skipJavaLangImports(true)
                .build();

//        ConcreteStrategyC class declaration
        ClassName ConcreteStrategyC = ClassName.get("",input5);
        TypeSpec concStratC = TypeSpec.classBuilder(ConcreteStrategyC)
                .addSuperinterface(Strategy)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Implements the algorithm defined in Strategy interface.")
                .addMethod(MethodSpec.methodBuilder(algoInterface.name)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addStatement("return $S","Inside ConcreteStrategyC to invoke Algorithm C")
                        .build())
                .build();
        JavaFile class5 = JavaFile.builder(packageName,concStratC)
                .skipJavaLangImports(true)
                .build();

        System.out.println(class1);
        System.out.println(class2);
        System.out.println(class3);
        System.out.println(class4);
        System.out.println(class5);
    }
}
