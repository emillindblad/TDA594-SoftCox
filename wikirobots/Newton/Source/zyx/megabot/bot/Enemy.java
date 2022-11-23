package zyx.megabot.bot;

import java.util.ArrayList;

import robocode.Bullet;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import zyx.megabot.battle.State;
import zyx.megabot.equipment.Equipment;
import zyx.megabot.equipment.Item;
import zyx.megabot.equipment.ItemId;
import zyx.megabot.movement.EnemyBasedMovement;
import zyx.megabot.utils.GamePhysics;
import zyx.megabot.utils.Range;
import zyx.megabot.utils.RollingAverage;

public class Enemy extends EnemyInfo {
  private static ArrayList<Enemy> enemies_ = new ArrayList<Enemy>();
  public static Enemy Find(String name) {
    if ( name.equalsIgnoreCase(Me.Robot().getName()) ) {
      try {
        throw new Error();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    for (Enemy enemy : enemies_) {
      if ( enemy.name_.equals(name) ) return enemy;
    }
    Enemy enemy = new Enemy(name);
    enemies_.add(enemy);
    return enemy ;
  }
  public static void StaticInit() {
    for (Enemy enemy : enemies_) {
      enemy.Init();
    }
  }
  public static void StaticUpdate() {
    for (Enemy enemy : enemies_) {
      enemy.Update();
    }
  }

  public String name_;
  public boolean scanned_;
  public Equipment equipment_;
  public double fire_power_;
  public int current_log_size_;
  public ArrayList<EnemyInfo> log_;
  public long last_shot_;
  public double shot_power_;
  public double max_velocity_;
  public RollingAverage avg_accuracy_;
  
  
  private Enemy(String name) {
    name_ = name;
    equipment_ = new Equipment(this);
    log_ = new ArrayList<EnemyInfo>();
    gun_heat_ = me_.gun_heat_ + GamePhysics.COOLING_RATE;
    avg_accuracy_ = new RollingAverage(11);
    max_velocity_ = 8;
    Me.Robot().FirstLook(this);
    Init();
  }
  public void Init() {
    current_log_size_ = 0;
    my_direction_ = direction_ = 1;
    equipment_.Init();
  }
  public ItemId Add(Item item, ItemId id) {
    return equipment_.Add(item, id);
  }
  private void Update() {
    UpdateFirePower();
    //equipment_.Update();
  }

  private void UpdateFirePower() {
    fire_power_ = Me.Robot().FirePower(this);
  }
  public void Update(BulletHitEvent event) {
    Bullet bullet = event.getBullet();
    energy_ -= Rules.getBulletDamage(bullet.getPower());
  }
  public void Update(BulletHitBulletEvent event, Bullet enemy_bullet, Bullet my_bullet) {
    equipment_.move_manager_.Update(enemy_bullet, false);
  }
  public void Update(HitByBulletEvent event) {
    Bullet bullet = event.getBullet();
    energy_ += Rules.getBulletHitBonus(bullet.getPower());
    equipment_.move_manager_.Update(bullet, true);
  }
  public void Update(HitRobotEvent event) {
  }
  public void Update(RobotDeathEvent event) {
  }
  public void Update(ScannedRobotEvent event) {
    EnemyInfo previous_me = current_log_size_ > 0 ? log_.get(0) : null;
    double bearing = me_.heading_ + event.getBearingRadians();
    double distance = event.getDistance();
    double energy_drop = energy_ - event.getEnergy();
    //Me.printf("energy: %.4f\n", event.getEnergy());
    if ( event.getEnergy() == 0 ) Me.printf("Disabled Disabled Disabled\nDisabled Disabled Disabled\nDisabled Disabled Disabled\n");
    /* Point class */
    Project(me_, bearing, distance);
    /* Bot class */
    energy_ = event.getEnergy();
    heading_ = event.getHeadingRadians();
    velocity_ = event.getVelocity();
    gun_heat_ = Range.CapLow(gun_heat_ - GamePhysics.COOLING_RATE, 0);
    UpdateBoundingRectangle();
    UpdateWallHitTime();
    //Me.printf("wh: %d %d\n", fwd_wall_hit_time_, bck_wall_hit_time_);

    /* EnemyInfo class */
    scan_time_ = event.getTime();
    bearing_ = bearing;
    distance_ = event.getDistance();
    lateral_velocity_ = velocity_ * Math.sin(heading_ - bearing_);
    my_lateral_velocity_ = me_.velocity_ * Math.sin(event.getBearingRadians());
    if ( velocity_ != 0 ) direction_ = lateral_velocity_ <= 0 ? -1 : 1;
    if ( me_.velocity_ != 0 ) my_direction_ = my_lateral_velocity_ <= 0 ? -1 : 1;
    /* Enemy class */
    if ( /*!robot_hit_ && */ previous_me != null ) {
      if ( Math.abs(velocity_ - previous_me.velocity_) > 2 ) {
        //Me.printf("Wall hit wall hit: %.2f\n", Rules.getWallHitDamage(previous_me.velocity_));
        energy_drop -= Rules.getWallHitDamage(previous_me.velocity_);
      }
    }
    if ( gun_heat_ < GamePhysics.COOLING_RATE &&
        energy_drop + 1e-9 >= Rules.MIN_BULLET_POWER && energy_drop - 1e-9 <= Rules.MAX_BULLET_POWER ) {
      last_shot_ = State.time_;
      shot_power_ = energy_drop;
      gun_heat_ = Rules.getGunHeat(shot_power_) - GamePhysics.COOLING_RATE;
    }
    max_velocity_ = Math.max(max_velocity_, Math.abs(velocity_));
    /* Relative Information */
    if ( previous_me != null ) {
      acceleration_ = 0;
      if ( velocity_ * previous_me.velocity_ >= 0 ) {
        if ( Math.abs(velocity_) > Math.abs(previous_me.velocity_) ) {
          acceleration_ = 1;
        } else if ( Math.abs(velocity_) < Math.abs(previous_me.velocity_) ) {
          acceleration_ = -1;
        }
      } else {
        acceleration_ = 1;
      }
      if ( previous_me.direction_ == direction_ ) {
        ++time_direction_;
      } else {
        time_direction_ = 0;
      }
    } else {
      acceleration_ = 0;
      time_direction_ = 0;
    }
    //Me.printf("enemy info: %.2f, %d\n", velocity_, acceleration_);
    if ( acceleration_ == 1 ||
         (Math.abs(Math.abs(velocity_) - max_velocity_) < 1e-1 &&
          acceleration_ == 0) ) {
      ++time_running_;
    } else {
      time_running_ = 0;
    }
    //Me.printf("times: %d %d\n", time_direction_, time_running_);
    /* Log It */
    ++current_log_size_;
    log_.add(0, new EnemyInfo(this));
  }
  public static void Update(HitWallEvent event) {
    for (Enemy enemy : enemies_) {
      for (EnemyBasedMovement movement : enemy.equipment_.move_manager_.movements_) {
        movement.Update(event);
      }
    }
  }
  public void UpdateAccuracy(double score) {
    avg_accuracy_.Roll(score, 1);
  }
}
