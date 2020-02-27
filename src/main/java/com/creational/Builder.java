package com.creational;

import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import com.DesignPattern;



public class Builder implements DesignPattern {
    String[] defaultClasses = {"Builder", "Director","Product","concreteBuilder"};
    String packageName = "com.CreationalDP.builder";
    JavaFile[] generatedCode = new JavaFile[defaultClasses.length];

    public Builder()throws IOException{
        createDesignPattern(defaultClasses,packageName);
    }

    @Override
    public JavaFile[] generateCode(String[] classes, String packageName){
        int i = 0;

//        Abstract Builder class declaration
        ClassName Builder = ClassName.get("",classes[i]);

        MethodSpec createProduct = MethodSpec.methodBuilder("createProduct")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .returns(Builder)
                .build();

        MethodSpec buildPart1 = MethodSpec.methodBuilder("buildPart1")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .returns(Builder)
                .addParameter(String.class, "part")
                .build();

        MethodSpec buildPart2 = MethodSpec.methodBuilder("buildPart2")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .returns(Builder)
                .addParameter(String.class, "part")
                .build();

        TypeSpec builder = TypeSpec.classBuilder(Builder)
                .addModifiers(Modifier.ABSTRACT)
                .addJavadoc("Builder, declares interface for creating parts of a Product object")
                .addMethod(createProduct)
                .addMethod(buildPart1)
                .addMethod(buildPart2)
                .build();

        generatedCode[i] = JavaFile.builder(packageName,builder)
                .addFileComment("Builder, declares interface for creating parts of a Product object")
                .skipJavaLangImports(true)
                .build();

        i += 1;
//        Director class declaration

        ClassName Director = ClassName.get("",classes[i]);

        MethodSpec constructorDirector = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Builder,"builder")
                .addStatement("this.builder = builder")
                .build();

        MethodSpec construct = MethodSpec.methodBuilder("construct")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addStatement("builder.$N().$N($S).$N($S)",createProduct.name,buildPart1.name,"part1",buildPart2.name,"part2")
                .build();

        TypeSpec director = TypeSpec.classBuilder(Director)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Director class, constructs an object using the Builder interface")
                .addField(Builder,"builder",Modifier.PRIVATE)
                .addMethod(constructorDirector)
                .addMethod(construct)
                .build();

        generatedCode[i] = JavaFile.builder(packageName,director)
                .skipJavaLangImports(true)
                .build();

        i += 1;
//        Product class declaration

        ClassName Product = ClassName.get("",classes[i]);

        MethodSpec set1 = MethodSpec.methodBuilder("setPart1")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(String.class,"part1")
                .addStatement("this.$N() = $N","part1","part1")
                .build();

        MethodSpec set2 = MethodSpec.methodBuilder("setPart2")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(String.class,"part2")
                .addStatement("this.$N() = $N","part2","part2")
                .build();

        MethodSpec get1 = MethodSpec.methodBuilder("getPart1")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addStatement("return $N","part1")
                .build();

        MethodSpec get2 = MethodSpec.methodBuilder("getPart2")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addStatement("return $N","part2")
                .build();

        TypeSpec product = TypeSpec.classBuilder(Product)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Product class, represents complex object")
                .addField(String.class, "part1", Modifier.PRIVATE)
                .addField(String.class, "part2", Modifier.PRIVATE)
                .addMethod(set1)
                .addMethod(set2)
                .addMethod(get1)
                .addMethod(get2)
                .build();

        generatedCode[i] = JavaFile.builder(packageName,product)
                .skipJavaLangImports(true)
                .build();

        i += 1;
//        Concrete Builder class declaration

        ClassName concreteBuilder = ClassName.get("",classes[i]);

        MethodSpec createproduct = MethodSpec.methodBuilder("createProduct")
                .addModifiers(Modifier.PUBLIC)
                .returns(Builder)
                .addStatement("this.product = new $T()",Product)
                .addStatement("return this")
                .build();

        MethodSpec buildpart1 = MethodSpec.methodBuilder("buildPart1")
                .addModifiers(Modifier.PUBLIC)
                .returns(Builder)
                .addParameter(String.class,"part" )
                .addStatement("product.$N($N)",set1.name,"part")
                .addStatement("return this")
                .build();

        MethodSpec buildpart2 = MethodSpec.methodBuilder("buildPart2")
                .addModifiers(Modifier.PUBLIC)
                .returns(Builder)
                .addParameter(String.class,"part" )
                .addStatement("product.$N($N)",set2.name,"part")
                .addStatement("return this")
                .build();

        MethodSpec getresult = MethodSpec.methodBuilder("getResult")
                .addModifiers(Modifier.PUBLIC)
                .returns(Product)
                .addStatement("return product")
                .build();

        TypeSpec concretebuilder = TypeSpec.classBuilder(concreteBuilder)
                .superclass(Builder)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("""
                        ConcreteBuilder class, constructs and assembles parts of the Product by
                        implementing the Builder interface""")
                .addField(Product, "product", Modifier.PRIVATE)
                .addMethod(createproduct)
                .addMethod(buildpart1)
                .addMethod(buildpart2)
                .addMethod(getresult)
                .build();

        generatedCode[i] = JavaFile.builder(packageName,concretebuilder)
                .skipJavaLangImports(true)
                .build();

        return generatedCode;
    }
}
