package com.weatherObserver;

/**
 * ConcreteSubject stores state of interest to ConcreteObserver objects, sends a notification to its observers
 * when its state changes.
 */
public class DarkSkyServer extends WeatherServiceServer {
  private int state;

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
    this.notifyObservers();
  }
}
