package com.creational;

import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import com.DesignPattern;

public class Prototype implements DesignPattern {

    String[] defaultClasses = {"Prototype","ConcretePrototype","Client"};
    String packageName = "com.CreationalDP.prototype";
    JavaFile[] generatedCode = new JavaFile[defaultClasses.length];

    public Prototype()throws IOException {
        createDesignPattern(defaultClasses,packageName);
    }

    @Override
    public JavaFile[] generateCode(String[] classes, String packageName){
        int i = 0;

//        Prototype abstract class declaration
        ClassName Prototype = ClassName.get("",classes[i]);
        MethodSpec copyMe = MethodSpec.methodBuilder("copyMe")
                .addModifiers(Modifier.ABSTRACT)
                .addException(CloneNotSupportedException.class)
                .addJavadoc("""
                        Copy method.
                        @return copy of the object
                        @throws CloneNotSupportedException exception""")
                .returns(Prototype)
                .build();
        TypeSpec prototype = TypeSpec.classBuilder(Prototype)
                .addModifiers(Modifier.ABSTRACT,Modifier.PUBLIC)
                .addSuperinterface(Cloneable.class)
                .addJavadoc("Declares interface to copy it self.")
                .addMethod(copyMe)
                .build();
        generatedCode[i] = JavaFile.builder(packageName,prototype)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        ConcretePrototype class declaration
        ClassName ConcretePrototype = ClassName.get("",classes[i]);
        TypeSpec concPrototype = TypeSpec.classBuilder(ConcretePrototype)
                .addModifiers(Modifier.PUBLIC)
                .superclass(Prototype)
                .addJavadoc("Declares interface to copy it self.")
                .addMethod(MethodSpec.methodBuilder(copyMe.name)
                        .addModifiers(Modifier.PUBLIC)
                        .addException(CloneNotSupportedException.class)
                        .returns(Prototype)
                        .addStatement("return ($T) this.clone()",Prototype)
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concPrototype)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        Client class declaration
        ClassName Client = ClassName.get("",classes[i]);
        FieldSpec proto = FieldSpec.builder(Prototype, "prototype",Modifier.PRIVATE).build();
        TypeSpec client = TypeSpec.classBuilder(Client)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Creates a new object by asking a Prototype to clone itself")
                .addField(proto)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(Prototype,"prototype")
                        .addStatement("this.$N = prototype",proto.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder("operation")
                        .addModifiers(Modifier.PUBLIC)
                        .addException(CloneNotSupportedException.class)
                        .returns(Prototype)
                        .addStatement("return $N.$N()",proto.name,copyMe.name)
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,client)
                .skipJavaLangImports(true)
                .build();

        return generatedCode;


    }
}
