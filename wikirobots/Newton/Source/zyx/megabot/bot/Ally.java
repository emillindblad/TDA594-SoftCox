package zyx.megabot.bot;


public class Ally extends Bot {
  public double gun_heading_;
  public double radar_heading_;
  public Ally() {
  }
  public Ally(Ally ally) {
    super(ally);
    gun_heading_ = ally.gun_heading_;
    radar_heading_ = ally.radar_heading_;
  }
}
