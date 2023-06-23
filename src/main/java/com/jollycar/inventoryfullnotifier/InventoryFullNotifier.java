package com.jollycar.inventoryfullnotifier;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class InventoryFullNotifier implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("invfullnot");

	@Override
	public void onInitialize() {

		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			if (world.isClient && !player.isSpectator()) {
				// Find any slot that has no items
				boolean hasOnlyFilledSlots = player.getInventory().main.stream()
						.noneMatch(itemStack -> itemStack.getCount() == 0);

				// All slots have items in them
				if (hasOnlyFilledSlots) {
					// Get all items in slots that still have space
					Set<Item> itemsInFilledSlotsWithSpace = player.getInventory().main.stream()
							.filter(itemStack -> itemStack.getCount() < itemStack.getMaxCount())
							.map(ItemStack::getItem)
							.collect(Collectors.toSet());

					// Count the slots where ItemStack count is max and there is no slot with the same item and still has space
					long count = player.getInventory().main.stream()
							.filter(itemStack -> itemStack.getCount() == itemStack.getMaxCount())
							.filter(not(itemStack -> itemsInFilledSlotsWithSpace.contains(itemStack.getItem())))
							.count();

					if (count > 0) {
						player.playSound(SoundEvents.ENCHANT_THORNS_HIT, 1.0F, 1.0F);
					}
				}
			}

			return ActionResult.PASS;
		});
	}
}
