package zyx.megabot.utils;

import robocode.util.Utils;
import zyx.megabot.geometry.Point;

public class GamePhysics {

  public static final double PI = Math.PI;
  public static final double PI2 = PI * 2;
  public static final double HALF_PI = PI / 2;
  public static final double PI_2 = HALF_PI;
  public static final double PI_9 = PI / 9;
  private static final double PI_18 = PI / 18;
  private static final double PI_240 = PI / 240;
  public static final double PI_360 = PI / 360;

  public static double COOLING_RATE;

  public static double RelativeAngle(double bearing, double heading) {
    return Utils.normalRelativeAngle(bearing - heading);
  }

  public static double AngleDifference(double angle0, double angle1) {
    return Math.min(
        Math.abs(angle1 - angle0),
        Math.min(angle0, angle1) + GamePhysics.PI2 - Math.max(angle0, angle1));
  }

  public static double Angle(Point p0, Point p1) {
    return Math.atan2(p1.x_ - p0.x_, p1.y_ - p0.y_);
  }

  public static double MaxRobotTurn(double velocity) {
    return PI_18 - PI_240 * Math.abs(velocity);
  }

  public static double AbsoluteAngle(double heading, double bearing) {
    return Utils.normalAbsoluteAngle(heading + bearing);
  }

  public static double Square(double x) {
    return x * x;
  }

  public static double Distance(Point p0, Point p1) {
    return Math.sqrt(Square(p1.x_ - p0.x_) + Square(p1.y_ - p0.y_));
  }
}
