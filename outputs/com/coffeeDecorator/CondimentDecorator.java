package com.coffeeDecorator;

/**
 * Decorator, keep reference to Component object
 */
abstract class CondimentDecorator implements Beverage {
  protected Beverage component;

  public abstract void operation();

  public void setComponent(Beverage component) {
    this.component = component;
  }
}
