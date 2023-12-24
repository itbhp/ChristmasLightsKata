package it.twinsbrain.dojos.parse;

import it.twinsbrain.dojos.commands.Command;
import java.util.Optional;

public interface CommandMatcher {
  Optional<Command> match(String commandString);
}
