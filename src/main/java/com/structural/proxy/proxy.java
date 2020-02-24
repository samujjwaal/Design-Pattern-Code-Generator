package com.structural.proxy;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

public class proxy {
    public static void main(String[] args) {
        String input1 = "Subject";
        String input2 = "RealSubject";
        String input3 = "Proxy";
        String packageName = "com.StructuralDP.proxy";

//      Proxy interface declaration
        MethodSpec doOp = MethodSpec.methodBuilder("doOperation")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .returns(TypeName.VOID)
                .build();
        ClassName Subject = ClassName.get("",input1);
        TypeSpec subject = TypeSpec.interfaceBuilder(Subject)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Subject interface defines common interface for RealSubject and Proxy")
                .addMethod(doOp)
                .build();
        JavaFile class1 = JavaFile.builder(packageName,subject)
                .skipJavaLangImports(true)
                .build();

//        RealSubject class declaration
        ClassName RealSubject = ClassName.get("",input2);
        TypeSpec realSubject = TypeSpec.classBuilder(RealSubject)
                .addSuperinterface(Subject)
                .addJavadoc("RealSubject class is a real object which is represented by Proxy")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder(doOp.name)
                        .addModifiers(Modifier.PUBLIC)
                        .build())
                .build();
        JavaFile class2 = JavaFile.builder(packageName,realSubject)
                .skipJavaLangImports(true)
                .build();

//        Proxy class declaration
        ClassName Proxy = ClassName.get("",input3);
        TypeSpec proxy =  TypeSpec.classBuilder(Proxy)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Subject)
                .addJavadoc("""
                        Proxy class keep reference on a real subject, define interface which
                        represents Subject, so he can: - act as a surrogate - control access to real
                        subject - can be responsible for creation and maintenance of the real
                        subject
                        """)
                .addField(RealSubject, "realSubject",Modifier.PRIVATE)
                .addMethod(MethodSpec.methodBuilder(doOp.name)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("this.$N = new $T()","realSubject",RealSubject)
                        .addStatement("this.$N.$N()","realSubject",doOp.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder("getRealSubject")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(RealSubject)
                        .addStatement("return $N","realSubject")
                        .build())
                .build();
        JavaFile class3 = JavaFile.builder(packageName,proxy)
                .skipJavaLangImports(true)
                .build();

        System.out.println(class1);
        System.out.println(class2);
        System.out.println(class3);

    }
}
