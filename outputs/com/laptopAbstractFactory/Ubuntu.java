package com.laptopAbstractFactory;

/**
 * Abstract Factory, defines interface for creation of the abstract product objects
 */
public interface Ubuntu {
  Processor createProcessor();

  OperatingSystem createOperatingSystem();
}
