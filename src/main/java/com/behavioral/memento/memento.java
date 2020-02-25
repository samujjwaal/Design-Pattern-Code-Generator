package com.behavioral.memento;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

public class memento {
    public static void main(String[] args) {

        String input1 = "Memento";
        String input2 = "Caretaker";
        String input3 = "Originator";
        String packageName = "com.BehavioralDP.memento";

//      Memento class declaration
        ClassName Memento = ClassName.get("",input1);
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
                .addJavadoc("""
                        Memento stores internal state of the Originator object, protects against
                        access by objects other than the Originator.""")
                .addField(state)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(int.class,"state")
                        .addStatement("this.$N = $N",state.name,"state")
                        .build())
                .addMethod(getstate)
                .build();
        JavaFile class1 = JavaFile.builder(packageName, memento)
                .skipJavaLangImports(true)
                .build();

//        Caretaker class declaration
        ClassName Caretaker = ClassName.get("",input2);
        TypeSpec caretaker = TypeSpec.classBuilder(Caretaker)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("""
                        Caretaker responsible for the Memento's safekeeping.""")
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
        JavaFile class2 = JavaFile.builder(packageName,caretaker)
                .skipJavaLangImports(true)
                .build();

//        Originator class declaration
        ClassName Originator = ClassName.get("",input3);
        TypeSpec originator = TypeSpec.classBuilder(Originator)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("""
                        Originator creates a Memento containing a snapshot of its current internal
                        state. Originator use Memento to restore its internal state.""")
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
        JavaFile class3 = JavaFile.builder(packageName,originator)
                .skipJavaLangImports(true)
                .build();

        System.out.println(class1);
        System.out.println(class2);
        System.out.println(class3);
    }
}
