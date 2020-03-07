## Object-oriented design and implementation of a Design Pattern Code Generator.
This project is the implementation of a program that generates the implementation code of the 23 Gang of Four(GoF) design patterns, as presented in the book “Design Patterns: Elements of Reusable Object-Oriented Software”. 

------

## Index

1. About Design Patterns
2. About JavaPoet
3. Application Design
4. Test Cases
5. Instructions to Execute
6. Results of Execution

------

#### 1. About Design Patterns

Design patterns are the best practices that a programmer can employ to solve trivial problems while designing an application. Design patterns help in speeding up the development process as they provide a set of time tested, proven development paradigms. They provide an industry standard approach to solve a recurring problem. Using design patterns promotes reusability that leads to more robust and highly maintainable code.

The 23 Design Patterns are classified into 3 main categories:

* Creational Design Patterns
  * Singleton
  * Abstract  Factory
  * Builder
  * Factory Method

* Structural Design Patterns
  * Adapter
  * Bridge
  * Composite
  * Decorator
  * Facade
  * Flyweight
  * Proxy
* Behavioral Design Patterns
  * Chain of Responsibility
  * Command
  * Interpreter
  * Iterator
  * Mediator
  * Memento
  * Observer
  * State
  * Strategy
  * Template
  * Visitor

------

#### 2. About JavaPoet

