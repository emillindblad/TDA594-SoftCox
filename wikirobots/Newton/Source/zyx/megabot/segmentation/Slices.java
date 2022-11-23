package zyx.megabot.segmentation;

public class Slices {
  public double slices_[];
  public Slices() {
    slices_ = new double[]{};
  }
  public Slices(double slices[]) {
    Set(slices);
  }
  public void Set(double slices[]) {
    slices_ = slices;
  }
  int Size() {
    return slices_.length + 1;
  }
  int Index(double value) {
    for ( int i = 0; i < slices_.length; ++i ) {
      if ( value < slices_[i] ) return i;
    }
    return slices_.length;
  }
}
