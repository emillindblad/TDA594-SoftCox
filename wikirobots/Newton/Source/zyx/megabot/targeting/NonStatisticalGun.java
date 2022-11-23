package zyx.megabot.targeting;

import zyx.megabot.bot.Enemy;
import zyx.megabot.wave.ShootingWave;

public abstract class NonStatisticalGun extends Gun {
  public NonStatisticalGun(Enemy enemy) {
    super(enemy);
  }
  public void Update(ShootingWave wave) {}
}
