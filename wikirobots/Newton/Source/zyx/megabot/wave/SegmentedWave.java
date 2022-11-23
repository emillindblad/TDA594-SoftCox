package zyx.megabot.wave;


public class SegmentedWave extends Wave {
  public double lateral_velocity_;
  public double distance_;
  public int fwd_wall_hit_time_;
  public int bck_wall_hit_time_;
  public int time_direction_;
  public int time_running_;
  public int acceleration_;
  public double bullet_hit_time_;
  public SegmentedWave(long time) {
    super(time);
  }
  public void WrapUp() {
    bullet_hit_time_ = distance_ / velocity_;
  }
}
