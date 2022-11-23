package zyx.megabot.segmentation;

import zyx.megabot.wave.SegmentedWave;

public class IndexHolder {
  public int lateral_velocity_;
  public int distance_;
  public int fwd_wall_hit_time_;
  public int bck_wall_hit_time_;
  public int time_direction_;
  public int time_running_;
  public int acceleration_;
  public int bullet_hit_time_;
  public IndexHolder(SegmentedWave wave, Segmentation segmentation) {
    lateral_velocity_ = segmentation.lateral_velocity_.Index(wave.lateral_velocity_);
    distance_ = segmentation.distance_.Index(wave.distance_);
    fwd_wall_hit_time_ = segmentation.fwd_wall_hit_time_.Index(wave.fwd_wall_hit_time_);
    bck_wall_hit_time_ = segmentation.bck_wall_hit_time_.Index(wave.bck_wall_hit_time_);
    time_direction_ = segmentation.time_direction_.Index(wave.time_direction_);
    time_running_ = segmentation.time_running_.Index(wave.time_running_);
    acceleration_ = segmentation.acceleration_.Index(wave.acceleration_);
    bullet_hit_time_ = segmentation.bullet_hit_time_.Index(wave.bullet_hit_time_);
  }
  public String toString() {
    return String.format("%d, %d, %d, %d, %d, %d, %d, %d",
    lateral_velocity_,
    distance_,
    fwd_wall_hit_time_,
    bck_wall_hit_time_,
    time_direction_,
    time_running_,
    acceleration_,
    bullet_hit_time_);
  }
}
