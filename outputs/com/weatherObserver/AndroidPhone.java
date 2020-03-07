package com.weatherObserver;

/**
 * ConcreteObserver maintains a reference to a ConcreteSubject object, stores
 * state that should stay consistent with the subject's, implements the Observer
 * updating interface to keep its state consistent with the subject's.
 */
public class AndroidPhone implements SmartPhone {
  private int observerState;

  private DarkSkyServer subject;

  public AndroidPhone(DarkSkyServer subject) {
    this.subject = subject;
  }

  public void update() {
    observerState = subject.getState();
  }

  protected int getObserverState() {
    return observerState;
  }
}
