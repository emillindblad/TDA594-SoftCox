package zyx.megabot.movement;

import java.awt.geom.Line2D;
import java.util.ArrayList;

import robocode.Bullet;
import robocode.Rules;
import zyx.megabot.battle.State;
import zyx.megabot.bot.Enemy;
import zyx.megabot.bot.Me;
import zyx.megabot.debug.Painter;
import zyx.megabot.equipment.Equipment;
import zyx.megabot.geometry.Point;
import zyx.megabot.utils.GamePhysics;

public class MoveManager {
  protected static Me me_;
  public static void StaticInit() { me_ = Me.Instance(); }
  private Enemy enemy_;
  public ArrayList<EnemyBasedMovement> movements_;
  public ArrayList<SurfingWave> waves_;
  private Bullet last_bullet_;
  private boolean hit_me_;

  public MoveManager(Enemy enemy) {
    enemy_ = enemy;
    movements_ = new ArrayList<EnemyBasedMovement>();
  }
  public void Init() {
    waves_ = new ArrayList<SurfingWave>();
    last_bullet_ = null;
  }
  public void Add(EnemyBasedMovement movement) {
    movements_.add(movement);
  }
  public void Update() {
    CreateWave();
    UpdateWaves();
    if ( last_bullet_ != null ) {
      if ( hit_me_ ) UpdateHitWave(last_bullet_);
      else UpdateHitBullet(last_bullet_);
      last_bullet_ = null;
    }
  }
  private void CreateWave() {
    if ( enemy_.last_shot_ != State.time_ ) return;
    /* Wave class */
    SurfingWave wave = new SurfingWave(State.time_ - 1);
    wave.Set(enemy_.log_.get(1));
    wave.bearing_ = enemy_.log_.get(2).bearing_ + GamePhysics.PI;
    wave.direction_ = enemy_.log_.get(2).my_direction_;
    wave.velocity_ = Rules.getBulletSpeed(enemy_.shot_power_);
    /* SegmentedWave class */
    wave.distance_ = enemy_.log_.get(2).distance_;
    wave.lateral_velocity_ = Math.abs(enemy_.log_.get(2).my_lateral_velocity_);
    /* SurfingWave class */
    for (EnemyBasedMovement movement : movements_) {
      if ( !Equipment.Active(movement) ) continue;
      wave.movements_.add(movement);
    }
    waves_.add(wave);
  }
  private void UpdateWaves() {
    for ( int i = 0; i < waves_.size(); ++i ) {
      SurfingWave wave = waves_.get(i);
      wave.Update(State.time_);
      double bearing = GamePhysics.Angle(wave, me_);
      Point end_point = (new Point(wave, bearing, wave.radius_));
      Line2D.Double beam = new Line2D.Double(wave.Point2D(), end_point.Point2D());
      wave.hitting_me_ = false;
      if ( me_.bounding_rectangle_.intersectsLine(beam) ) {
        if ( me_.bounding_rectangle_.contains(end_point.Point2D()) ) {
          wave.hitting_me_ = true;
          Painter.Draw(beam, 0, 0, 255);
          if ( !wave.flagged_ ) {
            wave.flagged_ = true;
            for (EnemyBasedMovement movement : wave.movements_) {
              movement.Update(wave, null);
            }
          }
        } else if ( wave.Done() ) {
          waves_.remove(i--);
        }
      }
      if ( wave.ttl_ == SurfingWave.START_TTL ) Painter.Draw(wave, 0, 0, 255);
      else Painter.Draw(wave, 255, 255, 255);
    }
  }
  public void Update(Bullet bullet, boolean hit_me) {
    last_bullet_ = bullet;
    hit_me_ = hit_me;
  }
  private void UpdateHitWave(Bullet bullet) {
    double velocity = bullet.getVelocity();
    for (int i = 0; i < waves_.size(); ++i ) {
      SurfingWave wave = waves_.get(i);
      if ( Math.abs(velocity - wave.velocity_) < 1e-1 &&
           wave.hitting_me_ ) {
        for (EnemyBasedMovement movement : wave.movements_) {
          movement.Update(wave, bullet);
        }
        waves_.remove(i--);
        break;
      }
    }
  }
  private void UpdateHitBullet(Bullet bullet) {
    double velocity = bullet.getVelocity();
    for (int i = 0; i < waves_.size(); ++i ) {
      SurfingWave wave = waves_.get(i);
      double distance = GamePhysics.Distance(wave, me_);
      if ( Math.abs(velocity - wave.velocity_) < 1e-1 &&
           Math.abs(wave.radius_ - distance) < 50 ) {
        for (EnemyBasedMovement movement : wave.movements_) {
          movement.Update(wave, bullet);
        }
        waves_.remove(i--);
        break;
      }
    }
  }
  public Move Move() {
    for (EnemyBasedMovement movement : movements_) {
      if ( !Equipment.Active(movement) ) continue;
      Move move = movement.Move(waves_);
      if ( move != null ) return move;
    }
    return null;
  }
}
