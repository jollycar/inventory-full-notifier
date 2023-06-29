package com.jollycar.inventoryfullnotifier;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryFullNotifier implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("inventoryfullnotifier");

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}
