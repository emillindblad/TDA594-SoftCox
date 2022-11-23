package zyx.megabot.equipment;

import zyx.megabot.bot.Enemy;

public abstract class EnemyBasedItem extends Item {
  protected Enemy enemy_;
  public EnemyBasedItem(Enemy enemy) {
    enemy_ = enemy;
  }
}
