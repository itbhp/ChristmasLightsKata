package it.twinsbrain.dojos;

import it.twinsbrain.dojos.commands.Command;
import it.twinsbrain.dojos.commands.TurnOnCommand;
import it.twinsbrain.dojos.values.From;
import it.twinsbrain.dojos.values.Light;
import it.twinsbrain.dojos.values.To;

import java.util.concurrent.atomic.AtomicInteger;

public class LightGrid {

  private final Light[][] grid = new Light[1000][1000];
  private final AtomicInteger numberOfLightsOn = new AtomicInteger(0);

  public LightGrid() {
    for (int i = 0; i < 1000; i++) {
      grid[i] = new Light[1000];
      for (int j = 0; j < 1000; j++) {
        grid[i][j] = new Light();
      }
    }
  }

  public void accept(Command command) {
    switch (command) {
      case TurnOnCommand turnOnCommand -> turnOn(turnOnCommand.from(), turnOnCommand.to());
      default -> throw new IllegalArgumentException("Unexpected command: " + command);
    }
  }

  private void turnOn(From from, To to) {
    for (int i = from.x(); i <= to.x(); i++) {
      for (int j = from.y(); j <= to.y(); j++) {
        grid[i][j].turnOn();
        numberOfLightsOn.incrementAndGet();
      }
    }
  }

  public int numberOfLightsOn() {
    return numberOfLightsOn.get();
  }
}
