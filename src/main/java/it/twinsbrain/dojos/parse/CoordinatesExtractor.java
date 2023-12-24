package it.twinsbrain.dojos.parse;

import it.twinsbrain.dojos.values.From;
import it.twinsbrain.dojos.values.Pair;
import it.twinsbrain.dojos.values.To;
import java.util.regex.Matcher;

public interface CoordinatesExtractor {
    Pair<From, To> extractFrom(Matcher matcher);
}
