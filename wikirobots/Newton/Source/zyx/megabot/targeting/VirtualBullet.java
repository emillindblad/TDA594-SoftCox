package zyx.megabot.targeting;

import java.awt.geom.Line2D;

import zyx.megabot.debug.Painter;
import zyx.megabot.geometry.Circle;
import zyx.megabot.geometry.Point;
import zyx.megabot.wave.ShootingWave;

public class VirtualBullet {

  public Gun gun_;
  public double bearing_;
  public Line2D.Double line_;
  public boolean flagged_;

  public VirtualBullet(Gun gun, double bearing) {
    gun_ = gun;
    bearing_ = bearing;
    line_ = new Line2D.Double();
  }
  public void SetFirePosition(Point position) {
    line_.setLine(position.Point2D(), position.Point2D());
  }
  public void Update(ShootingWave wave, boolean real_bullet) {
    if ( flagged_ ) return;
    double travelled_1 = (wave.update_time_ - 1) * wave.velocity_;
    double travelled = wave.update_time_ * wave.velocity_;
    line_.x1 = line_.x2 + Math.sin(bearing_) * travelled_1;
    line_.y1 = line_.y2 + Math.cos(bearing_) * travelled_1;
    line_.x2 += Math.sin(bearing_) * travelled;
    line_.y2 += Math.cos(bearing_) * travelled;
    if ( real_bullet ) {
      Painter.Draw(line_, 255, 64, 64);
      Painter.Draw(new Circle(new Point(line_.x2, line_.y2), 5), 255, 64, 64);
    } else {
      Painter.Draw(line_, 128, 64, 192);
      Painter.Draw(new Circle(new Point(line_.x2, line_.y2), 3), 128, 64, 192);
    }
  }
}
