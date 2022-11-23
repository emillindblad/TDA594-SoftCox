package zyx.megabot.wave;

import java.util.ArrayList;

import zyx.megabot.targeting.Gun;
import zyx.megabot.targeting.VirtualBullet;

public class ShootingWave extends SegmentedWave {
  public ArrayList<VirtualBullet> virtual_bullets_;
  public Gun gun_;

  public ShootingWave(long time) {
    super(time);
    virtual_bullets_ = new ArrayList<VirtualBullet>();
  }
  public void Add(VirtualBullet virtual_bullet) {
    virtual_bullets_.add(virtual_bullet);
  }
  public void Update(long time) {
    super.Update(time);
    for (VirtualBullet virtual_bullet : virtual_bullets_) {
      virtual_bullet.Update(this, virtual_bullet.gun_ == gun_);
    }
  }
}
