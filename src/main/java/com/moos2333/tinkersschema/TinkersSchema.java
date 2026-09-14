package com.moos2333.tinkersschema;

import com.moos2333.tinkersschema.common.CommonProxy;
import com.moos2333.tinkersschema.config.ToolConfigLoader;
import com.moos2333.tinkersschema.tools.TraitApplier;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = Tags.MOD_ID,
        name = Tags.MOD_NAME,
        version = Tags.VERSION,
        dependencies = "required-after:tconstruct;required-after:mantle"
)
public class TinkersSchema {

    @Mod.Instance(Tags.MOD_ID)
    public static TinkersSchema instance;

    @SidedProxy(
            clientSide = "com.moos2333.tinkersschema.client.ClientProxy",
            serverSide = "com.moos2333.tinkersschema.common.CommonProxy"
    )
    public static CommonProxy proxy;

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_ID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ToolConfigLoader.load(event.getModConfigurationDirectory(), LOGGER);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.initToolGuis();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        TraitApplier.markReady();
    }
}