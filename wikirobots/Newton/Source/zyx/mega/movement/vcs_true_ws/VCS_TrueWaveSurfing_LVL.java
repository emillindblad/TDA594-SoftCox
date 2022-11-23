package zyx.mega.movement.vcs_true_ws;

import zyx.mega.movement.AbstractVCS_TrueWaveSurfing;
import zyx.megabot.bot.Enemy;
import zyx.megabot.segmentation.Segmentation;

public class VCS_TrueWaveSurfing_LVL extends AbstractVCS_TrueWaveSurfing {
  public VCS_TrueWaveSurfing_LVL(Enemy enemy) {
    super(enemy);
  }
  protected void SetUp() {
    bins_.UseKnownSlices(Segmentation.LATERAL_VELOCITY, Segmentation.HIGH_SEGMENTATION);
    bins_.UseKnownSlices(Segmentation.DISTANCE, Segmentation.LOW_SEGMENTATION);
    //bins_.UseKnownSlices(Segmentation.FWD_WALL_HIT_TIME, Segmentation.LOW_SEGMENTATION);
    //bins_.UseKnownSlices(Segmentation.TIME_DIRECTION, Segmentation.LOW_SEGMENTATION);
    //BINS = 33;
  }
  public String Name() {
    return "VCS TrueWS (Raw)";
  }
}
