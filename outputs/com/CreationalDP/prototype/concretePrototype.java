package com.CreationalDP.prototype;

/**
 * Declares interface to copy it self.
 */
public class ConcretePrototype extends Prototype {
  public Prototype copyMe() throws CloneNotSupportedException {
    return (Prototype) this.clone();
  }
}
