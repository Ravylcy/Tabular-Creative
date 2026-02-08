package com.slyrien.tabularcreative.client;

import com.slyrien.tabularcreative.api.TabularAPI;
import com.slyrien.tabularcreative.api.TabularSubCategory;
import com.slyrien.tabularcreative.mixin.CreativeModeInventoryScreenAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TabularScreenHandler {
    private final CreativeModeInventoryScreen screen;
    private Button prevBtn;
    private Button nextBtn;

    public TabularScreenHandler(CreativeModeInventoryScreen screen) {
        this.screen = screen;
    }

    public void init(int guiLeft, int guiTop, int xSize, int ySize) {

        int btnSize = 12;



        int xBase = guiLeft + xSize - 4;
        int yBase = guiTop + 4;


        nextBtn = new TransparentButton(xBase - btnSize, yBase, btnSize, btnSize, Component.literal(">"),
                () -> cycleCategory(1));


        prevBtn = new TransparentButton(xBase - btnSize * 2 - 2, yBase, btnSize, btnSize, Component.literal("<"),
                () -> cycleCategory(-1));


        prevBtn.visible = false;
        nextBtn.visible = false;
        updateButtonState(CreativeModeInventoryScreenAccessor.getSelectedTab());
    }

    public List<Button> getButtons() {
        return List.of(prevBtn, nextBtn);
    }

    public void updateVisibility(CreativeModeTab tab) {
        boolean hasCats = !TabularAPI.getCategories(tab).isEmpty();
        if (prevBtn != null) prevBtn.visible = hasCats;
        if (nextBtn != null) nextBtn.visible = hasCats;

        if (hasCats) {
            updateButtonState(tab);
        }
    }

    private void updateButtonState(CreativeModeTab tab) {
        if (prevBtn == null || nextBtn == null) return;
        List<TabularSubCategory> cats = TabularAPI.getCategories(tab);
        if (cats.isEmpty()) return;

        int current = TabularAPI.getCurrentIndex(tab);


        int next = (current + 1) % cats.size();
        int prev = (current - 1 + cats.size()) % cats.size();

        nextBtn.setTooltip(Tooltip.create(Component.translatable("tabularcreative.next", cats.get(next).displayName())));
        prevBtn.setTooltip(Tooltip.create(Component.translatable("tabularcreative.prev", cats.get(prev).displayName())));
    }

    private void cycleCategory(int direction) {
        CreativeModeTab tab = CreativeModeInventoryScreenAccessor.getSelectedTab();
        List<TabularSubCategory> cats = TabularAPI.getCategories(tab);
        if (cats.isEmpty()) return;

        int currentIndex = TabularAPI.getCurrentIndex(tab);
        currentIndex += direction;

        if (currentIndex >= cats.size()) currentIndex = 0;
        if (currentIndex < 0) currentIndex = cats.size() - 1;

        TabularAPI.setCurrentIndex(tab, currentIndex);
        updateButtonState(tab);

        ((CreativeModeInventoryScreenAccessor)screen).invokeRefreshCurrentTabContents(tab.getDisplayItems());
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, int guiLeft, int guiTop) {
        if (prevBtn != null && prevBtn.visible) {
            CreativeModeTab tab = CreativeModeInventoryScreenAccessor.getSelectedTab();
            List<TabularSubCategory> cats = TabularAPI.getCategories(tab);
            int idx = TabularAPI.getCurrentIndex(tab);

            if (!cats.isEmpty() && idx < cats.size()) {
                TabularSubCategory cat = cats.get(idx);
                Component name = cat.displayName();

                int textWidth = Minecraft.getInstance().font.width(name);


                int x = prevBtn.getX() - textWidth - 4;
                int y = prevBtn.getY() + (prevBtn.getHeight() - 8) / 2 + 1;


                graphics.drawString(Minecraft.getInstance().font, name, x, y, 0x404040, false);
            }
        }
    }

    public Collection<ItemStack> filterItems(CreativeModeTab tab, Collection<ItemStack> originals) {
        List<TabularSubCategory> cats = TabularAPI.getCategories(tab);
        if (cats.isEmpty()) return originals;

        int idx = TabularAPI.getCurrentIndex(tab);
        if (idx >= cats.size()) {
            idx = 0;
            TabularAPI.setCurrentIndex(tab, 0);
        }

        TabularSubCategory activeCat = cats.get(idx);
        return originals.stream().filter(activeCat.filter()).collect(Collectors.toList());
    }

    
    private static class TransparentButton extends Button {
        public TransparentButton(int x, int y, int width, int height, Component message, Runnable onPress) {
            super(x, y, width, height, message, b -> onPress.run(), DEFAULT_NARRATION);
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {


            int color = this.isHoveredOrFocused() ? 0xFFFFA0 : 0x404040;


            guiGraphics.drawCenteredString(Minecraft.getInstance().font, this.getMessage(),
                    this.getX() + this.width / 2,
                    this.getY() + (this.height - 8) / 2,
                    color);
        }
    }
}