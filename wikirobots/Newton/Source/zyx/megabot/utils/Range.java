package zyx.megabot.utils;


public class Range {
  public static double CapLow(double value, double min) {
    return Math.max(min, value);
  }
  public static double CapHigh(double value, double max) {
    return Math.min(max, value);
  }
  public static double CapLowHigh(double value, double min, double max) {
    return CapLow(CapHigh(value, max), min);
  }
  public static double CapCentered(double value, double absolute_max) {
    return CapLowHigh(value, -absolute_max, absolute_max);
  }
  public static double Map(double value, double min0, double max0, double min1, double max1) {
    double normal = (value - min0) / (max0 - min0);
    return CapLowHigh(normal * (max1 - min1) + min1, min1, max1);
  }
  public static boolean Inside(double value, double min, double max) {
    return value > min && value < max;
  }
}
