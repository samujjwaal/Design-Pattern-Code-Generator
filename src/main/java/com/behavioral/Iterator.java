package com.behavioral;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import com.DesignPattern;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;


public class Iterator implements DesignPattern {
    //Define a static logger variable so that it references the Logger instance
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Iterator.class);


    String[] defaultClasses = {"Iterator", "Aggregate","ConcreteAggregate","ConcreteIterator"};
    String packageName = "com.BehavioralDP.iterator";
    JavaFile[] generatedCode = new JavaFile[defaultClasses.length];

    public Iterator(int flag)throws IOException{
        logger.info("Executing Iterator()");
        if(flag == 1) {
            createDesignPattern(defaultClasses, packageName);
        }
    }

    public JavaFile[] generateCode(String[] classes, String packageName){
        logger.info("Executing generateCode()");

//        Observer interface declaration
        ClassName Iterator = ClassName.get("",classes[0]);
        ClassName ConcreteIterator = ClassName.get("",classes[3]);
        MethodSpec first = MethodSpec.methodBuilder("first")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT).returns(TypeName.OBJECT)
                .build();
        MethodSpec next = MethodSpec.methodBuilder("next")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT).returns(TypeName.OBJECT)
                .build();
        MethodSpec isDone = MethodSpec.methodBuilder("isDone")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT).returns(TypeName.BOOLEAN)
                .build();
        MethodSpec currentItem = MethodSpec.methodBuilder("currentItem")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT).returns(TypeName.OBJECT)
                .build();
        TypeSpec iterator = TypeSpec.interfaceBuilder(Iterator)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Iterator defines an interface for accessing and traversing elements.")
                .addMethod(first)
                .addMethod(next)
                .addMethod(isDone)
                .addMethod(currentItem)
                .build();
        generatedCode[0] = JavaFile.builder(packageName,iterator)
                .skipJavaLangImports(true)
                .build();

//        Aggregate interface declaration
        ClassName Aggregate = ClassName.get("",classes[1]);
        MethodSpec createIterator = MethodSpec.methodBuilder("createIterator")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT).returns(Iterator)
                .build();
        TypeSpec aggregate = TypeSpec.interfaceBuilder(Aggregate)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Aggregate defines an interface for creating an Iterator object.")
                .addMethod(createIterator)
                .build();
        generatedCode[1] = JavaFile.builder(packageName,aggregate)
                .skipJavaLangImports(true)
                .build();

//        ConcreteAggregate class declaration
        ClassName ConcreteAggregate = ClassName.get("",classes[2]);
//        String[] stringRecords = { "first", "second", "third", "fourth" };
        FieldSpec records = FieldSpec.builder(String[].class,"records",Modifier.FINAL,Modifier.PRIVATE)
                .initializer(" { \"first\", \"second\", \"third\", \"fourth\" }")
                .build();
        MethodSpec getRecords= MethodSpec.methodBuilder("getRecords")
                .addModifiers(Modifier.PROTECTED).returns(String[].class)
                .addStatement("return $N", records.name)
                .build();
        TypeSpec concreteAggregate = TypeSpec.classBuilder(ConcreteAggregate)
                .addSuperinterface(Aggregate)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("ConcreteAggregate implements the Iterator creation interface to return an instance of the proper ConcreteIterator.")
                .addField(records)
                .addMethod(MethodSpec.methodBuilder(createIterator.name)
                        .addModifiers(Modifier.PUBLIC).returns(Iterator)
                        .addStatement("return new $T(this)", ConcreteIterator)
                        .build())
                .addMethod(getRecords)
                .build();
        generatedCode[2] = JavaFile.builder(packageName,concreteAggregate)
                .skipJavaLangImports(true).build();


//        ConcreteIterator class declaration
        FieldSpec concAgg = FieldSpec.builder(ConcreteAggregate,"concreteAggregate",Modifier.PRIVATE)
                .build();
        FieldSpec index = FieldSpec.builder(int.class,"index",Modifier.PRIVATE)
                .initializer("-1")
                .build();
        TypeSpec concreteIterator = TypeSpec.classBuilder(ConcreteIterator)
                .addSuperinterface(Iterator)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("ConcreteIterator implements the Iterator interface. Keeps track of the current position in the traversal of the aggregate.")
                .addField(concAgg)
                .addField(index)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ConcreteAggregate, "concreteAggregate")
                        .addStatement("this.$N = concreteAggregate",concAgg.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder(first.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.OBJECT)
                        .addStatement("$N = 0",index.name)
                        .addStatement("return $N.$N()[$N]",concAgg.name,getRecords.name,index.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder(next.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.OBJECT)
                        .addStatement("$N++",index.name)
                        .addStatement("return $N.$N()[$N]",concAgg.name,getRecords.name,index.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder(isDone.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.BOOLEAN)
                        .beginControlFlow("if ($N.$N().length == ($N + 1))",concAgg.name,getRecords.name,index.name)
                        .addStatement("return true")
                        .endControlFlow()
                        .addStatement("return false")
                        .build())
                .addMethod(MethodSpec.methodBuilder(currentItem.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.OBJECT)
                        .addStatement("return $N.$N()[$N]",concAgg.name,getRecords.name,index.name)
                        .build())
                .build();
        generatedCode[3] = JavaFile.builder(packageName,concreteIterator)
                .skipJavaLangImports(true).build();

        logger.info("Returning generated java code to be written in files");

        return generatedCode;
    }
}
