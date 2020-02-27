package com.structural;


import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import com.DesignPattern;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class Flyweight implements DesignPattern {
    //Define a static logger variable so that it references the Logger instance
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Flyweight.class);


    String[] defaultClasses = {"Flyweight", "ConcreteFlyweight","UnsharedConcreteFlyweight","FlyweightFactory"};
    String packageName = "com.StructuralDP.flyweight";
    JavaFile[] generatedCode = new JavaFile[defaultClasses.length];

    public Flyweight(int flag) throws IOException {
        logger.info("Executing Flyweight()");
        if(flag == 1) {
            createDesignPattern(defaultClasses,packageName);
        }

    }

    public void create() throws IOException {
        createDesignPattern(defaultClasses,packageName);
    }
    public JavaFile[] generateCode(String[] classes, String packageName){
        logger.info("Executing generateCode()");

        int i = 0;

//        Flyweight interface declaration
        ClassName Flyweight = ClassName.get("",classes[i]);
        MethodSpec oper = MethodSpec.methodBuilder("operation")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .returns(TypeName.VOID)
                .addParameter(TypeName.OBJECT, "extrinsicState")
                .build();
        TypeSpec flyweight = TypeSpec.interfaceBuilder(Flyweight)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Flyweight, interface through flyweight can receive and act on extrinsic state.")
                .addMethod(oper)
                .build();
        generatedCode[i] = JavaFile.builder(packageName,flyweight)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        ConcreteFlyweight class declaration
        ClassName ConcreteFlyweight = ClassName.get("",classes[i]);
        FieldSpec intrinsicState = FieldSpec.builder(TypeName.OBJECT,"intrinsicState",Modifier.PRIVATE).build();
        TypeSpec concreteFlyweight = TypeSpec.classBuilder(ConcreteFlyweight)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Flyweight)
                .addJavadoc("ConcreteFlyweight,implements Flyweight, and add storage for intrinsic state.")
                .addField(intrinsicState)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(TypeName.OBJECT, "intrinsicState")
                        .addStatement("this.$N = intrinsicState", intrinsicState.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder(oper.name)
                        .addComment("Using extrinsicState as context and does NOT modify intrinsic state")
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(TypeName.OBJECT, "extrinsicState")
                        .build())
                .addMethod(MethodSpec.methodBuilder("getIntrinsicState")
                        .addJavadoc("@return intrinsic state")
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.OBJECT)
                        .addStatement("return $N", intrinsicState.name)
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concreteFlyweight)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        UnsharedConcreteFlyweight class declaration
        ClassName UnsharedConcreteFlyweight = ClassName.get("",classes[i]);
        FieldSpec state = FieldSpec.builder(TypeName.OBJECT,"state",Modifier.PRIVATE).build();
        TypeSpec unsharedConcreteFlyweight = TypeSpec.classBuilder(UnsharedConcreteFlyweight)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Flyweight)
                .addJavadoc("UnsharedConcreteFlyweight, defines objects which are not shared.")
                .addField(state)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(TypeName.OBJECT, "state")
                        .addStatement("this.$N = state", state.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder(oper.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(TypeName.OBJECT, "extrinsicState")
                        .build())
                .addMethod(MethodSpec.methodBuilder("getState")
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.OBJECT)
                        .addStatement("return $N", state.name)
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,unsharedConcreteFlyweight)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        FlyweightFactory class declaration
        ClassName FlyweightFactory = ClassName.get("",classes[i]);
        ClassName hashMap = ClassName.get("java.util", "HashMap");
        ClassName map = ClassName.get("java.util", "Map");
        TypeName listOfFlyweights = ParameterizedTypeName.get(map,Flyweight);
//        TypeName listOfFlyweights = ParameterizedTypeName.get(map,TypeName.get(String.class.getComponentType()));
//        TypeName f = ParameterizedTypeName.get(String.class,listOfFlyweights );

        FieldSpec flyweights = FieldSpec.builder(listOfFlyweights,"flyweights")
                .addModifiers(Modifier.PRIVATE,Modifier.STATIC)
                .initializer("new $T<$T, $T>()",hashMap,String.class,Flyweight)
                .build();
        TypeSpec flyweightFact = TypeSpec.classBuilder(FlyweightFactory)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("FlyweightFactory, creates and manages the flyweight objects.")
                .addField(flyweights)
                .addMethod(MethodSpec.methodBuilder("getFlyweight")
                        .addModifiers(Modifier.PUBLIC,Modifier.STATIC).returns(Flyweight)
                        .addParameter(String.class,"key")
                        .addParameter(String.class,"value")
                        .addJavadoc("Returns Flyweight object. Just for sake of example following logic is\n" +
                                    "applied, if key starts with phrase:unshared than UnsharedConcreteFlyweight\n" +
                                    "object is created. Otherwise ConcreteFlyweight object is created.\n" +
                                    "@param key\n" +
                                    "@return Flyweight")
                        .beginControlFlow("if (key.startsWith(\"unshared\"))")
                        .addStatement("$N.put(key, new $T(value))",flyweights.name,UnsharedConcreteFlyweight)
                        .nextControlFlow("else { \n" +
                                "if (!$N.containsKey(key))",flyweights.name)
                        .addStatement("$N.put(key, new $T(value))",flyweights.name,ConcreteFlyweight)
                        .addCode("}\n")
                        .endControlFlow()
                        .addStatement("return ($T) $N.get(key)",Flyweight,flyweights.name)
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,flyweightFact)
                .skipJavaLangImports(true)
                .build();

        logger.info("Returning generated java code to be written in files");

        return generatedCode;
    }
}
