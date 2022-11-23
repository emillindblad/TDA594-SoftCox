package zyx.mega.movement;

import robocode.HitWallEvent;
import zyx.megabot.bot.Enemy;
import zyx.megabot.movement.Move;
import zyx.megabot.movement.NonStatisticalEnemyBasedMovement;
import zyx.megabot.movement.WallSmoothing;
import zyx.megabot.utils.GamePhysics;

public abstract class OrbitalMovement extends NonStatisticalEnemyBasedMovement {
  protected int direction_;
  public OrbitalMovement(Enemy enemy) {
    super(enemy);
    direction_ = -1;
  }
  protected Move Move(double factor) {
    Move move = new Move();
    move.heading_ = WallSmoothing.Smooth(me_,
      enemy_.bearing_ + GamePhysics.PI * factor * direction_,
      direction_);
    move.distance_ = 100;
    /**
    Me.printf("Movement: %.2f, %.2f, %.2f\n",
        GamePhysics.PI * factor * direction_,
        enemy_.bearing_ + GamePhysics.PI * factor * direction_,
        move.heading_);
    /**/
    return move;
  }
  public void Update(HitWallEvent event) {
    direction_ = - direction_;
  }
}
