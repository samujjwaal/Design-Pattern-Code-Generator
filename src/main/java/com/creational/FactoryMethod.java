package com.creational;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import com.DesignPattern;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class FactoryMethod implements DesignPattern {

    //Define a static logger variable so that it references the Logger instance
    private static final Logger logger = (Logger) LoggerFactory.getLogger(FactoryMethod.class);

    String[] defaultClasses = {"Product","Factory","ConcreteProductA","ConcreteProductB","ConcreteFactory"};
    String packageName = "com.CreationalDP.factoryMethod";
    JavaFile[] generatedCode = new JavaFile[defaultClasses.length];

    public FactoryMethod(int flag)throws IOException {
        logger.info("Executing FactoryMethod()");

        if (flag == 1) {
            createDesignPattern(defaultClasses, packageName);
        }
    }

    @Override
    public JavaFile[] generateCode(String[] classes, String packageName){
        logger.info("Executing generateCode()");

        int i = 0;
//        Product interface declaration
        ClassName Product = ClassName.get("",classes[i]);
        TypeSpec product = TypeSpec.interfaceBuilder(Product)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Product interface, defines the interface of the objects which factory method creates.")
                .build();
        generatedCode[i] = JavaFile.builder(packageName,product)
                .skipJavaLangImports(true)
                .build();
        i += 1;
//        Factory abstract class declaration
        ClassName Factory = ClassName.get("",classes[i]);
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
        generatedCode[i] = JavaFile.builder(packageName,factory)
                .skipJavaLangImports(true)
                .build();
        i += 1;
//        ConcreteProductA class declaration
        ClassName ConcreteProductA = ClassName.get("",classes[i]);
        TypeSpec concProdA = TypeSpec.classBuilder(ConcreteProductA)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Product)
                .addJavadoc("ConcreteProductA class implements Product interface")
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concProdA)
                .skipJavaLangImports(true)
                .build();
        i += 1;
//        ConcreteProductB class declaration
        ClassName ConcreteProductB = ClassName.get("",classes[i]);
        TypeSpec concProdB = TypeSpec.classBuilder(ConcreteProductB)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Product)
                .addJavadoc("ConcreteProductB class implements Product interface")
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concProdB)
                .skipJavaLangImports(true)
                .build();
        i += 1;
//        ProductFactory class declaration
        ClassName ConcreteFactory = ClassName.get("",classes[i]);
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
        generatedCode[i] = JavaFile.builder(packageName,concFactory)
                .skipJavaLangImports(true)
                .build();

        logger.info("Returning generated java code to be written in files");

        return generatedCode;
    }
}
