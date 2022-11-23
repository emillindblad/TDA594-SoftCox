package zyx.megabot.debug;

import java.awt.Color;
import java.awt.Graphics2D;

import robocode.util.BoundingRectangle;

public class PaintingRectangle extends Painting {
  protected int x_;
  protected int y_;
  protected int w_;
  protected int h_;
  public PaintingRectangle(Color color, boolean fill, BoundingRectangle rectangle) {
    super(color, fill);
    x_ = (int) rectangle.x;
    y_ = (int) rectangle.y;
    w_ = (int) rectangle.width;
    h_ = (int) rectangle.height;
  }

  public void Paint(Graphics2D g) {
    super.Paint(g);
    if ( fill_ ) {
      g.fillRect(x_, y_, w_, h_);
    } else {
      g.drawRect(x_, y_, w_, h_);
    }
  }
}
