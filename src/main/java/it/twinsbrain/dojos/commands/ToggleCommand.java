package it.twinsbrain.dojos.commands;

import it.twinsbrain.dojos.values.From;
import it.twinsbrain.dojos.values.To;

public record ToggleCommand(From from, To to) implements Command {}
