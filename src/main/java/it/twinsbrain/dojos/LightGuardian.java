package it.twinsbrain.dojos;

import static it.twinsbrain.dojos.parse.CommandMatcher.createCommandMatcher;

import it.twinsbrain.dojos.commands.*;
import it.twinsbrain.dojos.exception.InvalidCommandException;
import it.twinsbrain.dojos.parse.CommandMatcher;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class LightGuardian {
  private final LightGrid lightGrid = new LightGrid();
  private final List<CommandMatcher> chain;

  public LightGuardian() {
    var turnOnPattern = Pattern.compile("turn on (\\d+),(\\d+) through (\\d+),(\\d+)");
    var turnOffPattern = Pattern.compile("turn off (\\d+),(\\d+) through (\\d+),(\\d+)");
    var togglePattern = Pattern.compile("toggle (\\d+),(\\d+) through (\\d+),(\\d+)");

    var turnOnCommandMatcher = createCommandMatcher(turnOnPattern, TurnOnCommand::new);
    var turnOffCommandMatcher = createCommandMatcher(turnOffPattern, TurnOffCommand::new);
    var toggleCommandMatcher = createCommandMatcher(togglePattern, ToggleCommand::new);

    this.chain = List.of(turnOffCommandMatcher, turnOnCommandMatcher, toggleCommandMatcher);
  }

  public int howManyLightsOn() {
    return lightGrid.numberOfLightsOn();
  }

  public void receive(String commandString) {
    var command = parseCommand(commandString);
    lightGrid.accept(command);
  }

  private Command parseCommand(String commandString) {
    return chain.stream()
        .map(commandMatcher -> commandMatcher.match(commandString))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst()
        .orElseThrow(newInvalidCommandExceptionSupplier(commandString));
  }

  private static Supplier<InvalidCommandException> newInvalidCommandExceptionSupplier(String commandString) {
    return () -> {
      String message = "invalid command " + commandString;
      System.out.println(message);
      return new InvalidCommandException(message);
    };
  }
}
