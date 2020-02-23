package com.CreationalDP.prototype;

public class Client {
  private Prototype prototype;

  public Client(Prototype prototype) {
    this.prototype = prototype;
  }

  public static void main(String[] args) {
    Prototype prototype = new Prototype;
    Prototype prototypeCopy =  prototype.copyMe();
  }
}
