package com.slyrien.tabularcreative.api;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;


public record TabularSubCategory(Component displayName, Predicate<ItemStack> filter) {

    public static TabularSubCategory create(String name, Predicate<ItemStack> filter) {
        return new TabularSubCategory(Component.literal(name), filter);
    }

    public boolean test(ItemStack stack) {
        return filter.test(stack);
    }
}