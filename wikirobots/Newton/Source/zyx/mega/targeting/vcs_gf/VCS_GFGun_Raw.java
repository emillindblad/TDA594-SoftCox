package zyx.mega.targeting.vcs_gf;

import zyx.mega.targeting.AbstractVCS_GFgun;
import zyx.megabot.bot.Enemy;

public class VCS_GFGun_Raw extends AbstractVCS_GFgun {
  public VCS_GFGun_Raw(Enemy enemy) {
    super(enemy);
  }
  protected void SetUp() {
  }
  public String Name() {
    return "VCS_GFGun (Raw)";
  }
}
