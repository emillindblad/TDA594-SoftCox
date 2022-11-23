package zyx.megabot.behaviour;

import zyx.megabot.battle.State;
import zyx.megabot.bot.Enemy;
import zyx.megabot.bot.Me;

public abstract class Disabled1v1 extends Behaviour {
  public boolean test() {
    Enemy target = Me.Robot().target_;
    if ( target == null ) return false;
    return target.energy_ == 0 && State._1v1_;
  }
}
