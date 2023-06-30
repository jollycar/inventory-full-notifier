package com.jollycar.inventoryfullnotifier.config;

import com.jollycar.inventoryfullnotifier.InventoryFullNotifier;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;

@Config(id = InventoryFullNotifier.MOD_ID)
public class ModConfiguration {

    @Configurable
    public boolean modEnabled = true;
}
