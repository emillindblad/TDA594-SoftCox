package zyx.megabot.bot;

public class EnemyInfo extends Bot {
  public long scan_time_;
  public double bearing_;
  public double lateral_velocity_;
  public int acceleration_;
  public int direction_;
  
  public int time_direction_;
  public int time_running_;

  public double my_lateral_velocity_;
  public int my_direction_;

  public double distance_;

  public EnemyInfo() {
  }
  public EnemyInfo(EnemyInfo enemy) {
    super(enemy);
    scan_time_ = enemy.scan_time_;
    bearing_ = enemy.bearing_;
    lateral_velocity_ = enemy.lateral_velocity_;
    acceleration_ = enemy.acceleration_;
    direction_ = enemy.direction_;

    time_direction_ = enemy.time_direction_;
    time_running_ = enemy.time_running_;

    my_lateral_velocity_ = enemy.my_lateral_velocity_;
    my_direction_ = enemy.my_direction_;

    distance_ = enemy.distance_;
  }
}
