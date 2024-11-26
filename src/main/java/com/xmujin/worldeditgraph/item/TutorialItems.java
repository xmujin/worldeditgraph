package com.xmujin.worldeditgraph.item;

import net.minecraft.registry.Registries;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.item.Item;



public final class TutorialItems {
    private TutorialItems() {}

    // 新物品的实例
    public static final CustomItem CUSTOM_ITEM = register("custom_item", new CustomItem(new Item.Settings()));

    public static <T extends Item> T register(String path, T item) {
        // 对于 1.21 之前的版本，请将 ''Identifier.of'' 替换为 ''new Identifier''
        return Registry.register(Registries.ITEM, Identifier.of("worldeditgraph", path), item);
    }

    public static void initialize() {
    }
}