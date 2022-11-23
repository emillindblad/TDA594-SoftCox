package zyx.megabot.segmentation;

import zyx.megabot.utils.RollingAverage;
import zyx.megabot.wave.SegmentedWave;


public class ValueSegmentation extends Segmentation {
  double data_[][][][][][][][];
  public void SetUp() {
    data_ = new double
    [lateral_velocity_.Size()]
    [distance_.Size()]
    [fwd_wall_hit_time_.Size()]
    [bck_wall_hit_time_.Size()]
    [time_direction_.Size()]
    [time_running_.Size()]
    [acceleration_.Size()]
    [bullet_hit_time_.Size()]
    ;
    super.SetUp();
  }
  public void UpdateData(SegmentedWave wave, double value, double weight) {
    IndexHolder holder = IndexHolder(wave);
    Increment(holder);
    data_
    [holder.lateral_velocity_]
    [holder.distance_]
    [holder.fwd_wall_hit_time_]
    [holder.bck_wall_hit_time_]
    [holder.time_direction_]
    [holder.time_running_]
    [holder.acceleration_]
    [holder.bullet_hit_time_]
     =
      RollingAverage.Roll(
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
        value,
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
          ),
        weight);
  }
  public double Value(SegmentedWave wave) {
    IndexHolder holder = IndexHolder(wave);
    return data_
    [holder.lateral_velocity_]
    [holder.distance_]
    [holder.fwd_wall_hit_time_]
    [holder.bck_wall_hit_time_]
    [holder.time_direction_]
    [holder.time_running_]
    [holder.acceleration_]
    [holder.bullet_hit_time_]
    ;
  }
}
