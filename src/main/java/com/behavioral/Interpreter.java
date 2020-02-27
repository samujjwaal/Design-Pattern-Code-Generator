package com.behavioral;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import com.DesignPattern;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class Interpreter implements DesignPattern {
    //Define a static logger variable so that it references the Logger instance
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Interpreter.class);


    String[] defaultClasses = {"Context", "AbstractExpression","OrExpression","AndExpression","TerminalExpression"};
    String packageName = "com.BehavioralDP.interpreter";
    JavaFile[] generatedCode = new JavaFile[defaultClasses.length];

    public Interpreter(int flag)throws IOException{
        logger.info("Executing Interpreter()");
        if(flag == 1) {
            createDesignPattern(defaultClasses, packageName);
        }
    }

    public JavaFile[] generateCode(String[] classes, String packageName){
        logger.info("Executing generateCode()");

        int i = 0;

//        Context class declaration
        ClassName Context = ClassName.get("",classes[i]);
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
        generatedCode[i] = JavaFile.builder(packageName,context).skipJavaLangImports(true).build();
        i += 1;

//        AbstractExpression class declaration
        ClassName AbstractExpression = ClassName.get("",classes[i]);
        MethodSpec interpret = MethodSpec.methodBuilder("interpret")
                .addModifiers(Modifier.ABSTRACT).returns(TypeName.VOID)
                .addParameter(Context, "context")
                .build();

        TypeSpec abstractExp = TypeSpec.classBuilder(AbstractExpression)
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addJavadoc("AbstractExpresion defines interface for interpretation. Interface must be used by" +
                        " TerminalExpression and NonTerminalExpression.")
                .addMethod(interpret).build();
        generatedCode[i] = JavaFile.builder(packageName,abstractExp).skipJavaLangImports(true).build();
        i += 1;

//      OrExpression class declaration
        ClassName OrExpression = ClassName.get("",classes[i]);
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
        generatedCode[i] = JavaFile.builder(packageName,orExp).skipJavaLangImports(true).build();
        i += 1;

//      OrExpression class declaration
        ClassName AndExpression = ClassName.get("",classes[i]);
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
        generatedCode[i] = JavaFile.builder(packageName,andExp).skipJavaLangImports(true).build();
        i += 1;

//      TerminalExpression class declaration
        ClassName TerminalExpression = ClassName.get("",classes[i]);
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
        generatedCode[i] = JavaFile.builder(packageName,termExp).skipJavaLangImports(true).build();

        logger.info("Returning generated java code to be written in files");

        return generatedCode;
    }
}
