package com.creational.factoryMethod;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

public class factoryMethod {
    public static void main(String[] args) {
        String input1 = "Product";
        String input2 = "Factory";
        String input3 = "ConcreteProductA";
        String input4 = "ConcreteProductB";
        String input5 = "ConcreteFactory";
        String packageName = "com.CreationalDP.factoryMethod";

//        Product interface declaration
        ClassName Product = ClassName.get("",input1);
        TypeSpec product = TypeSpec.interfaceBuilder(Product)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Product interface, defines the interface of the objects which factory method creates.")
                .build();
        JavaFile class1 = JavaFile.builder(packageName,product)
                .skipJavaLangImports(true)
                .build();

//        Factory abstract class declaration
        ClassName Factory = ClassName.get("",input2);
        MethodSpec factoryMethod = MethodSpec.methodBuilder("factoryMethod")
                .addModifiers(Modifier.ABSTRACT)
                .returns(Product)
                .addParameter(String.class,"type")
                .build();
        TypeSpec factory = TypeSpec.classBuilder(Factory)
                .addModifiers(Modifier.ABSTRACT)
                .addJavadoc("Factory class declares factory method")
                .addMethod(factoryMethod)
                .build();
        JavaFile class2 = JavaFile.builder(packageName,factory)
                .skipJavaLangImports(true)
                .build();

//        ConcreteProductA class declaration
        ClassName ConcreteProductA = ClassName.get("",input3);
        TypeSpec concProdA = TypeSpec.classBuilder(ConcreteProductA)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Product)
                .addJavadoc("ConcreteProductA class implements Product interface")
                .build();
        JavaFile class3 = JavaFile.builder(packageName,concProdA)
                .skipJavaLangImports(true)
                .build();

//        ConcreteProductB class declaration
        ClassName ConcreteProductB = ClassName.get("",input4);
        TypeSpec concProdB = TypeSpec.classBuilder(ConcreteProductB)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Product)
                .addJavadoc("ConcreteProductB class implements Product interface")
                .build();
        JavaFile class4 = JavaFile.builder(packageName,concProdB)
                .skipJavaLangImports(true)
                .build();

//        ProductFactory class declaration
        ClassName ConcreteFactory = ClassName.get("",input5);
        TypeSpec concFactory = TypeSpec.classBuilder(ConcreteFactory)
                .superclass(Factory)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder("productFactory")
                        .addModifiers(Modifier.PUBLIC).returns(Product)
                        .addParameter(String.class,"type")
                        .beginControlFlow("if (type.equals(\"A\"))")
                        .addStatement("return new $T()",ConcreteProductA)
                        .nextControlFlow("else if (type.equals(\"B\"))")
                        .addStatement("return new $T()",ConcreteProductB)
                        .endControlFlow()
                        .addStatement("return null")
                        .build())
                .build();
        JavaFile class5 = JavaFile.builder(packageName,concFactory)
                .skipJavaLangImports(true)
                .build();

        System.out.println(class1);
        System.out.println(class2);
        System.out.println(class3);
        System.out.println(class4);
        System.out.println(class5);
    }
}
