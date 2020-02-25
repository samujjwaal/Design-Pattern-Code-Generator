package com.behavioral.observer;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

public class observer {
    public static void main(String[] args) {
        String input1 = "Observer";
        String input2 = "Subject";
        String input3 = "ConcreteSubject";
        String input4 = "ConcreteObserver";
        String packageName = "com.BehavioralDP.observer";

//      Observer interface declaration
        ClassName Observer = ClassName.get("",input1);
        MethodSpec update = MethodSpec.methodBuilder("update")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT).returns(TypeName.VOID)
                .build();
        TypeSpec component = TypeSpec.interfaceBuilder(Observer)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Observer defines an updating interface for objects that should be notified of changes in a subject.")
                .addMethod(update)
                .build();
        JavaFile class1 = JavaFile.builder(packageName,component)
                .skipJavaLangImports(true)
                .build();

//        Subject class declaration
        ClassName Subject = ClassName.get("",input2);
        ClassName list = ClassName.get("java.util", "List");
        ClassName arrayList = ClassName.get("java.util", "ArrayList");
        ClassName Iterator = ClassName.get("java.util","Iterator");
        TypeName listOfObservers = ParameterizedTypeName.get(list,Observer);

        FieldSpec obs = FieldSpec.builder(listOfObservers,"observers",Modifier.PRIVATE)
                .initializer("new $T<$T>()",arrayList,Observer)
                .build();
        MethodSpec notifyObs = MethodSpec.methodBuilder("notifyObservers")
                .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                .beginControlFlow("($T iterator = $N.iterator(); iterator.hasNext();)",Iterator,obs.name)
                .addStatement("$T observer = ($T) iterator.next()",Observer,Observer)
                .addStatement("observer.$N()",update.name)
                .endControlFlow()
                .build();
        TypeSpec subject = TypeSpec.classBuilder(Subject)
                .addModifiers(Modifier.ABSTRACT)
                .addJavadoc("Subject knows its observers. Any number of Observer objects may observe a subject.")
                .addField(obs)
                .addMethod(MethodSpec.methodBuilder("attach")
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(Observer,"observer")
                        .addStatement("$N.add(observer)",obs.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder("detach")
                .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                .addParameter(Observer,"observer")
                .addStatement("$N.remove(observer)",obs.name)
                .build())
                .addMethod(notifyObs)
                .build();
        JavaFile class2 = JavaFile.builder(packageName,subject)
                .skipJavaLangImports(true).build();

//        ConcreteSubject class declaration
        ClassName ConcreteSubject = ClassName.get("",input3);
        FieldSpec state = FieldSpec.builder(int.class,"state",Modifier.PRIVATE).build();
        MethodSpec getstate = MethodSpec.methodBuilder("getState")
                .addModifiers(Modifier.PUBLIC).returns(int.class)
                .addStatement("return $N", state.name)
                .build();
        TypeSpec concSub = TypeSpec.classBuilder(ConcreteSubject)
                .superclass(Subject)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("""
                ConcreteSubject stores state of interest to ConcreteObserver objects, sends a notification to its observers
                when its state changes.""")
                .addField(state)
                .addMethod(getstate)
                .addMethod(MethodSpec.methodBuilder("setState")
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(int.class, "state")
                        .addStatement("this.$N = state",state.name)
                        .addStatement("this.$N()",notifyObs.name)
                        .build())
                .build();
        JavaFile class3 = JavaFile.builder(packageName,concSub)
                .skipJavaLangImports(true).build();

//        ConcreteObserver class declaration
        ClassName ConcreteObserver = ClassName.get("",input4);
        FieldSpec obsState = FieldSpec.builder(int.class, "observerState",Modifier.PRIVATE).build();
        FieldSpec subjectField = FieldSpec.builder(ConcreteSubject, "subject",Modifier.PRIVATE).build();
        TypeSpec concObs = TypeSpec.classBuilder(ConcreteObserver)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Observer)
                .addJavadoc("""
                        ConcreteObserver maintains a reference to a ConcreteSubject object, stores
                        state that should stay consistent with the subject's, implements the Observer
                        updating interface to keep its state consistent with the subject's.""")
                .addField(obsState)
                .addField(subjectField)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ConcreteSubject, "subject")
                        .addStatement("this.$N = subject",subjectField.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder("update")
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addStatement("$N = subject.$N()",obsState.name,getstate.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder("getObserverState")
                        .addModifiers(Modifier.PROTECTED).returns(int.class)
                        .addStatement("return $N",obsState.name)
                        .build())
                .build();
        JavaFile class4 = JavaFile.builder(packageName,concObs)
                .skipJavaLangImports(true).build();


        System.out.println(class1);
        System.out.println(class2);
        System.out.println(class3);
        System.out.println(class4);

    }
}
