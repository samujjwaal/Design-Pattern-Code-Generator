package com.creational;

import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import com.DesignPattern;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class Prototype implements DesignPattern {

    //Define a static logger variable so that it references the Logger instance
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Prototype.class);

    String[] defaultClasses = {"Prototype","ConcretePrototype","Client"};
    String packageName = "com.CreationalDP.prototype";
    JavaFile[] generatedCode = new JavaFile[defaultClasses.length];

    public Prototype(int flag)throws IOException {
        logger.info("Executing Prototype()");

        if (flag == 1) {
            createDesignPattern(defaultClasses, packageName);
        }
    }

    @Override
    public JavaFile[] generateCode(String[] classes, String packageName){
        logger.info("Executing generateCode()");
        int i = 0;

//        Prototype abstract class declaration
        ClassName Prototype = ClassName.get("",classes[i]);
        MethodSpec copyMe = MethodSpec.methodBuilder("copyMe")
                .addModifiers(Modifier.ABSTRACT)
                .addException(CloneNotSupportedException.class)
                .addJavadoc("Copy method.\n" +
                            "@return copy of the object\n" +
                            "@throws CloneNotSupportedException exception")
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

        logger.info("Returning generated java code to be written in files");

        return generatedCode;


    }
}
