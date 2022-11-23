package zyx.megabot.wave;

import zyx.megabot.geometry.Circle;


public class Wave extends Circle {
  public long time_;
  public double bearing_;
  public double velocity_;
  public int direction_;

  private long cache_time_;
  public long update_time_;
  
  public static final int START_TTL = 2;
  public int ttl_;

  public Wave(long time) {
    cache_time_ = time_ = time;
    ttl_ = START_TTL;
  }
  public boolean Done() {
    return --ttl_ <= 0;
  }
  public void Update(long time) {
    update_time_ = (time - cache_time_);
    radius_ += update_time_ * velocity_;
    cache_time_ = time;
  }
}
