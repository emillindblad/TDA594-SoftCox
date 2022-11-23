package zyx.megabot.debug;

import java.awt.Color;
import java.awt.Graphics2D;

import zyx.megabot.geometry.Circle;

public class PaintingCircle extends Painting {
  protected int x_;
  protected int y_;
  protected int w_;
  protected int h_;
  public PaintingCircle(Color color, boolean fill, Circle circle) {
    super(color, fill);
    double side = 2 * circle.radius_;
    x_ = (int)(circle.x_ - side * 0.5);
    y_ = (int)(circle.y_ - side * 0.5);
    w_ = (int) side;
    h_ = (int) side;
  }
  public void Paint(Graphics2D g) {
    super.Paint(g);
    if ( fill_ ) {
      g.fillOval(x_, y_, w_, h_);
    } else {
      g.drawOval(x_, y_, w_, h_);
    }
  }
}
