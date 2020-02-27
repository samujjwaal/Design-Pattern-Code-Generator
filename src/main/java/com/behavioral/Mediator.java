package com.behavioral;

import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;
import java.io.IOException;

import com.DesignPattern;

public class Mediator implements DesignPattern {

    String[] defaultClasses = {"Colleague","ConcreteColleague1","ConcreteColleague2","Mediator","ConcreteMediator"};
    String packageName = "com.BehavioralDP.mediator";
    JavaFile[] generatedCode = new JavaFile[defaultClasses.length];

    public Mediator()throws IOException{
        createDesignPattern(defaultClasses,packageName);
    }

    public JavaFile[] generateCode(String[] classes, String packageName){
        int i = 0;

        ClassName Colleague =  ClassName.get("",classes[0]);
        ClassName ConcreteColleague1 =  ClassName.get("",classes[1]);
        ClassName ConcreteColleague2 =  ClassName.get("",classes[2]);
        ClassName Mediator =  ClassName.get("",classes[3]);
        ClassName ConcreteMediator =  ClassName.get("",classes[4]);

//        Colleague abstract class declaration
        FieldSpec mediatorField = FieldSpec.builder(Mediator,"mediator",Modifier.PROTECTED).build();
        FieldSpec recMsg = FieldSpec.builder(String.class,"receivedMessage",Modifier.PRIVATE).build();
        MethodSpec notifyCol = MethodSpec.methodBuilder("notifyColleague")
                .addModifiers(Modifier.ABSTRACT).returns(TypeName.VOID)
                .addParameter(String.class,"message")
                .build();
        MethodSpec receive = MethodSpec.methodBuilder("receive")
                .addModifiers(Modifier.ABSTRACT).returns(TypeName.VOID)
                .addParameter(String.class,"message")
                .build();
        MethodSpec getRecMsg = MethodSpec.methodBuilder("getReceivedMessage")
                .addModifiers(Modifier.PROTECTED).returns(String.class)
                .addStatement("return this.$N",recMsg.name)
                .build();
        MethodSpec setRecMsg = MethodSpec.methodBuilder("setReceivedMessage")
                .addModifiers(Modifier.PROTECTED).returns(TypeName.VOID)
                .addParameter(String.class,"receivedMessage")
                .addStatement("this.$N = receivedMessage",recMsg.name)
                .build();
        TypeSpec colleague = TypeSpec.classBuilder(Colleague)
                .addModifiers(Modifier.ABSTRACT)
                .addJavadoc("Colleague defines an interface for communication with another Colleague via mediator.")
                .addField(mediatorField)
                .addField(recMsg)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(Mediator,"mediator")
                        .addStatement("this.$N = mediator",mediatorField.name)
                        .build())
                .addMethod(notifyCol)
                .addMethod(receive)
                .addMethod(getRecMsg)
                .addMethod(setRecMsg)
                .build();
        generatedCode[i] = JavaFile.builder(packageName,colleague)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        ConcreteColleague1 class declaration
        TypeSpec concCol1 = TypeSpec.classBuilder(ConcreteColleague1)
                .addModifiers(Modifier.PUBLIC).superclass(Colleague)
                .addJavadoc("ConcreteColleague1 implements Colleague interface.")
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(Mediator, "mediator")
                        .addStatement("super(mediator)")
                        .build())
                .addMethod(MethodSpec.methodBuilder(notifyCol.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(String.class,"message")
                        .addStatement("this.$N.$N(this, message)",mediatorField.name,notifyCol.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder(receive.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(String.class,"message")
                        .addStatement("this.$N(message)",setRecMsg.name)
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concCol1)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        ConcreteColleague1 class declaration
        TypeSpec concCol2 = TypeSpec.classBuilder(ConcreteColleague2)
                .addModifiers(Modifier.PUBLIC).superclass(Colleague)
                .addJavadoc("ConcreteColleague2 implements Colleague interface.")
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(Mediator, "mediator")
                        .addStatement("super(mediator)")
                        .build())
                .addMethod(MethodSpec.methodBuilder(notifyCol.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(String.class,"message")
                        .addStatement("this.$N.$N(this, message)",mediatorField.name,notifyCol.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder(receive.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(String.class,"message")
                        .addStatement("this.$N(message)",setRecMsg.name)
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concCol2)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        Mediator interface declaration
        TypeSpec mediator = TypeSpec.interfaceBuilder(Mediator)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder(notifyCol.name)
                        .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT).returns(TypeName.VOID)
                        .addParameter(Colleague, "colleague")
                        .addParameter(String.class,"message")
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,mediator)
                .skipJavaLangImports(true)
                .build();
        i += 1;

//        ConcreteMediator class declaration
        ClassName list = ClassName.get("java.util", "List");
        ClassName arrayList = ClassName.get("java.util", "ArrayList");
        ClassName Iterator = ClassName.get("java.util","Iterator");
        TypeName listOfColleagues = ParameterizedTypeName.get(list,Colleague);

        FieldSpec cols = FieldSpec.builder(listOfColleagues,"colleagues",Modifier.PRIVATE)
                .build();
        TypeSpec concreteMediator = TypeSpec.classBuilder(ConcreteMediator)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Mediator)
                .addJavadoc("ConcreteMediator implements Mediator, coordinates between Colleague objects.")
                .addField(cols)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("$N = new $T<$T>()", cols.name,arrayList,Colleague)
                        .build())
                .addMethod(MethodSpec.methodBuilder("addColleague")
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(Colleague,"colleague")
                        .addStatement("$N.add(colleague)",cols.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder(notifyCol.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(Colleague, "colleague")
                        .addParameter(String.class,"message")
                        .addCode("\n")
                        .beginControlFlow("for ($T iterator = $N.iterator(); iterator.hasNext();)",Iterator,cols.name)
                        .addStatement("$T receiverColleague = ($T) iterator.next()",Colleague,Colleague)
                        .addCode("\n")
                        .beginControlFlow("if (colleague != receiverColleague)")
                        .addStatement("receiverColleague.$N(message)",receive.name)
                        .endControlFlow()
                        .endControlFlow()
                        .build())
                .build();
        generatedCode[i] = JavaFile.builder(packageName,concreteMediator)
                .skipJavaLangImports(true)
                .build();

        return generatedCode;


    }

}
