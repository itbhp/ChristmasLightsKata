package it.twinsbrain.dojos;

import it.twinsbrain.dojos.commands.*;
import it.twinsbrain.dojos.exception.InvalidCommandException;
import it.twinsbrain.dojos.parse.CommandMatcher;
import it.twinsbrain.dojos.values.From;
import it.twinsbrain.dojos.values.To;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javaslang.Function3;
import javaslang.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightGuardian {
    private static final Logger logger = LoggerFactory.getLogger(LightGuardian.class);
    private final LightGrid lightGrid = new LightGrid();
    private final List<CommandMatcher> chain;

    private LightGuardian(List<CommandMatcher> chain) {
        this.chain = chain;
    }

    public static LightGuardian newLightGuardian() {
        var turnOnPattern = Pattern.compile("turn on (\\d+),(\\d+) through (\\d+),(\\d+)");
        var turnOffPattern = Pattern.compile("turn off (\\d+),(\\d+) through (\\d+),(\\d+)");
        var togglePattern = Pattern.compile("toggle (\\d+),(\\d+) through (\\d+),(\\d+)");
        var commandMatcherFactory =
                Function3.of(CommandMatcher::createCommandMatcher)
                        .curried()
                        .apply(LightGuardian::coordinatesFrom);
        var turnOnCommandMatcher = commandMatcherFactory.apply(turnOnPattern).apply(TurnOnCommand::new);
        var turnOffCommandMatcher =
                commandMatcherFactory.apply(turnOffPattern).apply(TurnOffCommand::new);
        var toggleCommandMatcher = commandMatcherFactory.apply(togglePattern).apply(ToggleCommand::new);

        return new LightGuardian(
                List.of(turnOffCommandMatcher, turnOnCommandMatcher, toggleCommandMatcher)
        );
    }

    public int howManyLightsOn() {
        return lightGrid.numberOfLightsOn();
    }

    public void receive(String commandString) {
        lightGrid.accept(parseCommand(commandString));
    }

    private Command parseCommand(String commandString) {
        return chain.stream()
                .map(commandMatcher -> commandMatcher.match(commandString))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(newInvalidCommandException(commandString));
    }

    private static Supplier<InvalidCommandException> newInvalidCommandException(
            String commandString
    ) {
        return () -> {
            var message = "invalid command \"" + commandString + "\"";
            logger.info(message);
            return new InvalidCommandException(message);
        };
    }

    private static Tuple2<From, To> coordinatesFrom(Matcher matcher) {
        var x1 = Integer.parseInt(matcher.group(1));
        var y1 = Integer.parseInt(matcher.group(2));
        var x2 = Integer.parseInt(matcher.group(3));
        var y2 = Integer.parseInt(matcher.group(4));
        return new Tuple2<>(From.of(x1, y1), To.of(x2, y2));
    }
}
