package com.usbAdapter;

/**
 * Adapter class, adapts Adaptee to the Target interface
 */
public class USBDongle implements TypeC {
  private MicroUSB adaptee;

  public USBDongle(MicroUSB adaptee) {
    this.adaptee = adaptee;
  }

  public String request() {
    return adaptee.specialRequest();
  }
}
