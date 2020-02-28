package com;

import com.squareup.javapoet.JavaFile;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Arrays;

public class DePaCoG {

    //Define a static logger variable so that it references the Logger instance
    private static final Logger logger = (Logger) LoggerFactory.getLogger(DePaCoG.class);

    public static void main(String[] args) throws IOException {
        logger.info("Started execution of main()");

        Hw1DesignPatternGenerator hw1 = Hw1DesignPatternGenerator.getInstance();
//        hw1.displayDesignPatterns();

        if(args[0].equals("0")){

            logger.info("Parsing input from default config file ");

            // parse the config file
            Config paramsConfig = ConfigFactory.load("default.conf");

            //mode for taking input from config file
            int mode = 0;
            // create array of Config objects to obtain inputs for every iterations of run
            Config[] iterations = paramsConfig.getConfigList("iterations").toArray(new Config[0]);

                for (Config iteration: iterations) {
                    // get the choice of design pattern to be generated
                    int choice = iteration.getInt("designPatternChoice");
                    // get class names for output files
                    Object[] classes = iteration.getStringList("classes").toArray();
                    String[] classNames = Arrays.copyOf(classes,classes.length,String[].class);
                    // get package name to set for output files
                    String packageName = iteration.getString("packageName");
                    // pass design pattern choice and mode
                    DesignPattern outputDesignPattern = hw1.chooseDesignPattern(choice,mode);
                    // get outputs files as JavaFile[]
                    JavaFile[] files = outputDesignPattern.generateCode(classNames,packageName);
                    // write generated design patterns into files
                    outputDesignPattern.writeJavaFiles(files);
                }
        }
        if (args[0].equals("1")){
            logger.info("Displaying interactive menu");
            // to execute using custom inputs
            hw1.generateDesignPattern();
        }
       logger.info("End of execution");
    }
}
