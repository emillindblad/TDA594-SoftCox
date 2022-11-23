package zyx.megabot.equipment;

import java.util.ArrayList;
import java.util.Arrays;

import zyx.megabot.bot.Enemy;
import zyx.megabot.movement.EnemyBasedMovement;
import zyx.megabot.movement.MoveManager;
import zyx.megabot.targeting.Arsenal;
import zyx.megabot.targeting.Gun;

public class Equipment {
  private static ArrayList<Item> items_;
  private static boolean active_[];
  private static int N = 0;

  public MoveManager move_manager_;
  public Arsenal arsenal_;
  
  public Equipment(Enemy enemy) {
    move_manager_ = new MoveManager(enemy);
    arsenal_ = new Arsenal(enemy);
    items_ = new ArrayList<Item>();
  }
  public void Init() {
    move_manager_.Init();
    arsenal_.Init();
  }
  public ItemId Add(Item item, ItemId id) {
    if (item instanceof Gun) {
      Gun gun = (Gun) item;
      arsenal_.Add(gun);
    } else if (item instanceof EnemyBasedMovement) {
      EnemyBasedMovement movement = (EnemyBasedMovement) item;
      move_manager_.Add(movement);
    } else {
      return null;
    }
    if ( id == null ) {
      id = new ItemId(N++);
      items_.add(item);
      active_ = null;
      //Me.printf("active is now null: %d\n", N);
    }
    item.id_ = id.id_;
    return id;
  }
  private void Update() {
    move_manager_.Update();
    arsenal_.Update();
  }
  public static boolean IsReady() {
    return active_ != null;
  }
  private static void Ready() {
    if ( !IsReady() ) {
      active_ = new boolean[N];
      ActivateAll();
      //Me.printf("active is NOT null: %s.%d\n", active_, active_.length);
    }
  }
  private static void SetAll(boolean value) {
    Ready();
    Arrays.fill(active_, value);
    /*
    for (boolean b : active_) {
      Me.printf(" %b", b);
    }
    Me.printf("\n");
    */
  }
  public static void ActivateAll() {
    SetAll(true);
  }
  public static void DeActivateAll() {
    SetAll(false);
  }
  private static void Set(int id, boolean value) {
    Ready();
    active_[id] = value;
  }
  public static void Activate(ItemId id) {
    if ( id == null ) return;
    Set(id.id_, true);
    //Me.printf("activating: %s\n", items_.get(id.id_).Name());
  }
  public static void DeActivate(ItemId id) {
    if ( id == null ) return;
    Set(id.id_, false);
  }
  public static boolean Active(Item item) {
    Ready();
    return active_[item.id_];
  }
}
