package it.twinsbrain.dojos;


import it.twinsbrain.dojos.commands.TurnOffCommand;
import it.twinsbrain.dojos.commands.TurnOnCommand;
import it.twinsbrain.dojos.values.From;
import it.twinsbrain.dojos.values.To;
import java.util.regex.Pattern;

public class LightGuardian {
  private final Pattern turnOnPattern = Pattern.compile("turn on (\\d),(\\d) through (\\d),(\\d)");
  private final Pattern turnOffPattern = Pattern.compile("turn off (\\d),(\\d) through (\\d),(\\d)");
  private final LightGrid lightGrid = new LightGrid();

  public void receive(String commandString) {
    var turnOnMatcher = turnOnPattern.matcher(commandString);
    if (turnOnMatcher.matches()) {
      var x1 = Integer.parseInt(turnOnMatcher.group(1));
      var y1 = Integer.parseInt(turnOnMatcher.group(2));
      var x2 = Integer.parseInt(turnOnMatcher.group(3));
      var y2 = Integer.parseInt(turnOnMatcher.group(4));
      lightGrid.accept(new TurnOnCommand(From.of(x1, y1), To.of(x2, y2)));
    }
    var turnOffMatcher = turnOffPattern.matcher(commandString);
    if (turnOffMatcher.matches()) {
      var x1 = Integer.parseInt(turnOffMatcher.group(1));
      var y1 = Integer.parseInt(turnOffMatcher.group(2));
      var x2 = Integer.parseInt(turnOffMatcher.group(3));
      var y2 = Integer.parseInt(turnOffMatcher.group(4));
      lightGrid.accept(new TurnOffCommand(From.of(x1, y1), To.of(x2, y2)));
    }
  }

  public int howManyLightsOn() {
    return lightGrid.numberOfLightsOn();
  }
}
