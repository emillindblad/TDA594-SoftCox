package zyx.mega.movement;

import java.util.ArrayList;

import robocode.Bullet;
import robocode.HitWallEvent;
import zyx.mega.wave.GuessFactor;
import zyx.megabot.battle.State;
import zyx.megabot.bot.Bot;
import zyx.megabot.bot.Enemy;
import zyx.megabot.geometry.Point;
import zyx.megabot.movement.EnemyBasedMovement;
import zyx.megabot.movement.Move;
import zyx.megabot.movement.SurfingWave;
import zyx.megabot.movement.WallSmoothing;
import zyx.megabot.segmentation.ArraySegmentation;
import zyx.megabot.segmentation.SegmentedArray;
import zyx.megabot.utils.GamePhysics;
import zyx.megabot.utils.RollingAverage;

public abstract class AbstractVCS_TrueWaveSurfing extends EnemyBasedMovement {
  protected ArraySegmentation bins_;
  protected double MIN_DISTANCE;
  protected double MAX_DISTANCE;
  protected int BINS;

  protected abstract void SetUp();
  public AbstractVCS_TrueWaveSurfing(Enemy enemy) {
    super(enemy);
    bins_ = new ArraySegmentation();
    bins_.rolling_depth_ = 17;
    MIN_DISTANCE = 250;
    MAX_DISTANCE = 400;
    BINS = 63;
    SetUp();
    bins_.SetUp(BINS);
  }
  public void Update(SurfingWave wave, Bullet bullet) {
    SegmentedArray segment = bins_.UpdateData(wave);
    Point hit_point = (bullet == null) ?
        (me_) : (new Point(bullet.getX(), bullet.getY()));
    double factor = GuessFactor.Factor(wave, hit_point);
    int index = GuessFactor.Index(factor, BINS);
    /**
    Me.printf("updating surf stats: %.2f (%d) (%s)\n" +
    		"[%.2f, %.2f] [%.2f, %.2f] [%.2f, %.2f]\n" +
    		"%.2f, %.2f, %d\n",
    		factor, index, hit_point,
    		wave.x_, wave.y_, me_.x_, me_.y_, hit_point.x_, hit_point.y_,
    		wave.bearing_, GuessFactor.MAE(wave), wave.direction_
    );
    /**/
    double weight = bullet != null ? 1 : 0.01;
    for ( int i = 0; i < BINS; ++i ) {
      double value = BINS / (1 + GamePhysics.Square(i - index));
      segment.data_[i] = RollingAverage.Roll(segment.data_[i], value, segment.depth_, weight);
    }
  }
  public Move Move(ArrayList<SurfingWave> waves) {
    if ( waves.size() == 0 ) return null;
    SurfingWave wave = waves.get(0);
    SegmentedArray segment = bins_.Data(wave);
    double stay_danger = CheckDanger(wave, segment, me_);
    double cw_danger = CheckDanger(wave, segment, me_, 1);
    double ccw_danger = CheckDanger(wave, segment, me_, -1);
    Move move = new Move();
    if ( cw_danger < ccw_danger ) {
      move.heading_ = WallSmoothing.Smooth(
          me_,
          GamePhysics.Angle(wave, me_) + GamePhysics.HALF_PI,
          1);
    } else {
      move.heading_ = WallSmoothing.Smooth(
          me_,
          GamePhysics.Angle(wave, me_) - GamePhysics.HALF_PI,
          -1);
    }
    if ( stay_danger < Math.min(ccw_danger, cw_danger) ) {
      move.distance_ = 0;
    } else {
      move.distance_ = 100;
    }
    return move;
  }
  private double CheckDanger(SurfingWave wave, SegmentedArray segment, Bot bot, int direction) {
    bot = new Bot(bot);
    double danger = Double.POSITIVE_INFINITY;
    for ( int i = 1; i < 500; ++i ) {
      double angle = WallSmoothing.Smooth(
        bot,
        GamePhysics.Angle(wave, bot) + direction * GamePhysics.HALF_PI,
        direction);
      bot.Move(angle, true, direction);
      danger = Math.min(danger, CheckDanger(wave, segment, bot));
      double distance = GamePhysics.Distance(wave, bot);
      double travelled = (State.time_ - wave.time_ + i) * wave.velocity_;
      if ( distance <= travelled ) {
        break;
      }
    }
    return danger;
  }
  private double CheckDanger(SurfingWave wave, SegmentedArray segment, Point point) {
    double factor = GuessFactor.Factor(wave, point);
    int index = GuessFactor.Index(factor, BINS);
    double distance = GamePhysics.Distance(point, enemy_);
    double extra = 0;
    if ( distance < MIN_DISTANCE ) {
      extra = (MIN_DISTANCE - distance) / GamePhysics.Square(MIN_DISTANCE);
    } else if ( distance > MAX_DISTANCE ) {
      extra = (distance - MAX_DISTANCE) / GamePhysics.Square(MAX_DISTANCE);
      extra *= 0.01;
    }
    /**
    Me.printf("danger: %.2f, %.2f -> %.2f (%.2f, %d)\n",
        segment.data_[index],
        extra,
        segment.data_[index] + extra * 0.1,
        factor, index);
    /**/
    return segment.data_[index] + extra;
  }
  public void Update(HitWallEvent event) {}
}
