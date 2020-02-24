package com.structural.adapter;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

public class adapter {
    public static void main(String[] args) {

        String input1 = "Target";
        String input2 = "Adaptee";
        String input3 = "Adapter";
        String packageName = "com.StructuralDP.adapter";

//      Target interface declaration
        ClassName Target = ClassName.get("",input1);
        TypeSpec target = TypeSpec.interfaceBuilder(Target)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Target interface, defines domain-specific interface to which Adaptee will be adapted")
                .addMethod(MethodSpec.methodBuilder("request")
                        .addModifiers(Modifier.ABSTRACT,Modifier.PUBLIC)
                        .returns(String.class)
                        .build())
                .build();
        JavaFile javaFile1 = JavaFile.builder(packageName,target)
                .skipJavaLangImports(true)
                .build();

//      Adaptee class declaration
        ClassName Adaptee = ClassName.get("",input2);
        TypeSpec adaptee = TypeSpec.classBuilder(Adaptee)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Adaptee class, interface which will be adapted")
                .addMethod(MethodSpec.methodBuilder("specialRequest")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addStatement("return $S","specialRequest")
                        .build())
                .build();
        JavaFile javaFile2 = JavaFile.builder(packageName,adaptee)
                .skipJavaLangImports(true)
                .build();

//      Adapter class declaration
        ClassName Adapter =ClassName.get("",input3);
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
        JavaFile javaFile3 = JavaFile.builder(packageName,adapter)
                .skipJavaLangImports(true)
                .build();

        System.out.println(javaFile1);
        System.out.println(javaFile2);
        System.out.println(javaFile3);

    }
}
