package com.jollycar.inventoryfullnotifier;

import com.jollycar.inventoryfullnotifier.config.ModConfiguration;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.collection.DefaultedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class InventoryFullNotifier implements ClientModInitializer {

	public static final String MOD_ID = "invfullnot";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static ModConfiguration config;

	@Override
	public void onInitializeClient() {

		ConfigHolder<ModConfiguration> holder = AutoConfig.register(ModConfiguration.class, Toml4jConfigSerializer::new);
		setConfig(holder.getConfig());

		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			if (config.modEnabled && (world.isClient && !player.isSpectator())) {
				DefaultedList<ItemStack> inventory = player.getInventory().main;

				Set<Item> voidedInventoryItems = getVoidedInventoryItems(inventory, config.notifySingleStackableItems);

				int[] inventorySize = getInventorySize(inventory);
				int currentSize = inventorySize[0];
				int maxSize = inventorySize[1];
				int percentageFull = (int) Math.round((double) currentSize / maxSize * 100);

				if ((config.notifyNoMoreRoom && !voidedInventoryItems.isEmpty()) || percentageFull > config.fullPercentage) {
					LOGGER.trace("Inventory is {} percent full", percentageFull);
					player.playSound(SoundEvents.ENCHANT_THORNS_HIT, (float) config.volumePercentage / 100, 1.0F);
				}
			}

			return ActionResult.PASS;
		});

	}

	public static void setConfig(ModConfiguration config) {
		InventoryFullNotifier.config = config;
	}

	/**
	 * Determine how much items are currently in the inventory (integer 1)
	 * and also the maximum capacity of the current inventory (integer 2),
	 * based on the maximum stack size of each item in every inventory slot.
	 *
	 * @param inventory The player's current inventory as a {@link DefaultedList}
	 *
	 * @return a primitive array, containing exactly 2 integers.
	 * The first integer is the current number of items in the inventory.
	 * The second integer is the total number of possible items that could be stored in the inventory,
	 * based on the maximum stack size of each item in every inventory slot.
	 */
	private static int[] getInventorySize(DefaultedList<ItemStack> inventory) {
		return inventory.stream()
				.reduce(new int[2],
						(partialSum, itemStack) -> {
							partialSum[0] += itemStack.getCount();
							partialSum[1] += itemStack.getMaxCount();
							return partialSum;
						},
						(firstArray, secondArray) -> {
							firstArray[0] += secondArray[0];
							firstArray[1] += secondArray[1];
							return firstArray;
						}
				);
	}

	/**
	 * Determine if there is any item in the current inventory that is full.
	 * In other words: there is no inventory slot available to store any more items already in the inventory.
	 *
	 * @param inventory The player's current inventory as a {@link DefaultedList}
	 * @param notifySingleStackableItems true if player is also notified for single stack items already in the inventory
	 *
	 * @return A list of items, currently in the inventory, that have no more available slots in your inventory if a new item would be picked up.
	 */
	private static Set<Item> getVoidedInventoryItems(DefaultedList<ItemStack> inventory, boolean notifySingleStackableItems) {
		if (inventory.stream().anyMatch(itemStack -> itemStack.getCount() == 0)) {
			return Collections.emptySet();
		}

		List<ItemStack> nonEmptyItemSlots = inventory.stream()
				.filter(itemStack -> itemStack.getCount() > 0 && (notifySingleStackableItems || itemStack.getMaxCount() > 1))
				.toList();

		Set<Item> itemsInFilledSlotsWithSpace = nonEmptyItemSlots.stream()
				.filter(itemStack -> itemStack.getCount() < itemStack.getMaxCount())
				.map(ItemStack::getItem)
				.collect(Collectors.toSet());

		return nonEmptyItemSlots.stream()
				.filter(itemStack -> itemStack.getCount() == itemStack.getMaxCount())
				.map(ItemStack::getItem)
				.collect(collectSlotsWithNoRoom(itemsInFilledSlotsWithSpace));
	}

	private static Collector<Item, ?, Set<Item>> collectSlotsWithNoRoom(Set<Item> itemsInFilledSlotsWithSpace) {
		return Collectors.filtering(item -> !itemsInFilledSlotsWithSpace.contains(item), Collectors.toSet());
	}
}
