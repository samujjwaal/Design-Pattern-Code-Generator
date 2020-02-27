package com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DePaCoG extends Hw1DesignPatternGenerator {

    //Define a static logger variable so that it references the Logger instance
private static final Logger logger = LoggerFactory.getLogger(DePaCoG.class);

    public static void main(String[] args){

        logger.info("Started execution of main()");

        Hw1DesignPatternGenerator h = new Hw1DesignPatternGenerator();
        h.generateDesignPattern();

        logger.info("End of execution");
    }
}
