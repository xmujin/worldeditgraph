package com.xmujin.worldeditgraph;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;


import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.registry.Registries;


public class WorldeditgraphClient implements ClientModInitializer {
	// 第一次选中的方块坐标
	public BlockPos firstSelectedPos;
	// 第二次选中的方块坐标
	public BlockPos secondSelectedPos;

	public boolean isFirstSelected = false;

	public boolean isSecondSelected = false;

	// 是否是使用的木斧
	public boolean isWoodenAxe = false;


	public float totalTickDelta = 0;


	public static final Logger LOGGER = LoggerFactory.getLogger("MyModName");

	public static final MinecraftClient client = MinecraftClient.getInstance();

	@Override
	public void onInitializeClient() {


		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			if (world.isClient) {
				var itemStack = player.getStackInHand(hand);
				if(!itemStack.isEmpty()) {
					Item item = itemStack.getItem(); // 获取项
					Identifier itemId = Registries.ITEM.getId(item);
					if (itemId.toString().equals("minecraft:wooden_axe")) {
						isFirstSelected = true;
						firstSelectedPos = pos;
						player.sendMessage(Text.of("选中了第一个点：" + firstSelectedPos.toString()), false);
					} else {
						isFirstSelected = false;
					}
				} else {
					isFirstSelected = false;
				}
			}
			return ActionResult.PASS;
		});

		UseItemCallback.EVENT.register((player, world, hand) -> {
			if (world.isClient) {
				var itemStack = player.getStackInHand(hand);
				if(!itemStack.isEmpty()) {
					Item item = itemStack.getItem(); // 获取项
					Identifier itemId = Registries.ITEM.getId(item);
					if (itemId.toString().equals("minecraft:wooden_axe")) {
						// 获取玩家视线中的目标
						HitResult hitResult = client.crosshairTarget;
						if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
							isSecondSelected = true;
							BlockHitResult blockHitResult = (BlockHitResult) hitResult;
							secondSelectedPos = blockHitResult.getBlockPos();
							player.sendMessage(Text.of("选中了第二个点：" + secondSelectedPos.toString()), false);
						}

					} else {
						isSecondSelected = false;
					}
				} else {
					isSecondSelected = false;
				}
			}
			return TypedActionResult.pass(ItemStack.EMPTY);
		});





		WorldRenderEvents.BEFORE_DEBUG_RENDER.register(context -> {
			//Todo 检测手里是否拿着的是木斧，如果不是，则返回
			PlayerEntity player = client.player;
			ItemStack itemStack = player.getMainHandStack();

			if(!itemStack.isEmpty()) {
				Item item = itemStack.getItem(); // 获取项
				Identifier itemId = Registries.ITEM.getId(item);
				if (itemId.toString().equals("minecraft:wooden_axe")) {
					isWoodenAxe = true;
				} else {
					isWoodenAxe = false;
				}

			} else {
				isWoodenAxe = false;
				return;
			}

			if(!isWoodenAxe || !(isFirstSelected && isSecondSelected))
			{
				return;
			}


			//Todo 初始化变量以供渲染
			Camera camera = client.gameRenderer.getCamera(); // 获取相机
			VertexConsumerProvider consumer = context.consumers();
			VertexConsumer layer = consumer.getBuffer(RenderLayer.LINES);

			//Todo 渲染初始化
			MatrixStack stack = context.matrixStack();
			stack.push();
			// 平移坐标
			stack.translate(-context.camera().getPos().x, -context.camera().getPos().y, -context.camera().getPos().z);


			// 获取最小的坐标和最大的坐标
			BlockPos min = firstSelectedPos, max = secondSelectedPos;

			if(firstSelectedPos.compareTo(secondSelectedPos) >= 0)
			{
				max = firstSelectedPos;
				min = secondSelectedPos;
			}
			else
			{
				max = secondSelectedPos;
				min = firstSelectedPos;
			}

			//Todo 当只选中第一个方块时，渲染一个红方块



			//Todo 当只选中第二个方块时，渲染一个蓝方块


			//渲染边框
			WorldRenderer.drawBox(stack, layer,
					min.getX(),
					min.getY(),
					min.getZ(),
					max.getX() + 1,
					max.getY() + 1,
					max.getZ() + 1,
					0.3f, 1.0f, 0.0f, 1f);

			//渲染结束的处理
			stack.pop();

		});



	}



}