package it.twinsbrain.dojos.values;

public class Light {
    private boolean isOn;

    public void turnOn(){
        this.isOn = true;
    }


    public boolean isOn() {
        return isOn;
    }

    public void turnOff() {
        this.isOn = false;
    }
}
