package com.xmujin.worldeditgraph;

import com.xmujin.worldeditgraph.item.TutorialItems;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;

import net.minecraft.entity.damage.DamageSources;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class Worldeditgraph implements ModInitializer {
	public static final String MOD_ID = "worldeditgraph";

	public int i = 0;

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

//		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
//			BlockState state = world.getBlockState(pos);
//			DynamicRegistryManager dynamicRegistryManager = world.getRegistryManager();
//			/* 手动的旁观者检查是必要的，因为 AttackBlockCallbacks 会在旁观者检查之前应用 */
////			if (state.isToolRequired() && !player.isSpectator() &&
////					player.getMainHandStack().isEmpty()) {
////				player.damage(world.getDamageSources().fall(), 1.0F);
////			}
//			player.damage(world.getDamageSources().fall(), 1.0F);
//			LOGGER.info("服了你了 老六" + i);
//			i++;
//			return ActionResult.PASS;
//		});


		TutorialItems.initialize();
	}
}