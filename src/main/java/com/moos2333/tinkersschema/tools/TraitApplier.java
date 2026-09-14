package com.moos2333.tinkersschema.tools;

import com.moos2333.tinkersschema.TinkersSchema;
import com.moos2333.tinkersschema.config.ToolConfig;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.library.utils.ToolBuilder;

import java.util.List;

public final class TraitApplier {

    private static boolean ready = false;

    private TraitApplier() {
    }

    public static void markReady() {
        ready = true;
    }

    public static void apply(ToolConfig config, NBTTagCompound root) {
        List<String> traits = config.getTraits();
        if (traits == null || traits.isEmpty()) return;
        for (String name : traits) {
            ITrait trait = resolve(name);
            if (trait != null) {
                ToolBuilder.addTrait(root, trait, 0xFFFFFF);
            } else if (ready) {
                TinkersSchema.LOGGER.warn("Unknown trait '{}' in tool '{}'", name, config.getId());
            }
        }
    }

    private static ITrait resolve(String name) {
        ITrait trait = TinkerRegistry.getTrait(name);
        if (trait != null) return trait;
        IModifier modifier = TinkerRegistry.getModifier(name);
        return modifier instanceof ITrait ? (ITrait) modifier : null;
    }
}