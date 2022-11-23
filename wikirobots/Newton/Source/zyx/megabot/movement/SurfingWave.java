package zyx.megabot.movement;

import java.util.ArrayList;

import zyx.megabot.battle.State;
import zyx.megabot.bot.Bot;
import zyx.megabot.utils.GamePhysics;
import zyx.megabot.wave.SegmentedWave;

public class SurfingWave extends SegmentedWave {
  private static final int MAX_PREDICTION_TICKS = 500;
  public ArrayList<EnemyBasedMovement> movements_;
  public boolean hitting_me_;
  public boolean flagged_;
  public SurfingWave(long time) {
    super(time);
    movements_ = new ArrayList<EnemyBasedMovement>();
  }

  public boolean PredictHitPosition(Bot bot, int direction) {
    for ( int i = 1; i < MAX_PREDICTION_TICKS; ++i ) {
      double angle = WallSmoothing.Smooth(
        bot,
        GamePhysics.Angle(this, bot) + direction * GamePhysics.HALF_PI,
        direction);
      bot.Move(angle, true, direction);
      double distance = GamePhysics.Distance(this, bot);
      double travelled = (State.time_ - time_ + i) * velocity_;
      if ( distance <= travelled ) {
        return true;
      }
    }
    return false;
  }
}
