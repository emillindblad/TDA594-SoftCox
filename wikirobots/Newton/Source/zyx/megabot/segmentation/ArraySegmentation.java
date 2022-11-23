package zyx.megabot.segmentation;

import zyx.megabot.wave.SegmentedWave;


public class ArraySegmentation extends Segmentation {
  private double data_[][][][][][][][][];
  public void SetUp(int size) {
    data_ = new double
    [lateral_velocity_.Size()]
    [distance_.Size()]
    [fwd_wall_hit_time_.Size()]
    [bck_wall_hit_time_.Size()]
    [time_direction_.Size()]
    [time_running_.Size()]
    [acceleration_.Size()]
    [bullet_hit_time_.Size()]
    [size];
    SetUp();
  }
  public SegmentedArray UpdateData(SegmentedWave wave) {
    return Data(wave, true);
  }
  public SegmentedArray Data(SegmentedWave wave) {
    return Data(wave, false);
  }
  public SegmentedArray Data(SegmentedWave wave, boolean increment) {
    IndexHolder holder = IndexHolder(wave);
    if ( increment ) Increment(holder);
    return new SegmentedArray(
      data_
        [holder.lateral_velocity_]
        [holder.distance_]
        [holder.fwd_wall_hit_time_]
        [holder.bck_wall_hit_time_]
        [holder.time_direction_]
        [holder.time_running_]
        [holder.acceleration_]
        [holder.bullet_hit_time_]
      ,
      Math.min(rolling_depth_,
      count_
        [holder.lateral_velocity_]
        [holder.distance_]
        [holder.fwd_wall_hit_time_]
        [holder.bck_wall_hit_time_]
        [holder.time_direction_]
        [holder.time_running_]
        [holder.acceleration_]
        [holder.bullet_hit_time_]
     ));
  }
}
