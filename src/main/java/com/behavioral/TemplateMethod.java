package com.behavioral;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import com.DesignPattern;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class TemplateMethod implements DesignPattern {

    //Define a static logger variable so that it references the Logger instance
    private static final Logger logger = (Logger) LoggerFactory.getLogger(TemplateMethod.class);


    String[] defaultClasses = {"AbstractClass", "ConcreteClass"};
    String packageName = "com.BehavioralDP.templateMethod";
    JavaFile[] generatedCode = new JavaFile[defaultClasses.length];

    public TemplateMethod(int flag)throws IOException{
        logger.info("Executing TemplateMethod()");
        if(flag == 1) {
            createDesignPattern(defaultClasses, packageName);
        }
    }

    public JavaFile[] generateCode(String[] classes, String packageName){
        logger.info("Executing generateCode()");

        int i = 0;

        ClassName abstractclass = ClassName.get("",classes[i]);

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
                        .addJavadoc("Template method, implementation of algorithm which consists of\n" +
                                    "primitiveOperations\n" +
                                    "\n" +
                                    "@return result of the primitive operations")
                        .addStatement("return this.$N() + this.$N()",primOp1.name,primOp2.name)
                        .build())
                .addMethod(primOp1)
                .addMethod(primOp2)
                .build();
        generatedCode[i] = JavaFile.builder(packageName,abstractC)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        ConcreteClass declaration
        ClassName concreteclass = ClassName.get("",classes[i]);
        TypeSpec concreteC = TypeSpec.classBuilder(concreteclass)
                .superclass(abstractclass)
                .addJavadoc("Implements the primitive operations to carry out subclass-specific steps of\n" +
                            "the algorithm.")
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
        generatedCode[i] = JavaFile.builder(packageName,concreteC)
                .skipJavaLangImports(true)
                .build();

        logger.info("Returning generated java code to be written in files");

        return generatedCode;

    }
}
