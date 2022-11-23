package zyx.megabot.debug;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class PaintingLine extends Painting {
  int x0_, y0_;
  int x1_, y1_;
  public PaintingLine(Color color, boolean fill, Line2D.Double line) {
    super(color, fill);
    x0_ = (int) line.x1;
    y0_ = (int) line.y1;
    x1_ = (int) line.x2;
    y1_ = (int) line.y2;
  }
  public void Paint(Graphics2D g) {
    super.Paint(g);
    g.drawLine(x0_, y0_, x1_, y1_);
  }
}
