package zyx.megabot.debug;

import java.awt.Color;
import java.awt.Graphics2D;

public class Painting {
  private Color color_;
  protected boolean fill_;
  public Painting(Color color, boolean fill) {
    color_ = color;
    fill_ = fill;
  }
  public void SetColor(Graphics2D g) {
    g.setColor(color_);
  }
  public void Paint(Graphics2D g) {
    SetColor(g);
  }
}
