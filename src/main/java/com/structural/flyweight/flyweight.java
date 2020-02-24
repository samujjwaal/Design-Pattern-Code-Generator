package com.structural.flyweight;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

public class flyweight {
    public static void main(String[] args) {
        String input1 = "Flyweight";
        String input2 = "ConcreteFlyweight";
        String input3 = "UnsharedConcreteFlyweight";
        String input4 = "FlyweightFactory";
        String packageName = "com.StructuralDP.flyweight";

//        Flyweight interface declaration
        ClassName Flyweight = ClassName.get("",input1);
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
        JavaFile class1 = JavaFile.builder(packageName,flyweight)
                .skipJavaLangImports(true)
                .build();

//        ConcreteFlyweight class declaration
        ClassName ConcreteFlyweight = ClassName.get("",input2);
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
        JavaFile class2 = JavaFile.builder(packageName,concreteFlyweight)
                .skipJavaLangImports(true)
                .build();


//        UnsharedConcreteFlyweight class declaration
        ClassName UnsharedConcreteFlyweight = ClassName.get("",input3);
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
        JavaFile class3 = JavaFile.builder(packageName,unsharedConcreteFlyweight)
                .skipJavaLangImports(true)
                .build();

//        FlyweightFactory class declaration
        ClassName FlyweightFactory = ClassName.get("",input4);
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
                        .addJavadoc("""
                                Returns Flyweight object. Just for sake of example following logic is
                                applied, if key starts with phrase:unshared than UnsharedConcreteFlyweight
                                object is created. Otherwise ConcreteFlyweight object is created.
                                @param key
                                @return Flyweight""")
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
        JavaFile class4 = JavaFile.builder(packageName,flyweightFact)
                .skipJavaLangImports(true)
                .build();

        System.out.println(class1);
        System.out.println(class2);
        System.out.println(class3);
        System.out.println(class4);
    }
}
