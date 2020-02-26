package com.behavioral.templateMethod;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

public class templateMethod {
    public static void main(String[] args) {
        String input1 = "AbstractClass";
        String input2 = "ConcreteClass";
        String packageName = "com.BehavioralDP.templateMethod";

        ClassName abstractclass = ClassName.get("",input1);
        ClassName concreteclass = ClassName.get("",input2);

//        AbstractClass declaration
        MethodSpec primOp1 = MethodSpec.methodBuilder("primitiveOperation1")
                .addModifiers(Modifier.ABSTRACT)
                .returns(String.class)
                .build();
        MethodSpec primOp2 = MethodSpec.methodBuilder("primitiveOperation2")
                .addModifiers(Modifier.ABSTRACT)
                .returns(String.class)
                .build();

        TypeSpec abstractC = TypeSpec.classBuilder(abstractclass)
                .addModifiers(Modifier.ABSTRACT)
                .addJavadoc("Defines interfaces for primitive operations. Implements algorithm.")
                .addMethod(MethodSpec.methodBuilder("templateMethod")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addJavadoc("""
                                Template method, implementation of algorithm which consists of
                                primitiveOperations

                                @return result of the primitive operations""")
                        .addStatement("return this.$N() + this.$N()",primOp1.name,primOp2.name)
                        .build())
                .addMethod(primOp1)
                .addMethod(primOp2)
                .build();
        JavaFile javaFile1 = JavaFile.builder(packageName,abstractC)
                .skipJavaLangImports(true)
                .build();

//        ConcreteClass declaration
        TypeSpec concreteC = TypeSpec.classBuilder(concreteclass)
                .superclass(abstractclass)
                .addJavadoc("""
                        Implements the primitive operations to carry out subclass-specific steps of
                        the algorithm.""")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder(primOp1.name)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addStatement("return $S","Template")
                        .build())
                .addMethod(MethodSpec.methodBuilder(primOp2.name)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addStatement("return $S","Method")
                        .build())
                .build();
        JavaFile javaFile2 = JavaFile.builder(packageName,concreteC)
                .skipJavaLangImports(true)
                .build();

        System.out.println(javaFile1);
        System.out.println(javaFile2);

    }
}
