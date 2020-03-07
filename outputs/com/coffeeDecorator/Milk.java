package com.coffeeDecorator;

/**
 * ConcreteDecoratorA, add features to component
 */
public class Milk extends CondimentDecorator {
  private boolean state;

  public void operation() {
    state = true;
    this.component.operation();
  }

  public boolean isState() {
    return state;
  }
}
