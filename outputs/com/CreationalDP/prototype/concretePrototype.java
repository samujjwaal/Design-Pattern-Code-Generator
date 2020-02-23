package com.CreationalDP.prototype;

public class concretePrototype extends Prototype {
  public Prototype copyMe() throws CloneNotSupportedException {
    return (Prototype) this.clone();
  }
}
