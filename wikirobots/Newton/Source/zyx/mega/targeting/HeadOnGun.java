package zyx.mega.targeting;

import zyx.megabot.bot.Enemy;
import zyx.megabot.targeting.NonStatisticalGun;
import zyx.megabot.wave.ShootingWave;

public class HeadOnGun extends NonStatisticalGun {
  public HeadOnGun(Enemy enemy) {
    super(enemy);
  }
  public String Name() {
    return "HeadOn Gun";
  }
  protected double AimAngle(ShootingWave wave) {
    return enemy_.bearing_;
  }
}
