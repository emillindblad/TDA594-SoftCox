package zyx.megabot;

import java.awt.Graphics2D;

import robocode.AdvancedRobot;
import robocode.BattleEndedEvent;
import robocode.Bullet;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.Condition;
import robocode.CustomEvent;
import robocode.DeathEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.SkippedTurnEvent;
import robocode.WinEvent;
import zyx.megabot.battlefield.BattleField;
import zyx.megabot.behaviour.Behaviour;
import zyx.megabot.bot.Bot;
import zyx.megabot.bot.Enemy;
import zyx.megabot.bot.Me;
import zyx.megabot.debug.Painter;
import zyx.megabot.equipment.Equipment;
import zyx.megabot.equipment.Item;
import zyx.megabot.movement.Move;
import zyx.megabot.movement.MoveManager;
import zyx.megabot.targeting.Arsenal;
import zyx.megabot.utils.GamePhysics;

public abstract class MegaBot extends AdvancedRobot {
  protected static Me me_;

  protected abstract void Init();
  public abstract void FirstLook(Enemy enemy);

  public Behaviour current_behaviour_;
  private Behaviour last_behaviour_;
  public Enemy target_ ;
  public Bullet last_bullet_;
  public void run() {
    if ( getRoundNum() == 0 ) {
      StartBattle();
    }
    StartRound();
    while ( true ) {
      StartTurn();
      TakeTurn();
      FinishTurn();
    }
  }
  protected void Add(Behaviour behaviour) {
    addCustomEvent(behaviour);
  }
  public double FirePower(Enemy enemy) {
    if ( current_behaviour_ == null ) return 0;
    double power = enemy.energy_ / 3.9;
    if ( power > 1 ) {
      power = (enemy.energy_ + 2) / 5.9;
    }
    if ( me_.energy_ - 1e-5 < 0.1 ) return 0;
    return Math.min(power, current_behaviour_.FirePower(enemy));
  }
  private void StartBattle() {
    Bot.WIDTH = getWidth();
    Bot.HEIGHT = getHeight();
    BattleField.SetConstants(this);
    GamePhysics.COOLING_RATE = getGunCoolingRate();
  }
  private void StartRound() {
    /* StaticInit */
    me_ = Me.Instance(this);
    Bot.StaticInit();
    Enemy.StaticInit();
    Behaviour.StaticInit();
    Item.StaticInit();
    Arsenal.StaticInit();
    MoveManager.StaticInit();
    /* attributes */
    last_behaviour_ = null;
    target_ = null;
    /* Robocode Init */
    setAdjustGunForRobotTurn(true);
    setAdjustRadarForGunTurn(true);
    /* Specific Start */
    Init();
  }
  private void StartTurn() {
    me_.Update();
  }
  private void TakeTurn() {
    if ( current_behaviour_ == null ) {
      Me.printf("no behaviour for me: %d\n", getTime());
      if ( last_behaviour_ == null ) {
        Me.printf("skipping it\n");
        return;
      } else {
        current_behaviour_ = last_behaviour_;
        Me.printf("using last behaviuor\n");
      }
    }
    Equipment.DeActivateAll();
    current_behaviour_.Activate();
    //Me.printf("%d: %s\n", State.time_, current_behaviour_);
    last_bullet_ = null;
    if ( target_ != null && target_.fire_power_ != 0 ) {
      last_bullet_ = setFireBullet(target_.fire_power_);
    }
    Update();
    Move();
    AimGun();
    Scan();
  }
  private void Update() {
    Enemy.StaticUpdate();
  }
  private void Move() {
    Move move = null;
    if ( target_ != null ) {
      target_.equipment_.move_manager_.Update();
      move = target_.equipment_.move_manager_.Move();
    }
    if ( move != null ) {
      double angle = GamePhysics.RelativeAngle(move.heading_, me_.heading_);
      double distance = move.distance_;
      if ( Math.abs(angle) > GamePhysics.HALF_PI ) {
        if ( angle < 0 ) {
          angle += GamePhysics.PI;
        } else {
          angle -= GamePhysics.PI;
        }
        distance = -distance;
      }
      setTurnRightRadians(angle);
      setAhead(distance);
    }
    me_.UpdateNextMe();
  }
  private void AimGun() {
    if ( target_ == null ) return;
    target_.equipment_.arsenal_.Update();
    double angle = target_.equipment_.arsenal_.AimAngle();
    if ( angle == Double.POSITIVE_INFINITY ) angle = target_.bearing_;
    setTurnGunRightRadians(GamePhysics.RelativeAngle(angle, me_.gun_heading_));
  }
  private void Scan() {
    if ( target_ != null ) {
      double turn = GamePhysics.RelativeAngle(target_.bearing_, me_.radar_heading_);
      double magnitude = Math.abs(turn) * 1.9;
      int direction = turn < 0 ? -1 : 1;
      setTurnRadarRightRadians(direction * magnitude);
    }
    if ( getRadarTurnRemainingRadians() == 0 ) {
      setTurnRadarRightRadians(Math.PI);
    } 
  }
  private void FinishTurn() {
    last_behaviour_ = current_behaviour_;
    current_behaviour_ = null;
    Painter.Paint();
    execute();
  }
  public void onBattleEnded(BattleEndedEvent event) {
  }
  public void onBulletHit(BulletHitEvent event) {
    me_.Update();
    Enemy enemy = Enemy.Find(event.getName());
    enemy.Update(event);
    //Me.printf("real hit: %.2f, %.2f\n", event.getBullet().getX(), event.getBullet().getY());
  }
  public void onBulletHitBullet(BulletHitBulletEvent event) {
    me_.Update();
    Bullet enemy_bullet = event.getBullet();
    Bullet my_bullet = event.getHitBullet();
    if ( enemy_bullet.getName().equals(Me.Robot().getName()) ) {
      Bullet aux = enemy_bullet;
      enemy_bullet = my_bullet;
      my_bullet = aux;
    }
    if ( enemy_bullet.getName().equals(Me.Robot().getName()) ) {
      Me.printf("Both bullets are mine: %s: %s\n", event.getBullet().getName(), event.getHitBullet().getName());
      //while ( true );
      return;
    }
    Enemy enemy = Enemy.Find(enemy_bullet.getName());
    enemy.Update(event, enemy_bullet, my_bullet);
  }
  public void onBulletMissed(BulletMissedEvent event) {
  }
  public void onCustomEvent(CustomEvent event) {
    me_.Update();
    Condition condition = event.getCondition();
    if ( current_behaviour_ == null ) {
      if (condition instanceof Behaviour) {
        current_behaviour_ = (Behaviour) condition;
      }
    }
  }
  public void onDeath(DeathEvent event) {
  }
  public void onHitByBullet(HitByBulletEvent event) {
    me_.Update();
    Enemy enemy = Enemy.Find(event.getName());
    enemy.Update(event);
  }
  public void onHitRobot(HitRobotEvent event) {
    me_.Update();
    Enemy enemy = Enemy.Find(event.getName());
    enemy.Update(event);
  }
  public void onHitWall(HitWallEvent event) {
    Enemy.Update(event);
  }
  public void onPaint(Graphics2D g) {
    Painter.SetGraphics(g);
  }
  public void onRobotDeath(RobotDeathEvent event) {
    me_.Update();
    Enemy enemy = Enemy.Find(event.getName());
    enemy.Update(event);
    if ( target_ == enemy ) target_= null;
  }
  public void onScannedRobot(ScannedRobotEvent event) {
    me_.Update();
    Enemy enemy = Enemy.Find(event.getName());
    enemy.Update(event);
    target_ = enemy;
  }
  public void onSkippedTurn(SkippedTurnEvent event) {
  }
  public void onWin(WinEvent event) {
  }
  public boolean ShootWave() {
    return last_bullet_ != null;
  }
}
