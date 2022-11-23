package zyx.mega.movement;

import java.util.ArrayList;

import robocode.HitWallEvent;

import zyx.megabot.bot.Enemy;
import zyx.megabot.movement.Move;
import zyx.megabot.movement.NonStatisticalEnemyBasedMovement;
import zyx.megabot.movement.SurfingWave;

public class RamMovement extends NonStatisticalEnemyBasedMovement {
  public RamMovement(Enemy enemy) {
    super(enemy);
  }
  public String Name() {
    return "Ram Movement";
  }
  public Move Move(ArrayList<SurfingWave> waves) {
    Move move = new Move();
    move.heading_ = enemy_.bearing_;
    move.distance_ = 100;
    return move;
  }
  public void Update(HitWallEvent event) {}
}
