package zyx.mega;

import java.awt.Color;

import zyx.mega.movement.OrbitalDistancing;
import zyx.mega.movement.RamMovement;
import zyx.mega.movement.vcs_true_ws.VCS_TrueWaveSurfing_LVL;
import zyx.mega.movement.vcs_true_ws.VCS_TrueWaveSurfing_Main;
import zyx.mega.movement.vcs_true_ws.VCS_TrueWaveSurfing_Raw;
import zyx.mega.targeting.HeadOnGun;
import zyx.mega.targeting.vcs_gf.VCS_GFGun_LVH;
import zyx.mega.targeting.vcs_gf.VCS_GFGun_LVH_DH;
import zyx.mega.targeting.vcs_gf.VCS_GFGun_LVH_DH_FWHH_BWHH;
import zyx.mega.targeting.vcs_gf.VCS_GFGun_LVH_DH_FWHH_TDL;
import zyx.mega.targeting.vcs_gf.VCS_GFGun_Main;
import zyx.mega.targeting.vcs_gf.VCS_GFGun_Raw;
import zyx.megabot.MegaBot;
import zyx.megabot.battle.State;
import zyx.megabot.behaviour.AlwaysTrue;
import zyx.megabot.behaviour.Disabled1v1;
import zyx.megabot.bot.Enemy;
import zyx.megabot.equipment.Equipment;
import zyx.megabot.equipment.ItemId;
import zyx.megabot.utils.Range;

public class Newton extends MegaBot {
  /* Movement */
  private static ItemId ws_raw_;
  private static ItemId ws_lvl_;
  private static ItemId ws_main_;
  private static ItemId distancing_;
  private static ItemId ram_;

  /* Targeting */
  private static ItemId gf_raw_;
  private static ItemId gf_lvh_;
  private static ItemId gf_main_;
  private static ItemId gf_lvh_dh_;
  private static ItemId gf_lvh_dh_fwhh_;
  private static ItemId gf_lvh_dh_fwhh_tdh_;
  private static ItemId hot_;
  
  protected void Init() {
    Color body = new Color(0, 64, 64);
    Color gun = new Color(32, 32, 32);
    Color radar = new Color(64, 128 ,64);
    Color arc = new Color(255, 0, 0);
    Color bullet = new Color(0, 255, 255);
    setColors(body, gun, radar, bullet, arc);
    /**
    Add(new AlwaysTrue() {
      public void Activate() {
        Equipment.Activate(distancing_);
      }
    });
    /**/
    Add(new Disabled1v1() {
      public void Activate() {
        if ( target_ == null ) return;
        if ( target_.equipment_.move_manager_.waves_.size() > 0 ) {
          Equipment.Activate(ws_main_);
          Equipment.Activate(distancing_);
          if ( me_.energy_ < 20 ) {
            Equipment.Activate(hot_);
          }
        } else {
          Equipment.Activate(ram_);
        }
      }
      public double FirePower(Enemy enemy) {
        return 0.1;
      }
    });
    Add(new AlwaysTrue() {
      public void Activate() {
        if ( State.round_ < 2 ) {
          Equipment.Activate(ws_raw_);
        }
        if ( State.round_ < 5 ) {
          Equipment.Activate(ws_lvl_);
        }
        if ( State.round_ < 7 ) {
          Equipment.Activate(gf_raw_);
        }
        Equipment.Activate(ws_main_);
        Equipment.Activate(gf_lvh_);
        Equipment.Activate(distancing_);
        Equipment.Activate(gf_main_);
      }
      public double FirePower(Enemy enemy) {
        double acc = enemy.avg_accuracy_.average_;
        double power = super.FirePower(enemy);
        if ( acc < 0.05 ) power = Math.min(power, 0.5);
        if ( acc < 0.01 ) power = Math.min(power, 0.1);
        if ( me_.energy_ * 3 < enemy.energy_ && me_.energy_ > 5 ) {
          power = 3;
        }
        if ( me_.energy_ > enemy.energy_ * 3 && me_.energy_ > 5 ) {
          power = 3;
        }
        while ( power > 0.1 &&
            Range.Inside(me_.energy_, enemy.energy_, enemy.energy_ + power) ) {
          power *= 0.5;
        }
        return power < 0.1 ? 0 : power;
      }
    });
  }
  public void FirstLook(Enemy enemy) {
    /* Movement */
    ws_raw_ = enemy.Add(new VCS_TrueWaveSurfing_Raw(enemy), ws_raw_);
    ws_lvl_ = enemy.Add(new VCS_TrueWaveSurfing_LVL(enemy), ws_lvl_);
    ws_main_ = enemy.Add(new VCS_TrueWaveSurfing_Main(enemy), ws_main_);
    distancing_ = enemy.Add(new OrbitalDistancing(enemy), distancing_);
    ram_ = enemy.Add(new RamMovement(enemy), ram_);

    /* Targeting */
    gf_raw_ = enemy.Add(new VCS_GFGun_Raw(enemy), gf_raw_);
    gf_lvh_ = enemy.Add(new VCS_GFGun_LVH(enemy), gf_lvh_);
    gf_main_ = enemy.Add(new VCS_GFGun_Main(enemy), gf_main_);
    gf_lvh_dh_ = enemy.Add(new VCS_GFGun_LVH_DH(enemy), gf_lvh_dh_);
    gf_lvh_dh_fwhh_ = enemy.Add(new VCS_GFGun_LVH_DH_FWHH_BWHH(enemy), gf_lvh_dh_fwhh_);
    gf_lvh_dh_fwhh_tdh_ = enemy.Add(new VCS_GFGun_LVH_DH_FWHH_TDL(enemy), gf_lvh_dh_fwhh_tdh_);
    hot_ = enemy.Add(new HeadOnGun(enemy), hot_);
  }
}
