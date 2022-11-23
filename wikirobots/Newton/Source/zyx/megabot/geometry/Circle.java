package zyx.megabot.geometry;

public class Circle extends Point {
  public double radius_;
  public Circle() {
  }
  public Circle(Point center, double radius) {
    super(center);
    radius_ = radius;
  }
  public Circle(Circle circle) {
    super(circle);
    radius_ = circle.radius_;
  }
}
