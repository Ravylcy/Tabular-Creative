package com.slyrien.tabularcreative.mixin;

import com.slyrien.tabularcreative.client.TabularScreenHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class MixinCreativeModeInventoryScreen extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu> {

    @Shadow private static CreativeModeTab selectedTab;
    @Unique private TabularScreenHandler tabular$handler;

    public MixinCreativeModeInventoryScreen(CreativeModeInventoryScreen.ItemPickerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        this.tabular$handler = new TabularScreenHandler((CreativeModeInventoryScreen)(Object)this);
        this.tabular$handler.init(this.leftPos, this.topPos, this.imageWidth, this.imageHeight);

        for (Button btn : tabular$handler.getButtons()) {
            this.addRenderableWidget(btn);
        }


        this.tabular$handler.updateVisibility(selectedTab);
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (this.tabular$handler != null) {
            this.tabular$handler.render(guiGraphics, mouseX, mouseY, this.leftPos, this.topPos);
        }
    }

    @Inject(method = "selectTab", at = @At("HEAD"))
    private void onSelectTab(CreativeModeTab tab, CallbackInfo ci) {

        if (this.tabular$handler != null) {
            this.tabular$handler.updateVisibility(tab);
        }
    }



    @Redirect(method = "selectTab", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTab;getDisplayItems()Ljava/util/Collection;"))
    private Collection<ItemStack> filterInSelectTab(CreativeModeTab instance) {
        Collection<ItemStack> originals = instance.getDisplayItems();
        if (this.tabular$handler != null) {
            return this.tabular$handler.filterItems(instance, originals);
        }
        return originals;
    }



    @ModifyVariable(method = "refreshCurrentTabContents", at = @At("HEAD"), argsOnly = true)
    private Collection<ItemStack> filterInRefresh(Collection<ItemStack> collection) {
        if (this.tabular$handler != null) {

            return this.tabular$handler.filterItems(selectedTab, collection);
        }
        return collection;
    }
}