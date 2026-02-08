package com.slyrien.tabularcreative;

import com.mojang.logging.LogUtils;
import com.slyrien.tabularcreative.api.TabularAPI;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(TabularcreativeMod.MODID)
public class TabularcreativeMod {
    public static final String MODID = "tabularcreative";
    private static final Logger LOGGER = LogUtils.getLogger();

    public TabularcreativeMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();


        modEventBus.addListener(this::commonSetup);


        TabularAPI.init();


        TabularDemo.register(modEventBus);


        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {


        event.enqueueWork(TabularDemo::initCategories);
    }
}