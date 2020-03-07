package com.laptopAbstractFactory;

/**
 * ConcreteFactory2, implements creation of the concrete Product2 objects
 */
public class ChromeBookFactory implements Ubuntu {
  public Processor createProcessor() {
    return new AMD();
  }

  public OperatingSystem createOperatingSystem() {
    return new ChromeOS();
  }
}
