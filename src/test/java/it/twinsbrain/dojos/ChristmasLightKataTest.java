package it.twinsbrain.dojos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ChristmasLightKataTest {

  @ParameterizedTest(name = "Turning lights on from ({0}, {1}) to ({2}, {3}) total of {4}")
  @CsvSource({
          "0,1,0,9,9",
          "0,1,0,3,3",
  })
  void lightOnAfterTurnOnCommandInOneDimension(
          int x1, int y1, int x2, int y2, int expected
  ) {
      var lightGuardian  = new LightGuardian();

      lightGuardian.receive("turn on %d,%d through %d,%d".formatted(x1, y1, x2, y2));

      assertEquals(expected, lightGuardian.howManyLightsOn());
  }
}
