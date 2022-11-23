package zyx.mega.targeting.vcs_gf;

import zyx.mega.targeting.AbstractVCS_GFgun;
import zyx.megabot.bot.Enemy;
import zyx.megabot.segmentation.Segmentation;

public class VCS_GFGun_LVH_DH_FWHH_TDL extends AbstractVCS_GFgun {
  public VCS_GFGun_LVH_DH_FWHH_TDL(Enemy enemy) {
    super(enemy);
  }
  protected void SetUp() {
    bins_.UseKnownSlices(Segmentation.LATERAL_VELOCITY, Segmentation.HIGH_SEGMENTATION);
    bins_.UseKnownSlices(Segmentation.DISTANCE, Segmentation.HIGH_SEGMENTATION);
    bins_.UseKnownSlices(Segmentation.FWD_WALL_HIT_TIME, Segmentation.HIGH_SEGMENTATION);
    bins_.UseKnownSlices(Segmentation.TIME_DIRECTION, Segmentation.LOW_SEGMENTATION);
  }
  public String Name() {
    return "VCS_GFGun (LVH DH FWHH BWHH)";
  }
}
