package zyx.megabot.battlefield;

import robocode.util.BoundingRectangle;
import zyx.megabot.MegaBot;

public class BattleField {
  public static final double WALL = 22;
  public static double WIDTH;
  public static double HEIGHT;
  public static BoundingRectangle RECTANGLE;

  public static void SetConstants(MegaBot robot) {
    WIDTH = robot.getBattleFieldWidth();
    HEIGHT = robot.getBattleFieldHeight();
    RECTANGLE = new BoundingRectangle(
        WALL, WALL,
        WIDTH - WALL * 2,
        HEIGHT - WALL * 2);
  }
}
