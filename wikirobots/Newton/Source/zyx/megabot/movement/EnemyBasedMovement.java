package zyx.megabot.movement;

import java.util.ArrayList;

import robocode.Bullet;
import robocode.HitWallEvent;

import zyx.megabot.bot.Enemy;
import zyx.megabot.equipment.EnemyBasedItem;

public abstract class EnemyBasedMovement extends EnemyBasedItem {
  public EnemyBasedMovement(Enemy enemy) {
    super(enemy);
  }

  public abstract Move Move(ArrayList<SurfingWave> waves_);
  public abstract void Update(SurfingWave wave, Bullet bullet);
  public abstract void Update(HitWallEvent event);
}
