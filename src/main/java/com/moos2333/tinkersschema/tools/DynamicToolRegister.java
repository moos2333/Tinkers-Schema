package com.moos2333.tinkersschema.tools;

import com.moos2333.tinkersschema.Tags;
import com.moos2333.tinkersschema.TinkersSchema;
import com.moos2333.tinkersschema.config.ToolConfig;
import com.moos2333.tinkersschema.config.ToolConfigLoader;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.common.ModelRegisterUtil;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.tools.ToolCore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public final class DynamicToolRegister {

    private static final List<ToolCore> DYNAMIC_TOOLS = new ArrayList<>();

    private DynamicToolRegister() {
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        List<ToolConfig> configs = ToolConfigLoader.getConfigs();
        if (configs.isEmpty()) return;
        for (ToolConfig config : configs) {
            try {
                ToolCore tool = ToolTypeRegistry.create(config);
                event.getRegistry().register(tool);
                if ("forge".equalsIgnoreCase(config.getStation())) {
                    TinkerRegistry.registerToolForgeCrafting(tool);
                } else {
                    TinkerRegistry.registerToolCrafting(tool);
                }
                DYNAMIC_TOOLS.add(tool);
            } catch (Exception e) {
                TinkersSchema.LOGGER.error("Failed to register tool '{}'", config.getId(), e);
            }
        }
    }

    public static List<ToolCore> getDynamicTools() {
        return Collections.unmodifiableList(DYNAMIC_TOOLS);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        if (DYNAMIC_TOOLS.isEmpty()) return;
        for (ToolCore tool : DYNAMIC_TOOLS) {
            ModelRegisterUtil.registerToolModel(tool);
        }
    }
}