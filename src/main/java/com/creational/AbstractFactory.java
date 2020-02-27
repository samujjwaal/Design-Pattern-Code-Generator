package com.creational;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import com.DesignPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractFactory implements DesignPattern {

    //Define a static logger variable so that it references the Logger instance
    private static final Logger logger = LoggerFactory.getLogger(AbstractFactory.class);

    String[] defaultClasses = {"AbstractProductA","ProductA1","ProductA2","AbstractProductB","ProductB1","ProductB2",
            "AbstractFactory","ConcreteFactory1","ConcreteFactory2"};
    String packageName = "com.CreationalDP.abstractFactory";
    JavaFile[] generatedCode = new JavaFile[defaultClasses.length];

    public AbstractFactory()throws IOException {
        logger.info("Executing AbstractFactory()");
        createDesignPattern(defaultClasses,packageName);
    }

    @Override
    public JavaFile[] generateCode(String[] classes, String packageName){
        logger.info("Executing generateCode()");

        int i = 0;
//        AbstractProductA interface declaration
        ClassName AbstractProductA = ClassName.get("",classes[i]);
        TypeSpec abstProductA = TypeSpec.interfaceBuilder(AbstractProductA)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("AbstractProductA defines interface for ProductA objects")
                .build();
        generatedCode[i] = JavaFile.builder(packageName,abstProductA)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        ProductA1 class declaration
        ClassName ProductA1 = ClassName.get("",classes[i]);
        TypeSpec prodA1 = TypeSpec.classBuilder(ProductA1)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(AbstractProductA)
                .addJavadoc("ProductA1, implements AbstractProductA interface")
                .build();
        generatedCode[i] = JavaFile.builder(packageName,prodA1)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        ProductA2 class declaration
        ClassName ProductA2 = ClassName.get("",classes[i]);
        TypeSpec prodA2 = TypeSpec.classBuilder(ProductA2)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(AbstractProductA)
                .addJavadoc("ProductA2, implements AbstractProductA interface")
                .build();
        generatedCode[i] = JavaFile.builder(packageName,prodA2)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        AbstractProductB interface declaration
        ClassName AbstractProductB = ClassName.get("",classes[i]);
        TypeSpec abstProductB = TypeSpec.interfaceBuilder(AbstractProductB)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("AbstractProductB defines interface for ProductB objects")
                .build();
        generatedCode[i] = JavaFile.builder(packageName,abstProductB)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        ProductB1 class declaration
        ClassName ProductB1 = ClassName.get("",classes[i]);
        TypeSpec prodB1 = TypeSpec.classBuilder(ProductB1)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(AbstractProductB)
                .addJavadoc("ProductB1, implements AbstractProductB interface")
                .build();
        generatedCode[i] = JavaFile.builder(packageName,prodB1)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        ProductB2 class declaration
        ClassName ProductB2 = ClassName.get("",classes[i]);
        TypeSpec prodB2 = TypeSpec.classBuilder(ProductB2)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(AbstractProductB)
                .addJavadoc("ProductB1, implements AbstractProductB interface")
                .build();
        generatedCode[i] = JavaFile.builder(packageName,prodB2)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        AbstractFactory interface declaration
        ClassName AbstractFactory = ClassName.get("",classes[i]);
        MethodSpec createProdA = MethodSpec.methodBuilder("create" + classes[0])
                .addModifiers(Modifier.ABSTRACT,Modifier.PUBLIC)
                .returns(AbstractProductA)
                .build();
        MethodSpec createProdB = MethodSpec.methodBuilder("create" + classes[3] )
                .addModifiers(Modifier.ABSTRACT,Modifier.PUBLIC)
                .returns(AbstractProductB)
                .build();
        TypeSpec abstractFactory = TypeSpec.interfaceBuilder(AbstractFactory)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Abstract Factory, defines interface for creation of the abstract product objects")
                .addMethod(createProdA)
                .addMethod(createProdB)
                .build();
        generatedCode[i] = JavaFile.builder(packageName,abstractFactory)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        ConcreteFactory1 interface declaration
        ClassName ConcreteFactory1 = ClassName.get("",classes[i]);
        TypeSpec concFactory1 = TypeSpec.classBuilder(ConcreteFactory1)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("ConcreteFactory1, implements creation of the concrete Product1 objects")
                .addMethod(MethodSpec.methodBuilder(createProdA.name)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(AbstractProductA)
                        .addStatement("return new $N()", prodA1.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder(createProdB.name)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(AbstractProductB)
                        .addStatement("return new $N()", prodB1.name)
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concFactory1)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        ConcreteFactory2 interface declaration
        ClassName ConcreteFactory2 = ClassName.get("",classes[i]);
        TypeSpec concFactory2 = TypeSpec.classBuilder(ConcreteFactory2)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("ConcreteFactory2, implements creation of the concrete Product2 objects")
                .addMethod(MethodSpec.methodBuilder(createProdA.name)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(AbstractProductA)
                        .addStatement("return new $N()", prodA2.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder(createProdB.name)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(AbstractProductB)
                        .addStatement("return new $N()", prodB2.name)
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concFactory2)
                .skipJavaLangImports(true)
                .build();

        logger.info("Returning generated java code to be written in files");

        return generatedCode;
    }

}
