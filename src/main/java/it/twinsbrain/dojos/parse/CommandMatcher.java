package it.twinsbrain.dojos.parse;

import it.twinsbrain.dojos.commands.Command;
import it.twinsbrain.dojos.commands.CommandFactory;
import it.twinsbrain.dojos.values.From;
import it.twinsbrain.dojos.values.Pair;
import it.twinsbrain.dojos.values.To;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface CommandMatcher {
  Optional<Command> match(String commandString);

  static CommandMatcher createCommandMatcher(Pattern pattern, CommandFactory factory) {
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

  static Pair<From, To> coordinatesFrom(Matcher matcher) {
    var x1 = Integer.parseInt(matcher.group(1));
    var y1 = Integer.parseInt(matcher.group(2));
    var x2 = Integer.parseInt(matcher.group(3));
    var y2 = Integer.parseInt(matcher.group(4));
    return new Pair<>(From.of(x1, y1), To.of(x2, y2));
  }
}
