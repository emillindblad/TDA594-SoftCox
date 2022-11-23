package zyx.megabot.behaviour;

import robocode.Condition;
import zyx.megabot.bot.Enemy;
import zyx.megabot.bot.Me;

public abstract class Behaviour extends Condition {
  protected static Me me_;
  public static void StaticInit() {
    me_ = Me.Instance();
  }
  public abstract void Activate();

  public double FirePower(Enemy enemy) {
    double power = 3;
    double distance = enemy.distance_;
    if ( distance < 150 ) {
      power = Math.min(3, power);
    } else if ( distance < 300 ) {
      power = Math.min(2.5, power);
    } else {
      power = Math.min(1.9, power);
    }
    double my_energy = me_.energy_;
    if ( my_energy < 3 + 1e-9 ) {
      if ( distance < 80 ) return Math.min(3, my_energy) - 5e-5;
      if ( distance < 300 ) return Math.min(3, my_energy) * 0.1 - 5e-5;
      return 0;
    } else  if ( my_energy < 20 ) {
      power = Math.min(1.5, power);
    }
    return power;
  }
}
