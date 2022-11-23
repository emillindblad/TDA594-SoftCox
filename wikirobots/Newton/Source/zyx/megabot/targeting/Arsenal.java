package zyx.megabot.targeting;

import java.awt.geom.Line2D;
import java.util.ArrayList;

import robocode.Rules;
import robocode.util.Utils;
import zyx.megabot.battle.State;
import zyx.megabot.bot.Enemy;
import zyx.megabot.bot.Me;
import zyx.megabot.debug.Painter;
import zyx.megabot.equipment.Equipment;
import zyx.megabot.geometry.Point;
import zyx.megabot.movement.SurfingWave;
import zyx.megabot.utils.GamePhysics;
import zyx.megabot.utils.Range;
import zyx.megabot.wave.ShootingWave;

public class Arsenal {
  protected static Me me_;
  public static void StaticInit() { me_ = Me.Instance(); }

  private ArrayList<Gun> guns_;
  private Gun best_gun_;
  private ShootingWave next_wave_;
  private Enemy enemy_;
  private ArrayList<ShootingWave> waves_;
  private Gun last_gun_;

  public Arsenal(Enemy enemy) {
    enemy_ = enemy;
    guns_ = new ArrayList<Gun>();
  }
  public void Init() {
    waves_ = new ArrayList<ShootingWave>();
    best_gun_ = null;
    next_wave_ = null;
    last_gun_ = null;
  }
  public void Add(Gun gun) {
    guns_.add(gun);
  }
  public double AimAngle() {
    if ( best_gun_ == null ) return Double.POSITIVE_INFINITY;
    if ( next_wave_ == null ) return Double.POSITIVE_INFINITY;
    return best_gun_.AimAngle(State.time_, next_wave_);
  }
  public void Update() {
    ShootWave();
    CreateWave();
    UpdateWaves();
    UpdateGuns();
    if ( next_wave_ != null ) next_wave_.gun_ = best_gun_;
  }
  private void ShootWave() {
    if ( Me.Robot().ShootWave() && next_wave_ != null ) {
      if ( last_gun_ != next_wave_.gun_ ) {
        Me.printf("switching to %s (%.2f%%)\n", best_gun_.Name(), next_wave_.gun_.Rating(next_wave_) * 100);
      }
      last_gun_ = next_wave_.gun_;
      next_wave_.Set(me_);
      for (VirtualBullet virtual_bullet : next_wave_.virtual_bullets_) {
        virtual_bullet.SetFirePosition(me_);
      }
      //Me.printf("wave(%d): %.2f, %.2f %.2f\n", Me.Robot().getTime(), next_wave_.x_, next_wave_.y_, me_.gun_heading_);
      waves_.add(next_wave_);
    }
    next_wave_ = null;
  }
  private void CreateWave() {
    if ( enemy_.scan_time_ != State.time_ ) return;
    /* Wave class */
    next_wave_ = new ShootingWave(State.time_ + 1);
    next_wave_.bearing_ = enemy_.bearing_;
    next_wave_.velocity_ = Rules.getBulletSpeed(enemy_.fire_power_);
    next_wave_.direction_ = enemy_.direction_;
    /* SegmentedWave class */
    next_wave_.lateral_velocity_ = Math.abs(enemy_.lateral_velocity_);
    next_wave_.distance_ = enemy_.distance_;
    next_wave_.fwd_wall_hit_time_ = enemy_.fwd_wall_hit_time_;
    next_wave_.bck_wall_hit_time_ = enemy_.bck_wall_hit_time_;
    next_wave_.time_direction_ = enemy_.time_direction_;
    next_wave_.time_running_ = enemy_.time_running_;
    next_wave_.acceleration_ = enemy_.acceleration_;
    next_wave_.WrapUp();
    /* ShootingWave class */
    double tank_turn = me_.NextTurn();
    for (Gun gun : guns_) {
      if ( !Equipment.Active(gun) ) continue;
      double angle = gun.AimAngle(State.time_, next_wave_);
      double turn = Range.CapLowHigh(
          Utils.normalRelativeAngle(angle - me_.gun_heading_),
          tank_turn - GamePhysics.PI_9,
          tank_turn + GamePhysics.PI_9);
      angle = GamePhysics.AbsoluteAngle(me_.gun_heading_, turn);
      next_wave_.Add(new VirtualBullet(gun, angle));
    }
  }
  private void UpdateWaves() {
    for ( int i = 0; i < waves_.size(); ++i ) {
      ShootingWave wave = waves_.get(i);
      wave.Update(State.time_);
      double bearing = GamePhysics.Angle(wave, enemy_);
      Point end_point = (new Point(wave, bearing, wave.radius_));
      Line2D.Double beam = new Line2D.Double(wave.Point2D(), end_point.Point2D());
      if ( enemy_.bounding_rectangle_.intersectsLine(beam) ) {
        if ( enemy_.bounding_rectangle_.contains(end_point.Point2D()) ) {
          for (VirtualBullet virtual_bullet : wave.virtual_bullets_) {
            Gun gun = virtual_bullet.gun_;
            gun.Update(wave);
            if ( !virtual_bullet.flagged_ && enemy_.bounding_rectangle_.intersectsLine(virtual_bullet.line_) ) {
              virtual_bullet.flagged_ = true;
              gun.LogHit(wave);
              if ( gun == wave.gun_ ) {
                enemy_.UpdateAccuracy(1);
              }
              /*
              if ( enemy_.bounding_rectangle_.contains(virtual_bullet.line_.getP1()) ) {
                Me.printf("virtual hit: %s: %.2f, %.2f\n", gun.Name(), virtual_bullet.line_.x1, virtual_bullet.line_.y1);
              } else {
                Me.printf("virtual hit: %s: %.2f, %.2f\n", gun.Name(), virtual_bullet.line_.x2, virtual_bullet.line_.y2);
              }
              */
            }
          }
          Painter.Draw(beam, 255, 0, 0);
        } else if ( wave.Done() ) {
          for (VirtualBullet virtual_bullet : wave.virtual_bullets_) {
            if ( !virtual_bullet.flagged_ ) {
              virtual_bullet.gun_.LogMiss(wave);
              if ( virtual_bullet.gun_ == wave.gun_ ) {
                enemy_.UpdateAccuracy(0);
              }
            }
          }
          waves_.remove(i--);
        }
      }
      if ( wave.ttl_ == SurfingWave.START_TTL ) Painter.Draw(wave, 255, 0, 0);
      else Painter.Draw(wave, 0, 0, 0);
    }
  }
  public void UpdateGuns() {
    best_gun_ = null;
    double best_rating = -1;
    for (Gun gun : guns_) {
      if ( !Equipment.Active(gun) ) continue;
      double rating_ = gun.Rating(next_wave_);
      if ( best_gun_ == null || rating_ > best_rating ) {
        best_gun_ = gun;
        best_rating = rating_;
      }
    }
    if ( best_gun_ == null ) {
      Me.printf("no active guns\n");
      enemy_.fire_power_ = 0;
    }
  }
}
