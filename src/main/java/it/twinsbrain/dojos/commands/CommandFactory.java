package it.twinsbrain.dojos.commands;

import it.twinsbrain.dojos.values.From;
import it.twinsbrain.dojos.values.To;

public interface CommandFactory {
  Command create(From from, To to);
}
