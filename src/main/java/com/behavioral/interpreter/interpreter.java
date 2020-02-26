package com.behavioral.interpreter;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

public class interpreter {
    public static void main(String[] args) {
        String input1 = "Context";
        String input2 = "AbstractExpression";
        String input3 = "OrExpression";
        String input4 = "AndExpression";
        String input5 = "TerminalExpression";

        String packageName = "com.BehavioralDP.interpreter";

//        Context class declaration
        ClassName Context = ClassName.get("",input1);
        ClassName list = ClassName.get("java.util", "List");
        ClassName arrayList = ClassName.get("java.util", "ArrayList");
        ClassName Boolean = ClassName.get("java.lang","Boolean");
        TypeName listOfBoolean = ParameterizedTypeName.get(list,Boolean);

        FieldSpec operands = FieldSpec.builder(listOfBoolean,"operands",Modifier.PRIVATE)
                .initializer("new $T<$T>()",arrayList,Boolean.class)
                .build();
        FieldSpec result = FieldSpec.builder(Boolean.class, "result",Modifier.PRIVATE)
                .initializer("null").build();

        MethodSpec getOperands = MethodSpec.methodBuilder("getOperands")
                .addModifiers(Modifier.PUBLIC).returns(listOfBoolean)
                .addStatement("return $N",operands.name).build();
        MethodSpec addOperand = MethodSpec.methodBuilder("addOperand")
                .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                .addParameter(Boolean.class, "operand")
                .addStatement("$N.add(operand)",operands.name).build();

        TypeSpec context = TypeSpec.classBuilder(Context)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Context contains global information for Interpreter.")
                .addField(operands)
                .addField(result)
                .addMethod(getOperands)
                .addMethod(addOperand)
                .addMethod(MethodSpec.methodBuilder("isResult")
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.BOOLEAN)
                        .addStatement("return $N",result.name).build())
                .addMethod(MethodSpec.methodBuilder("setResult")
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(TypeName.BOOLEAN, "result")
                        .addStatement("this.$N = result",result.name).build())
                .build();
        JavaFile class1 = JavaFile.builder(packageName,context).skipJavaLangImports(true).build();



//        AbstractExpression class declaration
        ClassName AbstractExpression = ClassName.get("",input2);
        MethodSpec interpret = MethodSpec.methodBuilder("interpret")
                .addModifiers(Modifier.ABSTRACT).returns(TypeName.VOID)
                .addParameter(Context, "context")
                .build();

        TypeSpec abstractExp = TypeSpec.classBuilder(AbstractExpression)
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addJavadoc("AbstractExpresion defines interface for interpretation. Interface must be used by" +
                        " TerminalExpression and NonTerminalExpression.")
                .addMethod(interpret).build();
        JavaFile class2 = JavaFile.builder(packageName,abstractExp).skipJavaLangImports(true).build();

//      OrExpression class declaration
        ClassName OrExpression = ClassName.get("",input3);
        FieldSpec firstAbsExp = FieldSpec.builder(AbstractExpression, "firstAbstractExpression",Modifier.PRIVATE).build();
        FieldSpec secondAbsExp = FieldSpec.builder(AbstractExpression, "secondAbstractExpression",Modifier.PRIVATE).build();
        TypeSpec orExp = TypeSpec.classBuilder(OrExpression)
                .superclass(AbstractExpression)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("OrExpression implements AbstractExpression for logical OR grammar expression")
                .addField(firstAbsExp)
                .addField(secondAbsExp)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(AbstractExpression, "firstAbstractExpression")
                        .addParameter(AbstractExpression, "secondAbstractExpression")
                        .addStatement("this.$N = firstAbstractExpression",firstAbsExp)
                        .addStatement("this.$N = secondAbstractExpression",secondAbsExp)
                        .build())
                .addMethod(MethodSpec.methodBuilder(interpret.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(Context, "context")
                        .addStatement("$N.$N(context)",firstAbsExp.name,interpret.name)
                        .addStatement("$N.$N(context)",secondAbsExp.name,interpret.name)
                        .addCode("\n")
                        .addStatement("$T $N = context.$N()",listOfBoolean,operands.name,getOperands.name)
                        .addCode("\n")
                        .addStatement("Boolean firstOperand = operands.get(0)")
                        .addStatement("Boolean secondOperand = operands.get(1)")
                        .addCode("\n")
                        .addStatement("$T $N = firstOperand || secondOperand",Boolean,result.name)
                        .addStatement("$N.setResult($N)",context.name,result.name)
                        .build())
                .build();
        JavaFile class3 = JavaFile.builder(packageName,orExp).skipJavaLangImports(true).build();

//      OrExpression class declaration
        ClassName AndExpression = ClassName.get("",input4);
        TypeSpec andExp = TypeSpec.classBuilder(AndExpression)
                .superclass(AbstractExpression)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("AndExpression implements AbstractExpression for logical AND grammar expression")
                .addField(firstAbsExp)
                .addField(secondAbsExp)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(AbstractExpression, "firstAbstractExpression")
                        .addParameter(AbstractExpression, "secondAbstractExpression")
                        .addStatement("this.$N = firstAbstractExpression",firstAbsExp)
                        .addStatement("this.$N = secondAbstractExpression",secondAbsExp)
                        .build())
                .addMethod(MethodSpec.methodBuilder(interpret.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(Context, "context")
                        .addStatement("$N.$N(context)",firstAbsExp.name,interpret.name)
                        .addStatement("$N.$N(context)",secondAbsExp.name,interpret.name)
                        .addCode("\n")
                        .addStatement("$T $N = context.$N()",listOfBoolean,operands.name,getOperands.name)
                        .addCode("\n")
                        .addStatement("Boolean firstOperand = operands.get(0)")
                        .addStatement("Boolean secondOperand = operands.get(1)")
                        .addCode("\n")
                        .addStatement("$T $N = firstOperand && secondOperand",Boolean,result.name)
                        .addStatement("$N.setResult($N)",context.name,result.name)
                        .build())
                .build();
        JavaFile class4 = JavaFile.builder(packageName,andExp).skipJavaLangImports(true).build();

//      TerminalExpression class declaration
        ClassName TerminalExpression = ClassName.get("",input5);
        FieldSpec data = FieldSpec.builder(boolean.class,"data",Modifier.PRIVATE).build();
        TypeSpec termExp = TypeSpec.classBuilder(TerminalExpression)
                .superclass(AbstractExpression)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("TerminalExpresion implements AbstractExpression for literal symbol in grammar.")
                .addField(data)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(boolean.class,"data")
                        .addStatement("this.$N = data",data.name)
                        .build())
                .addMethod(MethodSpec.methodBuilder(interpret.name)
                        .addModifiers(Modifier.PUBLIC).returns(TypeName.VOID)
                        .addParameter(Context,"context")
                        .addStatement("context.$N(this.$N)",addOperand.name,data.name)
                        .build())
                .build();
        JavaFile class5 = JavaFile.builder(packageName,termExp).skipJavaLangImports(true).build();


        System.out.println(class1);
        System.out.println(class2 );
        System.out.println(class3);
        System.out.println(class4);
        System.out.println(class5);
    }
}
