package com.behavioral;

import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import com.DesignPattern;

public class Visitor implements DesignPattern {

    String[] defaultClasses = {"Element","ConcreteElementA","ConcreteElementB","Visitor","ConcreteVisitor1",
    "ConcreteVisitor2", "ObjectStructure"};
    String packageName = "com.BehavioralDP.visitor";
    JavaFile[] generatedCode = new JavaFile[defaultClasses.length];

    public Visitor()throws IOException{
        createDesignPattern(defaultClasses,packageName);
    }

    public JavaFile[] generateCode(String[] classes, String packageName){

        ClassName Element =  ClassName.get("",classes[0]);
        ClassName ConcreteElementA =  ClassName.get("",classes[1]);
        ClassName ConcreteElementB =  ClassName.get("",classes[2]);
        ClassName Visitor =  ClassName.get("",classes[3]);
        ClassName ConcreteVisitor1 =  ClassName.get("",classes[4]);
        ClassName ConcreteVisitor2 =  ClassName.get("",classes[5]);
        ClassName ObjectStructure =  ClassName.get("",classes[6]);

        int i = 0;
//        Element interface declaration
        MethodSpec accept = MethodSpec.methodBuilder("accept")
                .addJavadoc("Defines an Accept operation that takes a visitor as an argument.")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT).returns(TypeName.VOID)
                .addParameter(Visitor, "visitor").build();
        TypeSpec element = TypeSpec.interfaceBuilder(Element)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(accept).build();
        generatedCode[i] = JavaFile.builder(packageName,element)
                .skipJavaLangImports(true).build();
        i += 1;


        MethodSpec visitConcElementA = MethodSpec.methodBuilder("visitConcreteElementA")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT).returns(TypeName.VOID)
                .addParameter(ConcreteElementA,"concreteElementA").build();

        MethodSpec visitConcElementB = MethodSpec.methodBuilder("visitConcreteElementB")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT).returns(TypeName.VOID)
                .addParameter(ConcreteElementB,"concreteElementB").build();


//        ConcreteElementA class decalaration
        FieldSpec counter = FieldSpec.builder(int.class,"counter",Modifier.PRIVATE)
                .initializer("0").build();

        MethodSpec operA = MethodSpec.methodBuilder("operationA")
                .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                .addStatement("$N++",counter.name).build();
        MethodSpec getCounter = MethodSpec.methodBuilder("getCounter")
                .addModifiers(Modifier.PROTECTED).returns(TypeName.INT)
                .addStatement("return $N",counter.name).build();

        TypeSpec concElementA = TypeSpec.classBuilder(ConcreteElementA)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Element)
                .addJavadoc("Implements accept operation.")
                .addField(counter)
                .addMethod(MethodSpec.methodBuilder(accept.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(Visitor,"visitor")
                        .addStatement("visitor.$N(this)",visitConcElementA.name)
                        .build())
                .addMethod(operA)
                .addMethod(getCounter)
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concElementA)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        ConcreteElementB class decalaration
        MethodSpec operB = MethodSpec.methodBuilder("operationB")
                .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                .addStatement("$N++",counter.name).build();

        TypeSpec concElementB = TypeSpec.classBuilder(ConcreteElementB)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Element)
                .addJavadoc("Implements accept operation.")
                .addField(counter)
                .addMethod(MethodSpec.methodBuilder(accept.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(Visitor,"visitor")
                        .addStatement("visitor.$N(this)",visitConcElementB.name)
                        .build())
                .addMethod(operB)
                .addMethod(getCounter)
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concElementB)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        Visitor interface declaration
        TypeSpec visitor = TypeSpec.interfaceBuilder(Visitor)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Declares a Visit operation for each class of ConcreteElement in the object structure")
                .addMethod(visitConcElementA)
                .addMethod(visitConcElementB).build();
        generatedCode[i] = JavaFile.builder(packageName,visitor)
                .skipJavaLangImports(true).build();
        i += 1;

//        ConcreteVisitor1 class declaration
        TypeSpec concVisitor1 = TypeSpec.classBuilder(ConcreteVisitor1)
                .addSuperinterface(Visitor)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder(visitConcElementA.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(ConcreteElementA,"concreteElementA")
                        .addStatement("concreteElementA.$N()",operA.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder(visitConcElementB.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(ConcreteElementB,"concreteElementB")
                        .addStatement("concreteElementB.$N()",operB.name)
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concVisitor1)
                .skipJavaLangImports(true).build();
        i += 1;

//        ConcreteVisitor2 class declaration
        TypeSpec concVisitor2 = TypeSpec.classBuilder(ConcreteVisitor2)
                .addSuperinterface(Visitor)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder(visitConcElementA.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(ConcreteElementA,"concreteElementA")
                        .addStatement("concreteElementA.$N()",operA.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder(visitConcElementB.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(ConcreteElementB,"concreteElementB")
                        .addStatement("concreteElementB.$N()",operB.name)
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concVisitor2)
                .skipJavaLangImports(true).build();
        i += 1;

//        ObjectStructure class declaration
        ClassName list = ClassName.get("java.util", "List");
        ClassName arrayList = ClassName.get("java.util", "ArrayList");
        ClassName Iterator = ClassName.get("java.util","Iterator");
        TypeName listOfElements = ParameterizedTypeName.get(list,Element);

        FieldSpec children = FieldSpec.builder(listOfElements,"children",Modifier.PRIVATE)
                .initializer("new $T<$T>()",arrayList,Element)
                .build();

        TypeSpec objStruct = TypeSpec.classBuilder(ObjectStructure)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Holds objects in structure. Provides interface to allow visitors to visit its elements.")
                .addField(children)
                .addMethod(MethodSpec.methodBuilder("add")
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(Element,"element")
                        .addStatement("$N.add(element)",children.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder("remove")
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(Element,"element")
                        .addStatement("$N.remove(element)",children.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder("getChild")
                        .addModifiers(Modifier.PUBLIC).returns(Element)
                        .addParameter(int.class,"index")
                        .addStatement("return $N.get(index)",children.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder("acceptAll")
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(Visitor,"visitor")
                        .beginControlFlow("($T iterator = $N.iterator(); iterator.hasNext();)",Iterator,children.name)
                        .addStatement("$T element = ($T) iterator.next()",Element,Element)
                        .addStatement("element.$N(visitor)",accept.name)
                        .endControlFlow()
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,objStruct)
                .skipJavaLangImports(true).build();

        return generatedCode;

    }


}
