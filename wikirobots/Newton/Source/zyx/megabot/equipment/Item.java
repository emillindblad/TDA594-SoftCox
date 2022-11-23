package zyx.megabot.equipment;

import zyx.megabot.bot.Me;

public abstract class Item {
  protected static Me me_;
  public static void StaticInit() {
    me_ = Me.Instance();
  }
  
  public int id_;
  public abstract String Name();
}
