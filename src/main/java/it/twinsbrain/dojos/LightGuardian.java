package it.twinsbrain.dojos;

import it.twinsbrain.dojos.commands.Command;
import it.twinsbrain.dojos.commands.ToggleCommand;
import it.twinsbrain.dojos.commands.TurnOffCommand;
import it.twinsbrain.dojos.commands.TurnOnCommand;
import it.twinsbrain.dojos.values.From;
import it.twinsbrain.dojos.values.To;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LightGuardian {
  private final Pattern turnOnPattern = Pattern.compile("turn on (\\d),(\\d) through (\\d),(\\d)");
  private final Pattern turnOffPattern =
      Pattern.compile("turn off (\\d),(\\d) through (\\d),(\\d)");
  private final Pattern togglePattern = Pattern.compile("toggle (\\d),(\\d) through (\\d),(\\d)");
  private final LightGrid lightGrid = new LightGrid();

  public int howManyLightsOn() {
    return lightGrid.numberOfLightsOn();
  }

  public void receive(String commandString) {
    var turnOnCommandMatcher = createCommandMatcher(turnOnPattern, TurnOnCommand::new);
    var turnOffCommandMatcher = createCommandMatcher(turnOffPattern, TurnOffCommand::new);
    var toggleCommandMatcher = createCommandMatcher(togglePattern, ToggleCommand::new);
    var chain = List.of(turnOffCommandMatcher, turnOnCommandMatcher, toggleCommandMatcher);
    var command = chain.stream()
            .map(commandMatcher -> commandMatcher.match(commandString))
            .filter(Optional::isPresent)
            .findFirst()
            .flatMap(Function.identity())
            .orElse(null);
    lightGrid.accept(Objects.requireNonNull(command));
  }

  private CommandMatcher createCommandMatcher(Pattern pattern, CommandFactory factory) {
    return commandString -> {
      var turnOnMatcher = pattern.matcher(commandString);
      if (turnOnMatcher.matches()) {
        var fromToPair = coordinates(turnOnMatcher);
        return Optional.of(factory.create(fromToPair.first, fromToPair.second));
      } else {
        return Optional.empty();
      }
    };
  }

  private static Pair<From, To> coordinates(Matcher matcher) {
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
