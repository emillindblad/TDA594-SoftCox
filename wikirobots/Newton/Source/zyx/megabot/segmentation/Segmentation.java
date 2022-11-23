package zyx.megabot.segmentation;

import zyx.megabot.bot.Me;
import zyx.megabot.wave.SegmentedWave;


public abstract class Segmentation {
  
  private static final double known_slices_[][][] = new double[][][] {
    /* Lateral Velocity */
    { {.5, 1.25, 2.25, 6.75}, {2, 4, 6} },
    /* Distance */
    //{ {200, 300, 400, 500, 700}, {300, 500, 700} }
    { {250, 300, 350, 400, 600}, {250, 400, 600} },
    /* Forward Wall Hit Time */
    { {10, 20, 30, 50, 80}, {8, 17, 40} },
    /* Backward Wall Hit Time */
    { {10, 20, 30, 50, 80}, {15, 30, 50} },
    /* Time Direction */
    { {2, 5, 10, 20, 40, 60}, {5, 12, 24, 50} },
    /* Time Running */
    { {2, 5, 10, 20, 40, 60}, {5, 12, 24, 50} },
    /* Acceleration */
    { {-1e-1, 1e-1}, {-1e-1} },
    /* Bullet Hit Time */
    { {10, 20, 30, 40, 50, 60}, {20, 40, 60} }
  };
  public static final int HIGH_SEGMENTATION = 0;
  public static final int LOW_SEGMENTATION = 1;
  public static final int LATERAL_VELOCITY = 0;
  public static final int DISTANCE = 1;
  public static final int FWD_WALL_HIT_TIME = 2;
  public static final int BCK_WALL_HIT_TIME = 3;
  public static final int TIME_DIRECTION = 4;
  public static final int TIME_RUNNING = 5;
  public static final int ACCELERATION = 6;
  public static final int BULLET_HIT_TIME = 7;
  
  public Slices lateral_velocity_;
  public Slices distance_;
  public Slices fwd_wall_hit_time_;
  public Slices bck_wall_hit_time_;
  public Slices time_direction_;
  public Slices time_running_;
  public Slices acceleration_;
  public Slices bullet_hit_time_;
  public int rolling_depth_;
  protected int count_[][][][][][][][];
  public Segmentation() {
    lateral_velocity_ = new Slices();
    distance_ = new Slices();
    fwd_wall_hit_time_ = new Slices();
    bck_wall_hit_time_ = new Slices();
    time_direction_ = new Slices();
    time_running_ = new Slices();
    acceleration_ = new Slices();
    bullet_hit_time_ = new Slices();
  }
  public void SetUp() {
    count_ = new int
    [lateral_velocity_.Size()]
    [distance_.Size()]
    [fwd_wall_hit_time_.Size()]
    [bck_wall_hit_time_.Size()]
    [time_direction_.Size()]
    [time_running_.Size()]
    [acceleration_.Size()]
    [bullet_hit_time_.Size()]
    ;
  }
  public void UseKnownSlices(int attribute, int level) {
    double[] s = known_slices_[attribute][level];
    switch ( attribute ) {
    case LATERAL_VELOCITY: lateral_velocity_.Set(s); break;
    case DISTANCE: distance_.Set(s); break;
    case FWD_WALL_HIT_TIME: fwd_wall_hit_time_.Set(s); break;
    case BCK_WALL_HIT_TIME: bck_wall_hit_time_.Set(s); break;
    case TIME_DIRECTION: time_direction_.Set(s); break;
    case TIME_RUNNING: time_running_.Set(s); break;
    case ACCELERATION: acceleration_.Set(s); break;
    case BULLET_HIT_TIME: bullet_hit_time_.Set(s); break;
    default:
      try {
        throw new Exception();
      } catch (Exception e) {
        e.printStackTrace(Me.Robot().out);
      }
    }
  }
  public void UseKnownSlices(int level) {
    for ( int i = 0; i < known_slices_.length; ++i ) {
      UseKnownSlices(i, level);
    }
  }
  protected IndexHolder IndexHolder(SegmentedWave wave) {
    return new IndexHolder(wave, this);
  }
  protected void Increment(IndexHolder holder) {
    count_
    [holder.lateral_velocity_]
    [holder.distance_]
    [holder.fwd_wall_hit_time_]
    [holder.bck_wall_hit_time_]
    [holder.time_direction_]
    [holder.time_running_]
    [holder.acceleration_]
    [holder.bullet_hit_time_]
    ++;
  }
}
