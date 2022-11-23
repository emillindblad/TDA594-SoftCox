package zyx.megabot.debug;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import robocode.util.BoundingRectangle;
import zyx.megabot.geometry.Circle;

public class Painter {
  private static final int ALPHA = 255;
  private static Graphics2D g_;
  private static ArrayList<Painting> paintings_ = new ArrayList<Painting>();
  public static void SetGraphics(Graphics2D g) {
    g_ = g;
  }
  public static void Paint() {
    if ( g_ != null ) {
      for (Painting painting : paintings_) {
        painting.Paint(g_);
      }
    }
    paintings_.clear();
    g_ = null;
  }
  public static void Draw(BoundingRectangle rectangle, int r, int g, int b) {
    paintings_.add(new PaintingRectangle(new Color(r, g, b, ALPHA), false, rectangle));
  }
  public static void Draw(Line2D.Double line, int r, int g, int b) {
    paintings_.add(new PaintingLine(new Color(r, g, b, ALPHA), false, line));
  }
  public static void Draw(Circle circle, int r, int g, int b) {
    paintings_.add(new PaintingCircle(new Color(r, g, b, ALPHA), false, circle));
  }
}
