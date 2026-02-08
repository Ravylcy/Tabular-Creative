package com.slyrien.tabularcreative;

import com.slyrien.tabularcreative.api.TabularAPI;
import com.slyrien.tabularcreative.api.TabularSubCategory;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TabularDemo {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TabularcreativeMod.MODID);

    public static final RegistryObject<CreativeModeTab> DEMO_TAB = TABS.register("demo_tab", () -> CreativeModeTab.builder()
            .title(Component.literal("Tabular Demo"))
            .icon(() -> new ItemStack(Items.DIAMOND_SWORD))
            .displayItems((params, output) -> {

                output.accept(Items.DIAMOND_SWORD);
                output.accept(Items.IRON_AXE);
                output.accept(Items.BOW);
                output.accept(Items.ARROW);
                output.accept(Items.APPLE);
                output.accept(Items.COOKED_BEEF);
                output.accept(Items.BREAD);
                output.accept(Items.GOLDEN_CARROT);
                output.accept(Blocks.STONE);
                output.accept(Blocks.DIRT);
                output.accept(Blocks.OAK_LOG);
                output.accept(Blocks.COBBLESTONE);
            })
            .build());

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }

    public static void initCategories() {


        TabularAPI.registerSubCategories(DEMO_TAB.getKey(),

                TabularSubCategory.create("Combat", stack -> {
                    Item i = stack.getItem();
                    return i instanceof SwordItem || i instanceof AxeItem || i instanceof BowItem || i instanceof ArrowItem;
                }),

                TabularSubCategory.create("Food", stack ->
                        stack.getItem().isEdible()
                ),

                TabularSubCategory.create("Blocks", stack ->
                        stack.getItem() instanceof BlockItem
                )
        );
    }
}