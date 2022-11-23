package zyx.megabot.geometry;

import java.awt.geom.Point2D;


public class Point {
  public double x_;
  public double y_;
  public Point() {
  }
  public Point(double x, double y) {
    Set(x, y);
  }
  public Point(Point point) {
    Set(point);
  }
  public Point(Point point, double angle, double distance) {
    Project(point, angle, distance);
  }
  public void Set(Point point) {
    Set(point.x_, point.y_);
  }
  public void Set(double x, double y) {
    x_ = x;
    y_ = y;
  }
  public void Project(Point point, double angle, double distance) {
    x_ = point.x_ + Math.sin(angle) * distance;
    y_ = point.y_ + Math.cos(angle) * distance;
  }
  public void Move(double angle, double distance) {
    x_ += Math.sin(angle) * distance;
    y_ += Math.cos(angle) * distance;
  }
  public Point2D.Double Point2D() {
    return new Point2D.Double(x_, y_);
  }
}
