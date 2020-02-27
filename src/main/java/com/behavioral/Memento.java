package com.behavioral;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import com.DesignPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Memento implements DesignPattern {

    //Define a static logger variable so that it references the Logger instance
    private static final Logger logger = LoggerFactory.getLogger(Memento.class);


    String[] defaultClasses = {"Memento", "Caretaker","Originator"};
    String packageName = "com.BehavioralDP.memento";
    JavaFile[] generatedCode = new JavaFile[defaultClasses.length];

    public Memento()throws IOException{
        logger.info("Executing Memento()");
        createDesignPattern(defaultClasses,packageName);
    }

    public JavaFile[] generateCode(String[] classes, String packageName){
        logger.info("Executing generateCode()");

        int i = 0;

//      Memento class declaration
        ClassName Memento = ClassName.get("",classes[i]);
        FieldSpec state = FieldSpec.builder(int.class,"state")
                .addModifiers(Modifier.PRIVATE)
                .build();
        MethodSpec getstate = MethodSpec.methodBuilder("getState")
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addStatement("return $N", "state")
                .build();
        TypeSpec memento = TypeSpec.classBuilder(Memento)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Memento stores internal state of the Originator object, protects against\n" +
                            "access by objects other than the Originator.")
                .addField(state)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(int.class,"state")
                        .addStatement("this.$N = $N",state.name,"state")
                        .build())
                .addMethod(getstate)
                .build();
        generatedCode[i] = JavaFile.builder(packageName, memento)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        Caretaker class declaration
        ClassName Caretaker = ClassName.get("",classes[i]);
        TypeSpec caretaker = TypeSpec.classBuilder(Caretaker)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Caretaker responsible for the Memento's safekeeping.")
                .addField(Memento, "memento",Modifier.PRIVATE)
                .addMethod(MethodSpec.methodBuilder("getMemento")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(Memento)
                        .addStatement("return $N","memento")
                        .build())
                .addMethod(MethodSpec.methodBuilder("setMemento")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.VOID)
                        .addParameter(Memento, "memento")
                        .addStatement("this.$N = memento","memento")
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,caretaker)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        Originator class declaration
        ClassName Originator = ClassName.get("",classes[i]);
        TypeSpec originator = TypeSpec.classBuilder(Originator)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Originator creates a Memento containing a snapshot of its current internal\n" +
                            "state. Originator use Memento to restore its internal state.")
                .addField(state)
                .addMethod(MethodSpec.methodBuilder("setMemento")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.VOID)
                        .addParameter(Memento, "memento")
                        .addStatement("this.$N = $N.$N()",state.name,"memento",getstate.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder("createMemento")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(Memento)
                        .addStatement("return new $T(this.$N)",Memento,state.name)
                        .build())
                .addMethod(getstate)
                .addMethod(MethodSpec.methodBuilder("setState")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.VOID)
                        .addParameter(int.class, "state")
                        .addStatement("this.$N = $N",state.name,"state")
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,originator)
                .skipJavaLangImports(true)
                .build();

        logger.info("Returning generated java code to be written in files");

        return generatedCode;
    }
}
