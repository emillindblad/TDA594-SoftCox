package zyx.mega.targeting.vcs_gf;

import zyx.mega.targeting.AbstractVCS_GFgun;
import zyx.megabot.bot.Enemy;
import zyx.megabot.segmentation.Segmentation;

public class VCS_GFGun_LVH_DH extends AbstractVCS_GFgun {
  public VCS_GFGun_LVH_DH(Enemy enemy) {
    super(enemy);
  }
  protected void SetUp() {
    bins_.UseKnownSlices(Segmentation.LATERAL_VELOCITY, Segmentation.HIGH_SEGMENTATION);
    bins_.UseKnownSlices(Segmentation.DISTANCE, Segmentation.HIGH_SEGMENTATION);
  }
  public String Name() {
    return "VCS_GFGun (LVH DH)";
  }
}
