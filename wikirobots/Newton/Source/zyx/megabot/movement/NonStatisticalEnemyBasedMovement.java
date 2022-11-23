package zyx.megabot.movement;

import robocode.Bullet;
import zyx.megabot.bot.Enemy;

public abstract class NonStatisticalEnemyBasedMovement extends EnemyBasedMovement {
  public NonStatisticalEnemyBasedMovement(Enemy enemy) {
    super(enemy);
  }
  public final void Update(SurfingWave wave, Bullet bullet) {}
}
