package zyx.mega.targeting;

import zyx.mega.wave.GuessFactor;
import zyx.megabot.bot.Enemy;
import zyx.megabot.segmentation.ArraySegmentation;
import zyx.megabot.segmentation.SegmentedArray;
import zyx.megabot.targeting.Gun;
import zyx.megabot.utils.GamePhysics;
import zyx.megabot.utils.Range;
import zyx.megabot.utils.RollingAverage;
import zyx.megabot.wave.ShootingWave;

public abstract class AbstractVCS_GFgun extends Gun {
  protected int BINS;
  protected ArraySegmentation bins_;

  protected abstract void SetUp();
  public AbstractVCS_GFgun(Enemy enemy) {
    super(enemy);
    BINS = 63;
    bins_ = new ArraySegmentation();
    bins_.rolling_depth_ = 17;
    SetUp();
    bins_.SetUp(BINS);
  }
  protected double AimAngle(ShootingWave wave) {
    SegmentedArray segment = bins_.Data(wave);
    //if ( segment.depth_ < 2 ) return Double.POSITIVE_INFINITY;
    int best = (int)Range.Map(Math.random(), 0, 1, 0, BINS - 1);
    for ( int i = 0; i < BINS; ++i ) {
      if ( segment.data_[i] > segment.data_[best] ) best = i;
    }
    double factor = GuessFactor.Factor(best, BINS);
    double mae = GuessFactor.MAE(wave);
    //IndexHolder holder = new IndexHolder(wave, bins_);
    //if ( me_.gun_heat_ < GamePhysics.COOLING_RATE ) {
    //  Me.printf("%s aiming at factor: %.2f\nholder: %s (%d, %.2f)\n", Name(), factor, holder.toString(), wave.acceleration_, wave.bullet_hit_time_);
    //}
    return enemy_.bearing_ + wave.direction_ * factor * mae;
  }
  public void Update(ShootingWave wave) {
    SegmentedArray segment = bins_.UpdateData(wave);
    double factor = GuessFactor.Factor(wave, enemy_);
    int index = GuessFactor.Index(factor, BINS);
    //IndexHolder holder = new IndexHolder(wave, bins_);
    //Me.printf("%s update factor: %.2f\nholder: %s\n", Name(), factor, holder.toString());
    for ( int i = 0; i < BINS; ++i ) {
      double value = BINS / (1 + GamePhysics.Square(i - index));
      segment.data_[i] = RollingAverage.Roll(segment.data_[i], value, segment.depth_, 1);
    }
  }
}
