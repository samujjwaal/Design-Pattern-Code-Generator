package com.CreationalDP.prototype;

public abstract class Prototype implements Cloneable {
  abstract Prototype copyMe() throws CloneNotSupportedException;
}
