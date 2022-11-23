package zyx.megabot.utils;

public class RollingAverage {
  public double average_;
  public int count_;
  public int depth_;
  public RollingAverage(int depth) {
    average_ = 0;
    count_ = 0;
    depth_ = depth;
  }
  public RollingAverage(double value, int depth) {
    average_ = value;
    count_ = 1;
    depth_ = depth;
  }
  public void Roll(double value, double weigth) {
    average_ = Roll(average_, value, Math.min(++count_, depth_), weigth);
  }
  public static double Roll(double average, double value, int depth, double weigth) {
    return (average * depth + value * weigth) / (depth + weigth);
  }
}
