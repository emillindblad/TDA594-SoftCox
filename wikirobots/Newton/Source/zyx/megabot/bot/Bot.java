package zyx.megabot.bot;

import robocode.Rules;
import robocode.util.BoundingRectangle;
import robocode.util.Utils;
import zyx.megabot.battlefield.BattleField;
import zyx.megabot.debug.Painter;
import zyx.megabot.geometry.Point;
import zyx.megabot.utils.GamePhysics;
import zyx.megabot.utils.Range;

public class Bot extends Point {
  protected static Me me_;
  public static double WIDTH;
  public static double HEIGHT;
  public static void StaticInit() { me_ = Me.Instance(); }

  public double energy_;
  public double heading_;
  public double velocity_;
  public double gun_heat_;
  public int fwd_wall_hit_time_;
  public int bck_wall_hit_time_;
  public BoundingRectangle bounding_rectangle_;
  public Bot() {
    bounding_rectangle_ = new BoundingRectangle();
  }
  public Bot(Bot bot) {
    super(bot);
    energy_ = bot.energy_;
    heading_ = bot.heading_;
    velocity_ = bot.velocity_;
    gun_heat_ = bot.gun_heat_;
    bounding_rectangle_ = new BoundingRectangle(
        bot.bounding_rectangle_.x, bot.bounding_rectangle_.y,
        bot.bounding_rectangle_.width, bot.bounding_rectangle_.height);
  }
  public void Move(double angle) {
    Move(angle, true, 0);
  }
  public void Move(double angle, boolean back_as_front, int direction) {
    double turn = GamePhysics.RelativeAngle(angle, heading_);
    int baf_direction = Turn(turn);
    if ( back_as_front ) direction = baf_direction;
    int acceleration = ((velocity_ * direction < 0) ? 2 : 1);
    velocity_ = Range.CapCentered(velocity_ + acceleration * direction, Rules.MAX_VELOCITY);
    gun_heat_ -= GamePhysics.COOLING_RATE;
    Move(heading_, velocity_);
    UpdateBoundingRectangle();
  }
  public double MaxTurn() {
    return GamePhysics.MaxRobotTurn(velocity_);
  }
  protected void UpdateBoundingRectangle() {
    bounding_rectangle_.setRect(x_ - 18, y_ - 18, 36, 36);
    Painter.Draw(bounding_rectangle_, 0, 0, 255);
  }
  public int Turn(double turn) {
    int direction = 1;
    if ( Math.cos(turn) < 0 ) {
      turn += GamePhysics.PI;
      direction = -1;
    }
    turn = Range.CapCentered(Utils.normalRelativeAngle(turn), MaxTurn());
    heading_ = GamePhysics.AbsoluteAngle(heading_, turn);
    return direction;
  }
  public void TurnAndMove(double turn, int direction) {
    Move(heading_ + turn, false, direction);
  }
  protected void UpdateWallHitTime() {
    double fwd_heading = heading_;
    double bck_heading = heading_;
    if ( velocity_ < 0) fwd_heading += GamePhysics.PI;
    else bck_heading += GamePhysics.PI;
    fwd_wall_hit_time_ = WallHitTime(fwd_heading);
    bck_wall_hit_time_ = WallHitTime(bck_heading);
  }
  private int WallHitTime(double heading) {
    int low = 0, high = 500;
    Point p = new Point();
    for ( int iter = 0; iter < 15 && low < high; ++iter ) {
      int mid = (low + high) / 2;
      p.Project(this, heading, mid * 8);
      if ( BattleField.RECTANGLE.contains(p.Point2D()) ) {
        low = mid + 1;
      } else {
        high = mid;
      }
    }
    return (low + high) / 2;
  }
}
