package com.coffeeDecorator;

/**
 * ConcreteDecoratorB, add features to component
 */
public class WhipCream extends CondimentDecorator {
  private boolean behaviorMethodInvoked = false;

  public void operation() {
    this.component.operation();
    addedBehavior();
  }

  private void addedBehavior() {
    behaviorMethodInvoked = true;
  }

  protected boolean isBehaviorMethodInvoked() {
    return behaviorMethodInvoked;
  }
}
