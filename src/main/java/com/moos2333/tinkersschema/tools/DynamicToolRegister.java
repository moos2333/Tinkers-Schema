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
import slimeknights.tconstruct.library.tools.ranged.IAmmo;
import slimeknights.tconstruct.tools.ranged.TinkerRangedWeapons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public final class DynamicToolRegister {

    private static final List<ToolCore> DYNAMIC_TOOLS = new ArrayList<>();

    private static List<Item> defaultAmmo;

    private DynamicToolRegister() {
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        List<ToolConfig> configs = ToolConfigLoader.getConfigs();
        if (configs.isEmpty()) return;
        for (ToolConfig config : configs) {
            try {
                if (event.getRegistry().containsKey(config.getResourceLocation())) {
                    TinkersSchema.LOGGER.warn("Skipped tool '{}': item id is already registered", config.getId());
                    continue;
                }
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

    public static List<Item> getDefaultAmmo() {
        List<Item> ammo = defaultAmmo;
        if (ammo == null) {
            List<Item> items = new ArrayList<>(TinkerRangedWeapons.getDiscoveredArrows());
            for (ToolCore tool : DYNAMIC_TOOLS) {
                if (tool instanceof IAmmo) {
                    items.add(tool);
                }
            }
            ammo = Collections.unmodifiableList(items);
            defaultAmmo = ammo;
        }
        return ammo;
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