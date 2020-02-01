# Homework 1
### Description: object-oriented design and implementation of a Design Pattern Code Generator.
### Grade: 8% + bonus up to 5% (3% for generating code in two or more languages and 2% for using Eclipse Java AST parser for generating code).
#### You can obtain this Git repo using the command git clone git clone git@bitbucket.org:cs474_spring2020/homework1.git.

## Preliminaries
As part of  homework assignment you will gain experience with creating and managing your Git repository, learn design patterns, create your model and the object-oriented design of a design pattern code generator, learn to create JUnit or Cucumber or FlatSpec tests, and you will create your SBT or Gradle build scripts. Doing this homework is essential for successful completion of the rest of this course, since all other homeworks and the course project will share the same features of this homework: branching, merging, committing, pushing your code into your Git repo, creating test cases and build scripts, and using various tools for diagnosing problems with your applications.

First things first, you must create your account at [BitBucket](https://bitbucket.org/), a Git repo management system. It is imperative that you use your UIC email account that has the extension @uic.edu. Once you create an account with your UIC address, BibBucket will assign you an academic status that allows you to create private repos. Bitbucket users with free accounts cannot create private repos, which are essential for submitting your homeworks and the course project. If you have a problem with obtaining the academic account with your UIC.EDU email address, please contact Atlassian's license and billing team and ask them to enable your academic account by filling out the [Atlassian Bitbucket academic account request form](https://www.atlassian.com/software/views/bitbucket-academic-license).

Your instructor created a team for this class named CS474_Spring2020. Please contact your TA, [Mr. Mohammed Siddiq](msiddi56@uic.edu) using your UIC.EDU email account and he will add you to the team repo as developers, since Mr.Siddiq already has the admin privileges. Please use your emails from the class registration roster to add you to the team and you will receive an invitation from BitBucket to join the team. Since it is a large class, please use your UIC email address for communications or Piazza and avoid emails from other accounts like funnybunny1998@gmail.com. If you don't receive a response within 24 hours, please contact us via Piazza, since it may be a case that your direct emails went to the spam folder.

Next, if you haven't done so, you will install [IntelliJ](https://www.jetbrains.com/student/) with your academic license, the JDK, the Scala runtime and the IntelliJ Scala plugin, the [Simple Build Toolkit (SBT)](https://www.scala-sbt.org/1.x/docs/index.html) or the [Gradle build tool](https://gradle.org/) and make sure that you can create, compile, and run Java and Scala programs. Please make sure that you can run [various Java tools from your chosen JDK](https://docs.oracle.com/en/java/javase/index.html).

In this and all consecutive homeworks and in the course project you will use logging and configuration management frameworks. You will comment your code extensively and supply logging statements at different logging levels (e.g., TRACE, INFO, ERROR) to record information at some salient points in the executions of your programs. All input and configuration variables must be supplied through configuration files -- hardcoding these values in the source code is generally prohibited and will be punished by taking a large percentage of points from your total grade! You are expected to use [Logback](https://logback.qos.ch/) and [SLFL4J](https://www.slf4j.org/) for logging and [Typesafe Conguration Library](https://github.com/lightbend/config) for managing configuration files. These and other libraries should be imported into your project using your script [build.sbt](https://www.scala-sbt.org/1.0/docs/Basic-Def-Examples.html) or [gradle script](https://docs.gradle.org/current/userguide/writing_build_scripts.html). These libraries and frameworks are widely used in the industry, so learning them is the time well spent to improve your resumes.

## Overview
In this homework, you will create an object-oriented design and implementation of a program that generates the implementation code of the design patterns. This homework combines two separate topics: design patterns for object-oriented programming and using object-oriented facilities of Java to realize your model of the design pattern program generator. That is, you will determine what program entities (e.g., classes and their methods) are needed as well as specific constraints on the implementation of these program entities (e.g., inheritance among classes or method calls in a certain sequence) for each design pattern. Next, you will create a model for a program generator with explicitely defined abstractions, translate your abstractions into design, refine your abstractions and explain the decomposition of your design into modules (e.g., packages, classes, interfaces, inner and anonymous classes). Finally, you will create a generator that emits the implementation of the chosen design pattern in a target language. This part can be accomplished by treating the resulting code as a record, i.e., where you manipulate the resulting Java program as a set of key/value strings. 

Even though you can generate the resulting design pattern implementations in Java, you can also add support for other languages like Scala and Go and C++ for which you will receive an additional bonus of up to 3%. In addition, a bonus worth 1% will be given if you if you choose to use Java reflection package to construct the generated code or a bonus of 3% will be given to you if you use the [Eclipse Java parser](https://www.vogella.com/tutorials/EclipseJDT/article.html). Those who implement the code generation using both options will receive an additional bonus worth  4%.


This homework script is written using a retroscripting technique, in which the homework outlines are generally and loosely drawn, and the individual students improvise to create the implementation that fits their refined objectives. In doing so, students are expected to stay within the basic requirements of the homework and they are free to experiments. That is, it is impossible that two non-collaborating students will submit similar homeworks! Asking questions is important, **so please ask away on Piazza!**

## Functionality
The user of your program generator is a programmer who needs to use design patterns in her program. Instead of writing the code for a design pattern, the programmer will invoke your Design Pattern Code Generator (DePaCoG) to enter the names of the classes that will represent entities in the desired design pattern. DePaCoG will take this input and it will generate the resulting code in a desired language for this pattern that can be added to the programmer's project and compiled. For example, consider a snippet of the Java code below that realizes a design pattern called Singleton.
```java
public class Singleton {
	private static Singleton INSTANCE = null;
	public static Singleton getInstance() { 
		if(INSTANCE == null) INSTANCE = new Singleton();
		return INSTANCE;}
}
```

To generate this implementation, you need to know the structural organization of a design pattern, its components and relations among them. In this case, you can create a template for the pattern shown in the code snippet below. The programmer will enter the class name and it will be substituted in the template resulting in the actual implementation shown above.
```java
public class #$$CS474_Template$$# {
	private static #$$CS474_Template$$# INSTANCE = null;
	public static #$$CS474_Template$$# getInstance() { 
		if(INSTANCE == null) INSTANCE = new #$$CS474_Template$$#();
		return INSTANCE;}
}
```

From the lectures we already learned a few patterns in the context of using OO polymorphism. In your DePaCoG you will implement the generation of the code for the following design patterns from the GoF book on Design Patterns: Elements of Reusable Object-Oriented Software published in 1994. You do not need to buy this book, [it is available for free](https://w3sdesign.com/GoF_Design_Patterns_Reference0100.pdf).

Undegraduate students are expected to implement the following core design patterns:

* Abstract factory groups object factories that have a common theme.
* Builder constructs complex objects by separating construction and representation.
* Factory method creates objects without specifying the exact class to create.
* Facade provides a simplified interface to a large body of code.
* Chain of responsibility delegates commands to a chain of processing objects.
* Mediator allows loose coupling between classes by being the only class that has detailed knowledge of their methods.
* Visitor separates an algorithm from an object structure by moving the hierarchy of methods into one object.
* Template method defines the skeleton of an algorithm as an abstract class, allowing its subclasses to provide concrete behavior.


Graduate students must implement the same design patterns as the undergraduate students in addition to the following design patterns:

* Prototype creates objects by cloning an existing object.
* Adapter allows classes with incompatible interfaces to work together by wrapping its own interface around that of an already existing class.
* Bridge decouples an abstraction from its implementation so that the two can vary independently.
* Composite composes zero-or-more similar objects so that they can be manipulated as one object.
* Decorator dynamically adds/overrides behaviour in an existing method of an object.
* Flyweight reduces the cost of creating and manipulating a large number of similar objects.
* Proxy provides a placeholder for another object to control access, reduce cost, and reduce complexity.
* Command creates objects which encapsulate actions and parameters.
* Interpreter implements a specialized language.
* Iterator accesses the elements of an object sequentially without exposing its underlying representation.
* Memento provides the ability to restore an object to its previous state (undo).
* Observer is a publish/subscribe pattern which allows a number of observer objects to see an event.
* State allows an object to alter its behavior when its internal state changes.
* Strategy allows one of a family of algorithms to be selected on-the-fly at runtime.

Your DePaCog will itself be based on the number of design patterns, most likely, the abstract factory, the builder, the composite, the facade, and the template method. Not only this homework will force you to learn these patterns, but also you will learn how to use them to implement DePaCog and how to implement these patterns in more than one language to receive your bonus points.

Even though it looks daunting to implement so many design patterns in such limited time, the actual effort is much smaller. DePaCoG should use the same abstraction for all design patterns; the different is how constituent elements are organized in each design pattern to achieve a desired goal. From this perspective, it is the same abstract operations in DePaCoG that take user-defined class/interface names and place them in proper positions in the templates for the code generation. The real benefit for students is not only that they learn design patterns, which are very valuable things to know to pass interviews, but also they elevate seemingly disjoint concepts of design patterns under the single roof of the abstraction.

Your homework can be divided roughly into five steps. First, you learn the design patterns, their structure and how their constituent elements are organized and what your building blocks are for their realization in the target code. I suggest that you mock implementations of each pattern in IntelliJ and explore its classes, interfaces, and dependencies. Second, you create your own model that describes an abstract pattern structure and how you want to operate on it. Next, you will create an implementation of your design where you will use more than TWO different design patterns from the [GoF book](http://wiki.c2.com/?DesignPatternsBook). Fourth, you will create multiple unit tests using [JUnit framework](https://junit.org/junit5/) or some other framework like [Cucumber](https://cucumber.io/). Finally, you will run tests, run your program, generate the resulting pattern implementation in target languages, and report the results. 

## Baseline
To be considered for grading, your project should include at least one of your own written programs in Java (i.e., not copied examples where you renamed variables or refactored them similarly), your project should be buildable using the SBT or the Gradle, and your documentation must specify your chosen abstractions with links to specific modules where they are realized. Your documentation must include your design and model, the reasoning about pros and cons, explanations of your implementation and the chosen design patterns that you used to implement DePaCoG, and the results of your runs. Simply copying some open-source Java programs from examples and modifying them a bit (e.g., rename some variables) will result in desk-rejecting your submission.

## Piazza collaboration
You can post questions and replies, statements, comments, discussion, etc. on Piazza. For this homework, feel free to share your ideas, mistakes, code fragments, commands from scripts, and some of your technical solutions with the rest of the class, and you can ask and advise others using Piazza on where resources and sample programs can be found on the internet, how to resolve dependencies and configuration issues. When posting question and answers on Piazza, please select the appropriate folder, i.e., hw1 to ensure that all discussion threads can be easily located. Active participants and problem solvers will receive bonuses from the big brother :-) who is watching your exchanges on Piazza (i.e., your class instructor and your TA). However, *you must not describe your design or specific details related how your construct your models!*
Since you can post your questions anonymously on pizza, there is no reason to be afraid to ask questions. Start working on this homework early and ask away!
When you come to see your TA during his office hours don't ask him to debug your code - Mr.Siddiq **is prohibited** from doing so. You can discuss your design, abstractions, and show him your code to receive his feedback, but you should not ask him to solve problems for you!

## Git logistics
**This is an individual homework.** Separate repositories will be created for each of your homeworks and for the course project. You will fork the repository for this homework and your fork will be **private**, no one else besides you, the TA and your course instructor will have access to your fork. Please remember to grant a read access to your repository to your TA and your instructor. In future, for the team homeworks and the course project, you should grant the write access to your forkmates, but NOT for this homework. You can commit and push your code as many times as you want. Your code will not be visible and it should not be visible to other students (except for your forkmates for a team project, but not for this homework). When you push the code into the remote repo, your instructor and the TA will see your code in your separate private fork. Making your fork public or inviting other students to join your fork for an individual homework will result in losing your grade. For grading, only the latest push timed before the deadline will be considered. **If you push after the deadline, your grade for the homework will be zero**. For more information about using the Git and Bitbucket specifically, please use this [link as the starting point](https://confluence.atlassian.com/bitbucket/bitbucket-cloud-documentation-home-221448814.html). For those of you who struggle with the Git, I recommend a book by Ryan Hodson on Ry's Git Tutorial. The other book called Pro Git is written by Scott Chacon and Ben Straub and published by Apress and it is [freely available](https://git-scm.com/book/en/v2/). There are multiple videos on youtube that go into details of the Git organization and use.

Please follow this naming convention while submitting your work : "Firstname_Lastname_hw1" without quotes, where you specify your first and last names **exactly as you are registered with the University system**, so that we can easily recognize your submission. I repeat, make sure that you will give both your TA and the course instructor the read/write access to your *private forked repository* so that we can leave the file feedback.txt with the explanation of the grade assigned to your homework.

## Discussions and submission
As it is mentioned above, you can post questions and replies, statements, comments, discussion, etc. on Piazza. Remember that you cannot share your code and your solutions privately, but you can ask and advise others using Piazza and StackOverflow or some other developer networks where resources and sample programs can be found on the Internet, how to resolve dependencies and configuration issues. Yet, your implementation should be your own and you cannot share it. Alternatively, you cannot copy and paste someone else's implementation and put your name on it. Your submissions will be checked for plagiarism. **Copying code from your classmates or from some sites on the Internet will result in severe academic penalties up to the termination of your enrollment in the University**. When posting question and answers on Piazza, please select the appropriate folder, i.e., hw1 to ensure that all discussion threads can be easily located.


## Submission deadline and logistics
Sunday, February 23 at 10PM CST via the bitbucket repository. Your submission will include the code for the program, your documentation with instructions and detailed explanations on how to assemble and deploy your program along with the results of your simulation and a document that explains these results based on the characteristics and the parameters of your models, and what the limitations of your implementation are. Again, do not forget, please make sure that you will give both your TAs and your instructor the read access to your private forked repository. Your name should be shown in your README.md file and other documents. Your code should compile and run from the command line using the commands **sbt clean compile test** and **sbt clean compile run** or the corresponding commands for Gradle. Also, you project should be IntelliJ friendly, i.e., your graders should be able to import your code into IntelliJ and run from there. Use .gitignore to exlude files that should not be pushed into the repo.


## Evaluation criteria
- the maximum grade for this homework is 8% with the bonus described above. Points are subtracted from this maximum grade: for example, saying that 2% is lost if some requirement is not completed means that the resulting grade will be 8%-2% => 6%; if the core homework functionality does not work, no bonus points will be given;
- only some POJO classes are created and nothing else is done: up to 7% lost;
- having less than five unit and/or integration tests: up to 5% lost;
- missing comments and explanations from the submitted program: up to 5% lost;
- logging is not used in your programs: up to 3% lost;
- hardcoding the input values in the source code instead of using the suggested configuration libraries: up to 4% lost;
- no instructions in README.md on how to install and run your program: up to 5% lost;
- the program crashes without completing the core functionality: up to 6% lost;
- no design and modeling documentation exists that explains your choices: up to 6% lost;
- the deployment documentation exists but it is insufficient to understand how you assembled and deployed all components of the program: up to 5% lost;
- the minimum grade for this homework cannot be less than zero.

That's it, folks!