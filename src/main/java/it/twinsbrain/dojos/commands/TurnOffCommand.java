package it.twinsbrain.dojos.commands;

import it.twinsbrain.dojos.values.From;
import it.twinsbrain.dojos.values.To;

public record TurnOffCommand(From from, To to) implements Command {}
