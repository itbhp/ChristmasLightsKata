package it.twinsbrain.dojos.parse;

import it.twinsbrain.dojos.commands.Command;
import it.twinsbrain.dojos.commands.CommandFactory;
import java.util.Optional;
import java.util.regex.Pattern;

public interface CommandMatcher {
  Optional<Command> match(String commandString);

  static CommandMatcher createCommandMatcher(CoordinatesExtractor extractor, Pattern pattern, CommandFactory factory) {
    return commandString -> {
      var matcher = pattern.matcher(commandString);
      if (matcher.matches()) {
        var fromToPair = extractor.extractFrom(matcher);
        return Optional.of(factory.create(fromToPair._1(), fromToPair._2()));
      } else {
        return Optional.empty();
      }
    };
  }
}
