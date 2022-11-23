package zyx.megabot.segmentation;

public class SegmentedArray extends Segment {
  public double[] data_;

  public SegmentedArray(double[] data, int depth) {
    super(depth);
    data_ = data;
  }
}
