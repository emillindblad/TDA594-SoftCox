package zyx.megabot.movement;

import java.util.ArrayList;

import zyx.megabot.battlefield.BattleField;
import zyx.megabot.debug.Painter;
import zyx.megabot.geometry.Circle;
import zyx.megabot.geometry.Point;
import zyx.megabot.utils.GamePhysics;

public class WallSmoothing {
  private static final double WALL_TEST = BattleField.WALL + 1e-4;
  private static final double WALL_STICK = 140;
  private static final double RR = GamePhysics.Square(WALL_STICK);

  public static double Smooth(Point point, double angle, int direction) {
    Point end_point = new Point(point, angle, WALL_STICK);
    Painter.Draw(BattleField.RECTANGLE, 255, 255, 255);
    Painter.Draw(new Circle(point, WALL_STICK), 0, 0, 0);
    for ( int i = 1; i < 7; ++i ) {
      Painter.Draw(new Circle(end_point, i), 255, 0, 255);
    }
    if ( BattleField.RECTANGLE.contains(end_point.Point2D()) ) {
      return angle;
    }
    ArrayList<Point> points = new ArrayList<Point>();
    /* Left Wall */ {
      double x = WALL_TEST;
      double y[] = GetY(point.x_, point.y_, x);
      if ( y != null ) {
        Point np = new Point(x, y[0]);
        if ( BattleField.RECTANGLE.contains(np.Point2D()) ) {
          points.add(np);
        }
        Painter.Draw(new Circle(np, 3), 0, 0, 255);
        np = new Point(x, y[1]);
        if ( BattleField.RECTANGLE.contains(np.Point2D()) ) {
          points.add(np);
        }
        Painter.Draw(new Circle(np, 3), 255, 0, 0);
      }
    }
    /* Right Wall */ {
      double x = BattleField.WIDTH - WALL_TEST;
      double y[] = GetY(point.x_, point.y_, x);
      if ( y != null ) {
        Point np = new Point(x, y[0]);
        if ( BattleField.RECTANGLE.contains(np.Point2D()) ) {
          points.add(np);
        }
        Painter.Draw(new Circle(np, 3), 0, 0, 255);
        np = new Point(x, y[1]);
        if ( BattleField.RECTANGLE.contains(np.Point2D()) ) {
          points.add(np);
        }
        Painter.Draw(new Circle(np, 3), 255, 0, 0);
      }
    }
    //Me.printf("x smooth points: %d\n", points.size());

    /* Bottom Wall */ {
      double y = WALL_TEST;
      double x[] = GetY(point.y_, point.x_, y);
      if ( x != null ) {
        Point np = new Point(x[0], y);
        if ( BattleField.RECTANGLE.contains(np.Point2D()) ) {
          points.add(np);
          Painter.Draw(new Circle(np, 3), 0, 255, 0);
        }
        np = new Point(x[1], y);
        if ( BattleField.RECTANGLE.contains(np.Point2D()) ) {
          points.add(np);
          Painter.Draw(new Circle(np, 3), 255, 255, 255);
        }
      }
    }
    /* Top Wall */ {
      double y = BattleField.HEIGHT - WALL_TEST;
      double x[] = GetY(point.y_, point.x_, y);
      if ( x != null ) {
        Point np = new Point(x[0], y);
        if ( BattleField.RECTANGLE.contains(np.Point2D()) ) {
          points.add(np);
          Painter.Draw(new Circle(np, 3), 0, 255, 0);
        }
        np = new Point(x[1], y);
        if ( BattleField.RECTANGLE.contains(np.Point2D()) ) {
          points.add(np);
          Painter.Draw(new Circle(np, 3), 255, 255, 255);
        }
      }
    }
    double smooth_angle = 0;
    double smooth_difference = Double.POSITIVE_INFINITY;
    //Me.printf("smooth points: %d\n", points.size());
    for ( int i = 0; i < points.size(); ++i ) {
      Point p = points.get(i);
      double a = GamePhysics.Angle(point, p);
      Point pp = new Point(point, a, WALL_STICK);
      double difference = GamePhysics.Distance(end_point, pp);
      //Me.printf("point(%d): %.2f\n", i, difference);
      if ( difference < smooth_difference ) {
        smooth_difference = difference;
        smooth_angle = a;
      }
    }
    return smooth_angle;
  }

  private static double[] GetY(double x0, double y0, double x) {
    double determinant = RR - GamePhysics.Square(x - x0);
    if ( determinant < 0 ) return null;
    double factor = Math.sqrt(determinant);
    return new double[]{ y0 - factor, y0 + factor };
  }
}
