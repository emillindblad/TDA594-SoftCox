package zyx.megabot.bot;

import zyx.megabot.MegaBot;
import zyx.megabot.battle.State;
import zyx.megabot.utils.Range;

public class Me extends Ally {
  private static Me me_ = new Me();
  public static Me Instance() { return me_; }
  public static Me Instance(MegaBot robot) {
    robot_ = robot;
    return me_;
  }

  private static MegaBot robot_;
  public static MegaBot Robot() { return robot_; }
  public static void printf(String format, Object... args) {
    //robot_.out.printf(format, args);
  }

  public Ally next_me_;
  
  private Me() {
  }
  public void Update() {
    if ( robot_.getTime() == State.time_ ) return;
    State.Update();
    /* Point class */
    x_ = robot_.getX();
    y_ = robot_.getY();
    /* Bot class */
    energy_ = robot_.getEnergy();
    heading_ = robot_.getHeadingRadians();
    velocity_ = robot_.getVelocity();
    gun_heat_ = robot_.getGunHeat();
    /* Ally class */
    gun_heading_ = robot_.getGunHeadingRadians();
    radar_heading_ = robot_.getRadarHeadingRadians();
    UpdateBoundingRectangle();
    //Me.printf("now(%d): %.2f, %.2f %.2f\n", robot_.getTime(), robot_.getX(), robot_.getY(), robot_.getGunHeadingRadians());
  }
  public void UpdateNextMe() {
    next_me_ = new Ally(this);
    double remaining = robot_.getDistanceRemaining();
    int direction = (int) Math.signum(remaining);
    if ( direction == 0 && velocity_ != 0 ) {
      direction = -(int)Math.signum(velocity_);
    }
    next_me_.TurnAndMove(robot_.getTurnRemainingRadians(), direction);
    //Me.printf(" me(%d): %.2f, %.2f %.2f\n", robot_.getTime(), me_.x_, me_.y_, me_.gun_heading_);
    //Me.printf("nme(%d): %.2f, %.2f %.2f\n", robot_.getTime(), next_me_.x_, next_me_.y_, next_me_.gun_heading_);
  }
  public double NextTurn() {
    return Range.CapCentered(robot_.getTurnRemainingRadians(), MaxTurn());
  }
}
