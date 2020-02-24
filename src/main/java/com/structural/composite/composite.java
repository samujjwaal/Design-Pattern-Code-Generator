package com.structural.composite;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

public class composite {
    public static void main(String[] args) {

        String input1 = "Component";
        String input2 = "Composite";
        String packageName = "com.StructuralDP.composite";

//        Component interface declaration
        ClassName Component = ClassName.get("",input1);
        MethodSpec oper = MethodSpec.methodBuilder("operation")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .returns(TypeName.VOID)
                .build();
        TypeSpec component = TypeSpec.interfaceBuilder(Component)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Component declares the interface for objects in the composition")
                .addMethod(oper)
                .build();
        JavaFile class1 = JavaFile.builder(packageName,component)
                .skipJavaLangImports(true)
                .build();

//        Composite class declaration
        ClassName Composite = ClassName.get("",input2);
        ClassName list = ClassName.get("java.util", "List");
        ClassName arrayList = ClassName.get("java.util", "ArrayList");
        TypeName listOfComponents = ParameterizedTypeName.get(list,Component);

        FieldSpec child = FieldSpec.builder(listOfComponents,"children")
                .addModifiers(Modifier.PRIVATE)
                .initializer("new $T<$T>()",arrayList,Component)
                .build();
        TypeSpec composite =  TypeSpec.classBuilder(Composite)
                .superclass(Component)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("""
                        Composite class defines behavior for components having children, stores child
                        components, implements child-related operations in the Component interface""")
                .addField(child)
                .addMethod(MethodSpec.methodBuilder(oper.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .beginControlFlow("for($T component : $N)",Component,child.name)
                        .addStatement("component.$N()",oper.name)
                        .endControlFlow()
                        .build())
                .addMethod(MethodSpec.methodBuilder("add")
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(Component,"component")
                        .addStatement("$N.add(component)",child.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder("remove")
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(Component,"component")
                        .addStatement("$N.remove(component)",child.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder("getChild")
                        .addModifiers(Modifier.PUBLIC).returns(Component)
                        .addParameter(int.class,"index")
                        .addStatement("return $N.get(index)",child.name)
                        .build())
                .build();
        JavaFile class2 = JavaFile.builder(packageName,composite)
                .skipJavaLangImports(true)
                .build();

        System.out.println(class1);
        System.out.println(class2);

    }
}
