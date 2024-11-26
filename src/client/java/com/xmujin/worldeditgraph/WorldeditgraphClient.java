package com.xmujin.worldeditgraph;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.registry.Registries;


public class WorldeditgraphClient implements ClientModInitializer {
	public float totalTickDelta = 0;

	public static final Logger LOGGER = LoggerFactory.getLogger("MyModName");

	public static final MinecraftClient client = MinecraftClient.getInstance();

	@Override
	public void onInitializeClient() {


		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {

			client.player.sendMessage(Text.of("服了你了!a"), false);

			return ActionResult.PASS;
		});






		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {

			if (world.isClient) {
				client.player.sendMessage(Text.of("泥好!"), false);
				var itemStack = player.getStackInHand(hand);
				Item item = itemStack.getItem();
				item.getName();

				if (!itemStack.isEmpty()) {
					// 获取物品的唯一标识符
					Identifier itemId = Registries.ITEM.getId(item);
					player.sendMessage(Text.of("当前物品 ID: " + itemId.toString()), false);

					//player.sendMessage(Text.of("当前物品 ID: " + item.getTranslationKey()), false);
				} else {
					player.sendMessage(Text.of("当前没有物品"), false);
				}
			}
			return ActionResult.PASS;
		});







		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		// 注册渲染事件
//		HudRenderCallback.EVENT.register((drawContext, tickDeltaManager) -> {
//			// Get the transformation matrix from the matrix stack, alongside the tessellator instance and a new buffer builder.
//			//Matrix4f transformationMatrix = drawContext.getMatrices().peek().getPositionMatrix();
//
//			Tessellator tessellator = Tessellator.getInstance();
//
//			// Begin a triangle strip buffer using the POSITION_COLOR vertex format.
//			BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
//
//
//
//
//			MatrixStack matrices = drawContext.getMatrices();
//
//// Store the total tick delta in a field, so we can use it later.
//			totalTickDelta += tickDeltaManager.getTickDelta(true);
//
//// Push a new matrix onto the stack.
//			matrices.push();
//// Scale the matrix by 0.5 to make the triangle smaller and larger over time.
//			float scaleAmount = MathHelper.sin(totalTickDelta / 10F) / 2F + 1.5F;
//
//// Apply the scaling amount to the matrix.
//// We don't need to scale the Z axis since it's on the HUD and 2D.
//			matrices.scale(scaleAmount, scaleAmount, 1F);
//
//			Matrix4f transformationMatrix = drawContext.getMatrices().peek().getPositionMatrix();
//
//// ... write to the buffer.
//			// Write our vertices, Z doesn't really matter since it's on the HUD.
//			buffer.vertex(transformationMatrix, 20, 20, 5).color(0xFFFF0000);
//			buffer.vertex(transformationMatrix, 5, 40, 5).color(0xFF000000);
//			buffer.vertex(transformationMatrix, 35, 40, 5).color(0xFFa0522d);
//			buffer.vertex(transformationMatrix, 20, 60, 5).color(0xFFFF0000);
//
//			// We'll get to this bit in the next section.
//			RenderSystem.setShader(GameRenderer::getPositionColorProgram);
//			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//
//			// Draw the buffer onto the screen.
//			BufferRenderer.drawWithGlobalProgram(buffer.end());
//
//// Pop our matrix from the stack.
//			matrices.pop();
//
//
//		});


		WorldRenderEvents.BEFORE_DEBUG_RENDER.register(context -> {
			MinecraftClient mc = MinecraftClient.getInstance();
			Camera camera = mc.gameRenderer.getCamera();
			BlockPos blockPos = mc.player.getBlockPos();

			var consumer = context.consumers();

			var stack = context.matrixStack();

			stack.push();
			stack.translate(-context.camera().getPos().x, -context.camera().getPos().y, -context.camera().getPos().z);

			var layer = consumer.getBuffer(RenderLayer.LINES);


			double minX = blockPos.getX() + 1;
			double minY = blockPos.getY();
			double minZ = blockPos.getZ();

			//LOGGER.info(String.format("x: %d, y: %d, z, %d", blockPos.getX(),blockPos.getY(), blockPos.getZ() ));
			double maxX = minX + 1;
			double maxY = minY + 1;
			double maxZ = minZ + 1;

			// 减去相机位置以转换到局部坐标
			double cameraX = camera.getPos().x;
			double cameraY = camera.getPos().y;
			double cameraZ = camera.getPos().z;

			// 减去相机坐标 (使渲染相对于相机位置进行)
//			minX -= cameraX;
//			minY -= cameraY;
//			minZ -= cameraZ;
//			maxX -= cameraX;
//			maxY -= cameraY;
//			maxZ -= cameraZ;


			WorldRenderer.drawBox(stack, layer, minX, minY, minZ, maxX, maxY, maxZ, 0.3f, 1.0f, 0.0f, 1f);

			stack.pop();

		});



	}



}