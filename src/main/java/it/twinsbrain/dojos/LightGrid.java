package it.twinsbrain.dojos;

import it.twinsbrain.dojos.commands.Command;
import it.twinsbrain.dojos.commands.ToggleCommand;
import it.twinsbrain.dojos.commands.TurnOffCommand;
import it.twinsbrain.dojos.commands.TurnOnCommand;
import it.twinsbrain.dojos.values.From;
import it.twinsbrain.dojos.values.Light;
import it.twinsbrain.dojos.values.To;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class LightGrid {

    private final From startPoint;
    private final To endPoint;
    private final Light[][] grid;

    public LightGrid() {
        int numberOfRows = 1000;
        int numberOfColumns = 1000;
        grid = new Light[numberOfColumns][numberOfRows];
        for (int i = 0; i < numberOfRows; i++) {
            grid[i] = new Light[numberOfColumns];
            for (int j = 0; j < numberOfColumns; j++) {
                grid[i][j] = new Light();
            }
        }
        startPoint = new From(0, 0);
        endPoint = new To(numberOfRows - 1, numberOfColumns - 1);
    }

    public void accept(Command command) {
        switch (command) {
            case TurnOnCommand turnOnCommand -> executeAction(turnOnCommand.from(), turnOnCommand.to(), Light::turnOn);
            case TurnOffCommand turnOffCommand ->
                    executeAction(turnOffCommand.from(), turnOffCommand.to(), Light::turnOff);
            case ToggleCommand toggleCommand -> executeAction(toggleCommand.from(), toggleCommand.to(), Light::toggle);
        }
    }

    public int numberOfLightsOn() {
        var n = new AtomicInteger(0);
        executeAction(startPoint, endPoint, light -> {
            if (light.isOn()) {
                n.incrementAndGet();
            }
        });
        return n.get();
    }

    private void executeAction(From from, To to, Consumer<Light> action) {
        for (int i = from.x(); i <= to.x(); i++) {
            for (int j = from.y(); j <= to.y(); j++) {
                action.accept(grid[i][j]);
            }
        }
    }
}