[JavaPoet](https://github.com/square/javapoet), a successor to [JavaWriter](https://github.com/square/javapoet/tree/javawriter_2), is a Java API for generating .java source files. It can generate primitive types, reference types (like classes, interfaces, enumerated types, anonymous inner classes), fields, methods, parameters, annotations, and Javadocs. 

JavaPoet manages the import of the dependent classes automatically. It uses the ***Builder*** design pattern to specify the logic to generate Java code.

A HelloWorld program like the one here:

```java
package com.example.helloworld;

public final class HelloWorld {
  public static void main(String[] args) {
    System.out.println("Hello, JavaPoet!");
  }
}
```

can be generated using JavaPoet as follows:

```java
MethodSpec main = MethodSpec.methodBuilder("main")
    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
    .returns(void.class)
    .addParameter(String[].class, "args")
    .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
    .build();

TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
    .addMethod(main)
    .build();

JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
    .build();

javaFile.writeTo(System.out);
```

`MethodSpec` "main" is configured with modifiers, return type, parameters and code statements to declare the main method. The main method is added to a `HelloWorld` class and then the class is added to a `HelloWorld.java` file.

JavaPoet offers models for classes & interfaces (`TypeSpec`), fields (`FieldSpec`), methods & constructors (`MethodSpec`), parameters (`ParameterSpec`) and annotations (`AnnotationSpec`). It also generates the package folder structure for the output java source files.

The most recent version of JavaPoet available as of now is 1.12.1.

------

#### 3. Application Design

To implement Design Pattern Code Generator(DePaCoG), I have employed the **Factory Method**, **Template Method** and **Singleton** design patterns.

------

The **Factory Method**, a creational design pattern, creates two basic abstractions: the Product and Creator classes. Both these classes are abstract and clients need to extend them to realize their specific implementations. This pattern is used when there is a super class with multiple sub-classes and based on user input a particular sub-class needs to be instantiated. The responsibility of instantiation of a class is taken from the user to the factory class. 

In the case of DePaCoG, the `DesignPattern` and `DesignPatternGenerator` are the two abstractions I have chosen.

* `DesignPattern` is an `interface` which declares methods for accepting and displaying the class names & package names from the user, generating java source code of the design patterns using JavaPoet and writing the java code into java files as output.

```java
public interface DesignPattern {
    
    void displayClassNames(String[] classes);
    
    String[] setClassNames(String[] oldClasses);
    
    String setPackageName(String defaultPckgName);
    
    void createDesignPattern(String[] oldClasses, String oldPackageName);
    
    JavaFile[] generateCode(String[] classes, String packageName);
    
    void writeJavaFiles(JavaFile[] files);
}
```

There are 23 separate concrete classes(java files) to generate the java source code for each design pattern. Each concrete class `implements DesignPattern`. Interface `DesignPattern` provides the `default` method definition of all methods except the method `generateCode()` as the implementation of all other methods do not change  according to the design patterns. Each concrete class of `DesignPattern` overrides `generateCode()`. 

* `DesignPatternGenerator` is an `abstract class` which declares methods to display a menu of all available design patterns and invoking appropriate classes to generate a  user selected design pattern.

```java
abstract class DesignPatternGenerator {

    protected  String[] designPatterns;

    abstract void displayDesignPatterns();

    abstract void generateDesignPattern();

    abstract void designPatternFactory();
}
```

The field `designPatterns` declares all the design patterns available for creation. All the methods are abstract and are provided with the implementation in the concrete class `Hw1DesignPatternGenerator` which `extends DesignPatternGenerator.` The class `Hw1DesignPatternGenerator` also defines an additional method `chooseDesignPattern(int choice)` which takes in user’s choice of design pattern as input and instantiates an anonymous object of the appropriate design pattern class.

* Advantages of using Factory Method pattern:
  * It provides an approach to “program to an interface, not an implementation”.
  * Through inheritance it provides an abstraction between implementation and client classes. It removes the instantiation of objects of implementation classes from the user/client code
  * It makes the code more robust, easy to extend and less coupled
  * New types of concrete products can be introduced without any modifications to the existing client code
* Disadvantages of using Factory Method pattern:
  * For just creating a single instance of a particular concrete object the user might have to sub-class the creator, which complicates the hierarchy of creator classes
  * Makes the code difficult to interpret as all code is behind an abstraction that may also be hiding another set of abstractions

------

The **Template Method**, a behavioral design pattern, is used to create method stub and defer the steps of implementation to the subclasses. 

The algorithm for generating design patterns has the following steps: display list of all design patterns, ask user which design pattern code is to be generated, ask for and set custom (or default) class names & package name, generate source file using JavaPoet and finally write into output files. The order of execution of these steps cannot be altered as the sequence is critical for the execution of the program. So here different methods are used to achieve the goal of generating the design pattern code. Steps like asking class names& package name and writing generated code into file are common for any design pattern whether it is a creational or structural design pattern. 

This is where Template Method design pattern is useful. It helps define the basic steps to execute an algorithm and can provide default implementation of the methods that might be common for all or a majority of the sub-classes implementing the design pattern. The method is which defines an exclusive property of a design pattern can be overridden by the sub-class.

Template Method is implemented in the `interface DesignPattern` and its sub-classes. Interface `DesignPattern` provides the `default` method definition of all methods except the method `generateCode()` as the implementation of all other methods do not change  according to the design patterns. Each concrete class of `DesignPattern` overrides `generateCode()`.

Inside the `interface DesignPattern`:

```java
JavaFile[] generateCode(String[] classes, String packageName);
```

Inside `class Singleton` which implements `DesignPattern`:

```java
public JavaFile[] generateCode(String[] classes, String packageName){
        int i = 0;
        ClassName Singleton = ClassName.get("", classes[i]);
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .build();
        FieldSpec instance = FieldSpec.builder(Singleton, "INSTANCE")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .build();
        MethodSpec getInstance = MethodSpec.methodBuilder("getInstance")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(Singleton)
                .beginControlFlow("if ($N == null)", instance.name)
                .addStatement("$N = new $T()",instance.name, Singleton)
                .endControlFlow()
                .addStatement("return $N", instance.name)
                .build();
        TypeSpec singleton = TypeSpec.classBuilder(Singleton)
                .addModifiers(Modifier.PUBLIC)
                .addField(instance)
                .addMethod(constructor)
                .addMethod(getInstance)
                .build();
        generatedCode[i] = JavaFile.builder(packageName, singleton)
                .skipJavaLangImports(true)
                .build();
        return generatedCode;
    }
```

* Advantages of using Template Method pattern:
  * All the duplicate code/methods of sub-classes can be put together in the super-class
  * Client can override only certain methods of a large algorithm, so that the other components of the algorithm are not affected  
* Disadvantages of using Template Method pattern:
  * Sometimes client might have to change certain fundamental aspects of the algorithm and changing only certain fixed components of the algorithm might not suffice
  * If the number of components in the algorithm being modelled is large, it is harder to maintain the code

------

The **Singleton**, a creational design pattern, that helps in ensuring that a class has only one instance in a program while also providing a global access point to the instance. I have used Singleton to instantiate only one instance of the class `Hw1DesignPatternGenerator` so that in the client class `DePaCoG` the design pattern generating method is only called once at a time. The `instance` field in `Hw1DesignPatternGenerator` is lazy initialized.

Inside class `Hw1DesignPatternGenerator`:

```java
private static Hw1DesignPatternGenerator instance;

private Hw1DesignPatternGenerator(){
    logger.info("Executing constructor Hw1DesignPatternGenerator()");
    generateDesignPattern();
}

public static Hw1DesignPatternGenerator getInstance(){
        if (instance == null) {
            instance = new Hw1DesignPatternGenerator();
        }
        return instance;
    }
```

Creating instance inside `DePaCoG` class:

```java
public class DePaCoG {
    public static void main(String[] args){
        
        Hw1DesignPatternGenerator.getInstance();
    }
}
```

* Advantages of using Singleton pattern:
  * It makes sure that a single instance is instantiated for a class
  * Helps to get a global access point to the instance
  * The object is only initialized when initially requested    
* Disadvantages of using Singleton pattern:
  * Sometimes singleton pattern can hide bad code design
  * It requires modifications to be used in a multi threaded environment to make sure multiple instances are not created by multiple threads

------

The image below shows a class diagram for the complete application. For simplicity I have selected only 3 design pattern classes (each representing its category of design patterns) out of the 23.

![](/screenshots/DePaCoG.png)

The class `DePaCoG` has the main() method in it. It creates an instance of class `Hw1DesignPatternGenerator` (concrete implementation of `DesignPatternGenerator` ). Methods here are for displaying menu of design patterns and getting choice of design pattern from user. Depending on user’s choice the corresponding design pattern generating class (implementation of ``DesignPattern`) is instantiated to generate the design pattern source code. 



#### 4. Test Cases

There are 2 test classes `DesignPatternTest` and `Hw1DesignPatternGeneratorTest`, with a total of 5 test cases.

* Test Case to verify only 1 instance of `Hw1DesignPatternGenerator` is created each time.
* Test Case to verify correct design pattern file is executed when user inputs the choice of design pattern.
* Test Case to check null is returned on selecting incorrect design pattern choice.
* Test Case to verify  `generateCode()` returns `JavaFile[]` object
* Test Case to verify the generated java files are written successfully in the output folder.

------

#### 5. Instructions to Execute

First clone the repository from Bitbucket.

Execution can be in two modes, using default config file or using interactive environment/menu to get custom inputs from user.

<u>The `default.conf` configuration file is present at `src/main/resources`.</u> 

For command line execution, locate `hw1-assembly-0.1.jar` at `target\scala-2.13`.

On Windows, the command to run the jar file: `java -jar hw1-assembly-0.1.jar mode`

* Executing using default config file, then `mode = 0`: 

  * run `java -jar hw1-assembly-0.1.jar 0`

  ![](/screenshots/Capture4.PNG)

* Executing using custom inputs from user, then `mode = 1`: 

  * run `java -jar hw1-assembly-0.1.jar 1`

  ![](/screenshots/Capture2.PNG)

For execution in IntelliJ, use `run 0` or `run 1` for the preferred way of execution (argument passed has similar purpose like in the command line method) 

The output java files are saved under the package name provided in the config file (or as custom input) in a folder named `outputs`. The `outputs` folder is created in the current working directory during execution.

------

#### 6. Results of Execution

While executing the Design Pattern Code Generator, the main() method in class `DePaCoG` is executed along with a command line argument (`0` or `1`). The command line argument is to select whether inputs will be entered via a configuration file or via inputs through the interactive environment.

Here is an example of config values to create Abstract Factory design pattern.

```
{
      designPatternChoice = "2"
      classes = ["Processor", "Intel", "AMD",
      "OperatingSystem", "OS", "ChromeOS", "Ubuntu",
      "LaptopFactory", "ChromeBookFactory","LinuxLaptopFactory"]
      packageName = "com.laptopAbstractFacty"
}
```

For above config input, the output files will be saved at `outputs/com/laptopAbstractFactory`.

The classes created are : `Processor.java, Intel.java, AMD.java, OperatingSystem.java, OS.java, ChromeOS.java, Ubuntu.java, LaptopFactory.java, ChromeBookFactory.java,LinuxLaptopFactory.java`

The image below shows the class diagram of the classes generated after executing the config input values.

![](/screenshots/laptopFactory.png)

In addition to Abstract Factory, the `default.conf` configuration file also contains inputs for Decorator, Adapter and Observer design patterns.

The `outputs` folder is created in the current working directory during execution.

<img src="/screenshots/Capture.PNG" style="zoom:75%;" />

