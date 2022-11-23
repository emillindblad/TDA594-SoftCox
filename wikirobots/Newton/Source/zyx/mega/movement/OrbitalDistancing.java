package zyx.mega.movement;

import java.util.ArrayList;

import zyx.megabot.bot.Enemy;
import zyx.megabot.movement.Move;
import zyx.megabot.movement.SurfingWave;
import zyx.megabot.utils.GamePhysics;

public class OrbitalDistancing extends OrbitalMovement {
  protected static final int MIN_DISTANCE = 300;
  protected static final int MAX_DISTANCE = 400;
  public OrbitalDistancing(Enemy enemy) {
    super(enemy);
  }
  public String Name() {
    return "Orbital Distancing";
  }
  public Move Move(ArrayList<SurfingWave> waves) {
    double factor = 0.48;
    double distance = GamePhysics.Distance(me_, enemy_);
    if ( distance < MIN_DISTANCE ) {
      factor = 0.6;
    } else if ( distance > MAX_DISTANCE ) {
      factor = 0.35;
    }
    //Me.printf("distancing: %.3f, %.2f (%d)\n", distance, factor, direction_);
    return Move(factor);
  }
}
