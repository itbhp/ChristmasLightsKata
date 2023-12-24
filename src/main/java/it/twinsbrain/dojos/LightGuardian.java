package it.twinsbrain.dojos;

import it.twinsbrain.dojos.commands.*;
import it.twinsbrain.dojos.exception.InvalidCommandException;
import it.twinsbrain.dojos.parse.CommandMatcher;
import it.twinsbrain.dojos.values.From;
import it.twinsbrain.dojos.values.Pair;
import it.twinsbrain.dojos.values.To;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;
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

  private CommandMatcher createCommandMatcher(Pattern pattern, CommandFactory factory) {
    return commandString -> {
      var matcher = pattern.matcher(commandString);
      if (matcher.matches()) {
        var fromToPair = coordinatesFrom(matcher);
        return Optional.of(factory.create(fromToPair.first(), fromToPair.second()));
      } else {
        return Optional.empty();
      }
    };
  }

  private Pair<From, To> coordinatesFrom(Matcher matcher) {
    var x1 = Integer.parseInt(matcher.group(1));
    var y1 = Integer.parseInt(matcher.group(2));
    var x2 = Integer.parseInt(matcher.group(3));
    var y2 = Integer.parseInt(matcher.group(4));
    return new Pair<>(From.of(x1, y1), To.of(x2, y2));
  }
}
