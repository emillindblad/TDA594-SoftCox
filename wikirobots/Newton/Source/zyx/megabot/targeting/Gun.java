package zyx.megabot.targeting;

import robocode.util.Utils;
import zyx.megabot.bot.Enemy;
import zyx.megabot.equipment.EnemyBasedItem;
import zyx.megabot.segmentation.ValueSegmentation;
import zyx.megabot.wave.ShootingWave;

public abstract class Gun extends EnemyBasedItem {
  //private static final double START_RATING = 0.5;
  private static final int RATING_DEPTH = 47;
  public long cache_time_;
  public double cache_angle_;
  public ValueSegmentation rating_;

  public Gun(Enemy enemy) {
    super(enemy);
    //rating_ = new RollingAverage(START_RATING, RATING_DEPTH);
    rating_ = new ValueSegmentation();
    rating_.rolling_depth_ = RATING_DEPTH;
    //rating_.UseKnownSlices(Segmentation.HIGH_SEGMENTATION);
    //rating_.UseKnownSlices(Segmentation.DISTANCE, Segmentation.HIGH_SEGMENTATION);
    rating_.SetUp();
  }
  public double AimAngle(long time, ShootingWave wave) {
    if ( time == cache_time_ ) return cache_angle_;
    cache_time_ = time;
    return cache_angle_ = Utils.normalAbsoluteAngle(AimAngle(wave));
  }
  protected abstract double AimAngle(ShootingWave wave);
  public abstract void Update(ShootingWave wave);

  public void LogHit(ShootingWave wave) {
    LogScore(wave, 1, 1);
    //Me.printf("hit: %s: %.2f\n", Name(), rating_.average_);
  }
  public void LogMiss(ShootingWave wave) {
    LogScore(wave, 0, 1);
    //Me.printf("miss: %s: %.2f\n", Name(), rating_.average_);
  }
  private void LogScore(ShootingWave wave, double score, double weight) {
    rating_.UpdateData(wave, score, weight);
  }
  public double Rating(ShootingWave wave) {
    if ( wave == null ) return 0;
    return rating_.Value(wave);
  }
}
