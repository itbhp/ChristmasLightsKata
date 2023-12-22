package it.twinsbrain.dojos.values;

public class Light {
  private boolean isOn;

  public boolean isOn() {
    return isOn;
  }

  public void turnOn() {
    this.isOn = true;
  }

  public void turnOff() {
    this.isOn = false;
  }

  public void toggle() {
    if (this.isOn) {
      turnOff();
    } else {
      turnOn();
    }
  }
}
