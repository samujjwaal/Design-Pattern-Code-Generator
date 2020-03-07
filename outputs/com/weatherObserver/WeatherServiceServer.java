package com.weatherObserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Subject knows its observers. Any number of Observer objects may observe a subject.
 */
abstract class WeatherServiceServer {
  private List<SmartPhone> observers = new ArrayList<SmartPhone>();

  public void attach(SmartPhone observer) {
    observers.add(observer);
  }

  public void detach(SmartPhone observer) {
    observers.remove(observer);
  }

  public void notifyObservers() {
    (Iterator iterator = observers.iterator(); iterator.hasNext();) {
      SmartPhone observer = (SmartPhone) iterator.next();
      observer.update();
    }
  }
}
