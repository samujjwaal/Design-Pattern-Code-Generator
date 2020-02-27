package com.structural;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import com.DesignPattern;

public class Adapter implements DesignPattern {

    String[] defaultClasses = {"Target", "Adaptee","Adapter"};
    String packageName = "com.StructuralDP.adapter";
    JavaFile[] generatedCode = new JavaFile[defaultClasses.length];

    public Adapter()throws IOException{
        createDesignPattern(defaultClasses,packageName);
    }

    public JavaFile[] generateCode(String[] classes, String packageName){

        int i = 0;

//      Target interface declaration
        ClassName Target = ClassName.get("",classes[i]);
        TypeSpec target = TypeSpec.interfaceBuilder(Target)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Target interface, defines domain-specific interface to which Adaptee will be adapted")
                .addMethod(MethodSpec.methodBuilder("request")
                        .addModifiers(Modifier.ABSTRACT,Modifier.PUBLIC)
                        .returns(String.class)
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,target)
                .skipJavaLangImports(true)
                .build();
        i += 1;
//      Adaptee class declaration
        ClassName Adaptee = ClassName.get("",classes[i]);
        TypeSpec adaptee = TypeSpec.classBuilder(Adaptee)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Adaptee class, interface which will be adapted")
                .addMethod(MethodSpec.methodBuilder("specialRequest")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addStatement("return $S","specialRequest")
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,adaptee)
                .skipJavaLangImports(true)
                .build();
        i += 1;
//      Adapter class declaration
        ClassName Adapter =ClassName.get("",classes[i]);
        TypeSpec adapter = TypeSpec.classBuilder(Adapter)
                .addSuperinterface(Target)
                .addJavadoc("Adapter class, adapts Adaptee to the Target interface")
                .addModifiers(Modifier.PUBLIC)
                .addField(Adaptee, "adaptee",Modifier.PRIVATE)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(Adaptee, "adaptee")
                        .addStatement("this.adaptee = adaptee")
                        .build())
                .addMethod(MethodSpec.methodBuilder("request")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addStatement("return adaptee.specialRequest()")
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,adapter)
                .skipJavaLangImports(true)
                .build();

        return generatedCode;

    }
}
