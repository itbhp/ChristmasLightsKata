package it.twinsbrain.dojos;

import it.twinsbrain.dojos.commands.Command;
import it.twinsbrain.dojos.commands.ToggleCommand;
import it.twinsbrain.dojos.commands.TurnOffCommand;
import it.twinsbrain.dojos.commands.TurnOnCommand;
import it.twinsbrain.dojos.values.From;
import it.twinsbrain.dojos.values.To;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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
    try {
      var command =
          chain.stream()
              .map(commandMatcher -> commandMatcher.match(commandString))
              .filter(Optional::isPresent)
              .findFirst()
              .flatMap(Function.identity())
              .orElseThrow(IllegalArgumentException::new);
      lightGrid.accept(command);
    } catch (IllegalArgumentException e) {
      System.out.println("invalid command " + commandString);
      throw e;
    }
  }

  private CommandMatcher createCommandMatcher(Pattern pattern, CommandFactory factory) {
    return commandString -> {
      var matcher = pattern.matcher(commandString);
      if (matcher.matches()) {
        var fromToPair = coordinatesFrom(matcher);
        return Optional.of(factory.create(fromToPair.first, fromToPair.second));
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

  private interface CommandFactory {
    Command create(From from, To to);
  }

  private interface CommandMatcher {
    Optional<Command> match(String commandString);
  }

  private record Pair<T, U>(T first, U second) {}
}
