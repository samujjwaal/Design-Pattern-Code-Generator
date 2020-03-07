package com.laptopAbstractFactory;

/**
 * ConcreteFactory1, implements creation of the concrete Product1 objects
 */
public class LaptopFactory implements Ubuntu {
  public Processor createProcessor() {
    return new Intel();
  }

  public OperatingSystem createOperatingSystem() {
    return new OS();
  }
}
