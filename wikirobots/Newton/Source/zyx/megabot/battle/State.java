package zyx.megabot.battle;

import zyx.megabot.MegaBot;
import zyx.megabot.bot.Me;

public class State {
  public static int round_;
  public static long time_;
  public static int others_;
  public static boolean _1v1_;
  public static void Update() {
    MegaBot robot = Me.Robot();
    round_ = robot.getRoundNum();
    time_ = robot.getTime();
    others_ = robot.getOthers();
    _1v1_ = others_ < 2;
  }
}
