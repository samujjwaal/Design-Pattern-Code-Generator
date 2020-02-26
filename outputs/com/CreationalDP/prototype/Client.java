package com.CreationalDP.prototype;

/**
 * Creates a new object by asking a Prototype to clone itself
 */
public class Client extends Prototype {
  private Prototype prototype;

  public Client(Prototype prototype) {
    this.prototype = prototype;
  }

  public Prototype operation() throws CloneNotSupportedException {
    return prototype.copyMe();
  }
}
