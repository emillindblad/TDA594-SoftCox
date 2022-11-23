package zyx.mega.wave;

import robocode.Rules;
import robocode.util.Utils;
import zyx.megabot.geometry.Point;
import zyx.megabot.utils.GamePhysics;
import zyx.megabot.utils.Range;
import zyx.megabot.wave.Wave;

public class GuessFactor {
  public static double MAE(Wave wave) {
    return Math.asin(Rules.MAX_VELOCITY / wave.velocity_);
  }
  public static int Index(double factor, int bins) {
    return (int)Math.round(Range.Map(factor, -1, 1, 0, bins - 1));
  }
  public static double Factor(int index, int bins) {
    return Range.Map(index, 0, bins - 1, -1, 1);
  }
  public static double Factor(Wave wave, Point point) {
    double offset = Utils.normalRelativeAngle(GamePhysics.Angle(wave, point) - wave.bearing_);
    double mae = MAE(wave);
    //Me.printf("GF: %.2f, %.2f, %.2f\n", GamePhysics.Angle(wave, point), offset, mae);
    return Range.CapCentered(wave.direction_ * offset / mae, 1);
  }
}
