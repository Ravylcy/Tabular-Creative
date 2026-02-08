package com.slyrien.tabularcreative.api;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.Predicate;

public final class TabularAPI {
    private TabularAPI() {}

    private static final Map<ResourceKey<CreativeModeTab>, List<TabularSubCategory>> TAB_CATEGORIES = new HashMap<>();
    private static final Map<ResourceKey<CreativeModeTab>, Integer> TAB_STATES = new HashMap<>();

    public static void init() { }

    
    public static void registerSubCategories(ResourceKey<CreativeModeTab> tabKey, TabularSubCategory... categories) {
        registerSubCategories(tabKey, true, categories);
    }

    
    public static void registerSubCategories(ResourceKey<CreativeModeTab> tabKey, boolean includeAll, TabularSubCategory... categories) {
        List<TabularSubCategory> list = TAB_CATEGORIES.computeIfAbsent(tabKey, k -> new ArrayList<>());


        if (includeAll && list.isEmpty()) {
            list.add(new TabularSubCategory(Component.translatable("tabularcreative.all", "All"), s -> true));
        }

        Collections.addAll(list, categories);
    }

    public static List<TabularSubCategory> getCategories(CreativeModeTab tab) {
        ResourceKey<CreativeModeTab> key = BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(tab).orElse(null);
        if (key == null) return Collections.emptyList();
        return TAB_CATEGORIES.getOrDefault(key, Collections.emptyList());
    }

    public static int getCurrentIndex(CreativeModeTab tab) {
        ResourceKey<CreativeModeTab> key = BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(tab).orElse(null);
        if (key == null) return 0;
        return TAB_STATES.getOrDefault(key, 0);
    }

    public static void setCurrentIndex(CreativeModeTab tab, int index) {
        ResourceKey<CreativeModeTab> key = BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(tab).orElse(null);
        if (key != null) {
            TAB_STATES.put(key, index);
        }
    }

    public static Predicate<ItemStack> instanceOf(Class<?> clazz) {
        return stack -> clazz.isInstance(stack.getItem());
    }
}