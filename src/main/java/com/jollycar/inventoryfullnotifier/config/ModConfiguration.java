package com.jollycar.inventoryfullnotifier.config;

import com.jollycar.inventoryfullnotifier.InventoryFullNotifier;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = InventoryFullNotifier.MOD_ID)
public class ModConfiguration implements ConfigData {
    @Comment("Mod enabled [default: true]")
    public boolean modEnabled = true;

    @Comment("Notify if there is no more room for single stack items, like a pickaxe [default: false]")
    public boolean notifySingleStackableItems = false;

    @Comment("Notify if there is no more room for any item already in your inventory [default: true]")
    public boolean notifyNoMoreRoom = true;

    @Comment("Notify if the inventory reaches a certain percentage of its maximum capacity [default: 100] (1 to 100)")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int fullPercentage = 100;

    @Comment("Set the volume of the notifier sound [default: 75] (1 to 100)")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int volumePercentage = 75;
}
