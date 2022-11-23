package zyx.mega.movement.vcs_true_ws;

import zyx.mega.movement.AbstractVCS_TrueWaveSurfing;
import zyx.megabot.bot.Enemy;

public class VCS_TrueWaveSurfing_Raw extends AbstractVCS_TrueWaveSurfing {
  public VCS_TrueWaveSurfing_Raw(Enemy enemy) {
    super(enemy);
  }
  protected void SetUp() {
  }
  public String Name() {
    return "VCS TrueWS (Raw)";
  }
}
