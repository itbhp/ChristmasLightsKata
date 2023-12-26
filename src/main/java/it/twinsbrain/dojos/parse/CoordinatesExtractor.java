package it.twinsbrain.dojos.parse;

import it.twinsbrain.dojos.values.From;
import it.twinsbrain.dojos.values.To;
import java.util.regex.Matcher;
import javaslang.Tuple2;

public interface CoordinatesExtractor {
    Tuple2<From, To> extractFrom(Matcher matcher);
}
