package com.behavioral.chainOfResponsibility;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

public class chainOfResponsibility {
    public static void main(String[] args) {

        String input1 = "Handler";
        String input2 = "ConcreteHandler1";
        String input3 = "ConcreteHandler2";
        String packageName = "com.BehavioralDP.chainOfResponsibility";

//        Handler class declaration
        ClassName Handler =  ClassName.get("",input1);
        MethodSpec handleReq = MethodSpec.methodBuilder("handleRequest")
                .addModifiers(Modifier.ABSTRACT)
                .returns(TypeName.VOID)
                .build();
        TypeSpec handler = TypeSpec.classBuilder(Handler)
                .addModifiers(Modifier.ABSTRACT)
                .addJavadoc("Handler interface, declares an interface for request handling")
                .addField(Handler,"successor",Modifier.PROTECTED)
                .addMethod(handleReq)
                .addMethod(MethodSpec.methodBuilder("setSuccessor")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.VOID)
                        .addParameter(Handler,"successor")
                        .addStatement("this.$N = $N","successor","successor")
                        .build())
                .build();
        JavaFile class1 = JavaFile.builder(packageName,handler)
                .skipJavaLangImports(true)
                .build();

//        ConcreteHandler1 class declaration
        ClassName ConcreteHandler1 = ClassName.get("",input2);
        FieldSpec handleReqInv = FieldSpec.builder(Boolean.TYPE,"handleRequestInvoked")
                .addModifiers(Modifier.PRIVATE)
                .initializer("false")
                .build();
        MethodSpec isHandleReqInv = MethodSpec.methodBuilder("isHandleRequestInvoked")
                .addModifiers(Modifier.PROTECTED)
                .returns(Boolean.TYPE)
                .addStatement("return $N",handleReqInv.name)
                .build();
        TypeSpec concHandler1 = TypeSpec.classBuilder(ConcreteHandler1)
                .superclass(Handler)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("""
                        ConcreteHandler1 class, handles the request, can access to the next object in
                        a chain and forward the request if necessary.""")
                .addField(handleReqInv)
                .addMethod(MethodSpec.methodBuilder(handleReq.name)
                        .returns(TypeName.VOID)
                        .addStatement("$N = true",handleReqInv.name)
                        .addCode("\n")
                        .addComment("if some condition call handleRequest on successor")
                        .beginControlFlow("if ($N)",handleReqInv.name)
                        .addStatement("$N.$N()","successor",handleReq.name)
                        .endControlFlow()
                        .build())
                .addMethod(isHandleReqInv)
                .build();
        JavaFile class2 = JavaFile.builder(packageName,concHandler1)
                .skipJavaLangImports(true)
                .build();

//        ConcreteHandler2 class declaration
        ClassName ConcreteHandler2 = ClassName.get("",input3);
        TypeSpec concHandler2 = TypeSpec.classBuilder(ConcreteHandler2)
                .superclass(Handler)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("""
                        ConcreteHandler2 class, handles the request, can access to the next object in
                        a chain and forward the request if necessary.""")
                .addField(handleReqInv)
                .addMethod(MethodSpec.methodBuilder(handleReq.name)
                        .returns(TypeName.VOID)
                        .addStatement("$N = true",handleReqInv.name)
                        .build())
                .addMethod(isHandleReqInv)
                .build();
        JavaFile class3 = JavaFile.builder(packageName,concHandler2)
                .skipJavaLangImports(true)
                .build();

        System.out.println(class1);
        System.out.println(class2);
        System.out.println(class3);

    }
}
