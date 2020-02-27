package com.behavioral;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import com.DesignPattern;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class Strategy implements DesignPattern {

    //Define a static logger variable so that it references the Logger instance
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Strategy.class);


    String[] defaultClasses = {"Strategy", "Context","ConcreteStrategyA","ConcreteStrategyB","ConcreteStrategyC"};
    String packageName = "com.BehavioralDP.strategy";
    JavaFile[] generatedCode = new JavaFile[defaultClasses.length];

    public Strategy(int flag)throws IOException{
        logger.info("Executing Strategy()");
        if(flag == 1) {
            createDesignPattern(defaultClasses, packageName);
        }
    }

    public JavaFile[] generateCode(String[] classes, String packageName){
        logger.info("Executing generateCode()");

        int i = 0;

//        Strategy interface declaration
        ClassName Strategy = ClassName.get("",classes[i]);
        MethodSpec algoInterface = MethodSpec.methodBuilder("algorithmInterface")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .returns(String.class)
                .build();
        TypeSpec strategy = TypeSpec.interfaceBuilder(Strategy)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Declares an interface common to all supported algorithms. Context uses this interface to call the algorithm\n" +
                            "defined by a ConcreteStrategy.")
                .addMethod(algoInterface)
                .build();
        generatedCode[i] = JavaFile.builder(packageName,strategy)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        Context class declaration
        ClassName Context = ClassName.get("",classes[i]);
        FieldSpec strategyField = FieldSpec.builder(Strategy,"strategy",Modifier.PRIVATE).build();
        TypeSpec context = TypeSpec.classBuilder(Context)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc(" Maintains a reference to a Strategy object. Invokes algorithm implemented in ConcreteStrategy.")
                .addField(strategyField)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(Strategy, "strategy")
                        .addStatement("this.$N = strategy",strategyField.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder("contextInterface")
                        .addModifiers(Modifier.PROTECTED).returns(String.class)
                        .addStatement("return this.$N.$N()",strategyField.name,algoInterface.name)
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,context)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        ConcreteStrategyA class declaration
        ClassName ConcreteStrategyA = ClassName.get("",classes[i]);
        TypeSpec concStratA = TypeSpec.classBuilder(ConcreteStrategyA)
                .addSuperinterface(Strategy)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Implements the algorithm defined in Strategy interface.")
                .addMethod(MethodSpec.methodBuilder(algoInterface.name)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addStatement("return $S","Inside ConcreteStrategyA to invoke Algorithm A")
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concStratA)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        ConcreteStrategyB class declaration
        ClassName ConcreteStrategyB = ClassName.get("",classes[i]);
        TypeSpec concStratB = TypeSpec.classBuilder(ConcreteStrategyB)
                .addSuperinterface(Strategy)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Implements the algorithm defined in Strategy interface.")
                .addMethod(MethodSpec.methodBuilder(algoInterface.name)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addStatement("return $S","Inside ConcreteStrategyB to invoke Algorithm B")
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concStratB)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        ConcreteStrategyC class declaration
        ClassName ConcreteStrategyC = ClassName.get("",classes[i]);
        TypeSpec concStratC = TypeSpec.classBuilder(ConcreteStrategyC)
                .addSuperinterface(Strategy)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Implements the algorithm defined in Strategy interface.")
                .addMethod(MethodSpec.methodBuilder(algoInterface.name)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addStatement("return $S","Inside ConcreteStrategyC to invoke Algorithm C")
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concStratC)
                .skipJavaLangImports(true)
                .build();

        logger.info("Returning generated java code to be written in files");

        return generatedCode;
    }
}
