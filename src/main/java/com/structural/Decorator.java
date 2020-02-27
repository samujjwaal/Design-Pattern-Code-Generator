package com.structural;

import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import com.DesignPattern;

public class Decorator implements DesignPattern {

    String[] defaultClasses = {"Component","ConcreteComponent","Decorator","ConcreteDecoratorA","ConcreteDecoratorB"};
    String packageName = "com.StructuralDP.decorator";
    JavaFile[] generatedCode = new JavaFile[defaultClasses.length];

    public Decorator()throws IOException{
        createDesignPattern(defaultClasses,packageName);
    }

    public JavaFile[] generateCode(String[] classes, String packageName){
        int i = 0;

//        Component interface declaration
        ClassName Component = ClassName.get("",classes[i]);
        MethodSpec oper = MethodSpec.methodBuilder("operation")
                .addModifiers(Modifier.ABSTRACT,Modifier.PUBLIC).returns(TypeName.VOID)
                .build();
        TypeSpec component = TypeSpec.interfaceBuilder(Component)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Component, defines interface for new features which will be added dynamically")
                .addMethod(oper)
                .build();
        generatedCode[i] = JavaFile.builder(packageName,component)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        ConcreteComponent class declaration
        ClassName ConcreteComponent = ClassName.get("",classes[i]);
        TypeSpec concComponent = TypeSpec.classBuilder(ConcreteComponent)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Component)
                .addJavadoc("ConcreteComponent, define object where new features can be added")
                .addMethod(MethodSpec.methodBuilder(oper.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concComponent)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        Decorator abstract class declaration
        ClassName Decorator = ClassName.get("",classes[i]);
        FieldSpec compo = FieldSpec.builder(Component, "component",Modifier.PROTECTED).build();
        TypeSpec decorator = TypeSpec.classBuilder(Decorator)
                .addModifiers(Modifier.ABSTRACT)
                .addSuperinterface(Component)
                .addJavadoc("Decorator, keep reference to Component object")
                .addField(compo)
                .addMethod(MethodSpec.methodBuilder(oper.name)
                        .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT).returns(TypeName.VOID)
                        .build())
                .addMethod(MethodSpec.methodBuilder("setComponent")
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(Component, "component")
                        .addStatement("this.$N = component",compo.name)
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,decorator)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        ConcreteDecoratorA class declaration
        ClassName ConcreteDecoratorA = ClassName.get("",classes[i]);
        FieldSpec state = FieldSpec.builder(Boolean.TYPE, "state",Modifier.PRIVATE).build();
        TypeSpec concDecoA = TypeSpec.classBuilder(ConcreteDecoratorA)
                .addModifiers(Modifier.PUBLIC)
                .superclass(Decorator)
                .addJavadoc("ConcreteDecoratorA, add features to component")
                .addField(state)
                .addMethod(MethodSpec.methodBuilder(oper.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addStatement("$N = true",state.name)
                        .addStatement("this.$N.$N()",compo.name,oper.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder("isState")
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.BOOLEAN)
                        .addStatement("return $N",state.name)
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concDecoA)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        ConcreteDecoratorB class declaration
        ClassName ConcreteDecoratorB = ClassName.get("",classes[i]);
        FieldSpec behMethInvok = FieldSpec.builder(Boolean.TYPE, "behaviorMethodInvoked",Modifier.PRIVATE)
                .initializer("false").build();
        MethodSpec addBehav = MethodSpec.methodBuilder("addedBehavior")
                .addModifiers(Modifier.PRIVATE).returns(TypeName.VOID)
                .addStatement("$N = true", behMethInvok.name)
                .build();
        TypeSpec concDecoB = TypeSpec.classBuilder(ConcreteDecoratorB)
                .addModifiers(Modifier.PUBLIC)
                .superclass(Decorator)
                .addJavadoc("ConcreteDecoratorB, add features to component")
                .addField(behMethInvok)
                .addMethod(MethodSpec.methodBuilder(oper.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addStatement("this.$N.$N()",compo.name,oper.name)
                        .addStatement("$N()",addBehav.name)
                        .build())
                .addMethod(addBehav)
                .addMethod(MethodSpec.methodBuilder("isBehaviorMethodInvoked")
                        .addModifiers(Modifier.PROTECTED).returns(TypeName.BOOLEAN)
                        .addStatement("return $N", behMethInvok.name)
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concDecoB)
                .skipJavaLangImports(true)
                .build();

        return generatedCode;

    }

}
