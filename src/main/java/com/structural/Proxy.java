package com.structural;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import com.DesignPattern;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class Proxy implements DesignPattern {

    //Define a static logger variable so that it references the Logger instance
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Proxy.class);

    String[] defaultClasses = {"Subject", "RealSubject","Proxy"};
    String packageName = "com.StructuralDP.proxy";
    JavaFile[] generatedCode = new JavaFile[defaultClasses.length];

    public Proxy(int flag)throws IOException{
        logger.info("Executing Proxy()");
        if(flag == 1) {
            createDesignPattern(defaultClasses, packageName);
        }
    }

    public JavaFile[] generateCode(String[] classes, String packageName){
        logger.info("Executing generateCode()");

        int i = 0;

//      Proxy interface declaration
        MethodSpec doOp = MethodSpec.methodBuilder("doOperation")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .returns(TypeName.VOID)
                .build();
        ClassName Subject = ClassName.get("",classes[i]);
        TypeSpec subject = TypeSpec.interfaceBuilder(Subject)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Subject interface defines common interface for RealSubject and Proxy")
                .addMethod(doOp)
                .build();
        generatedCode[i] = JavaFile.builder(packageName,subject)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        RealSubject class declaration
        ClassName RealSubject = ClassName.get("",classes[i]);
        TypeSpec realSubject = TypeSpec.classBuilder(RealSubject)
                .addSuperinterface(Subject)
                .addJavadoc("RealSubject class is a real object which is represented by Proxy")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder(doOp.name)
                        .addModifiers(Modifier.PUBLIC)
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,realSubject)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        Proxy class declaration
        ClassName Proxy = ClassName.get("",classes[i]);
        TypeSpec proxy =  TypeSpec.classBuilder(Proxy)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Subject)
                .addJavadoc("Proxy class keep reference on a real subject, define interface which\n" +
                            "represents Subject, so he can: - act as a surrogate - control access to real\n" +
                            "subject - can be responsible for creation and maintenance of the real\n" +
                            "subject\n")
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
        generatedCode[i] = JavaFile.builder(packageName,proxy)
                .skipJavaLangImports(true)
                .build();

        logger.info("Returning generated java code to be written in files");

        return generatedCode;

    }
}
