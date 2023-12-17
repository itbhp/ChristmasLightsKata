package it.twinsbrain.dojos.commands;

import it.twinsbrain.dojos.values.From;
import it.twinsbrain.dojos.values.To;

public record TurnOnCommand(From from, To to) implements Command {}
