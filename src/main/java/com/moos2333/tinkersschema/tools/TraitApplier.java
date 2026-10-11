package com.moos2333.tinkersschema.tools;

import com.moos2333.tinkersschema.TinkersSchema;
import com.moos2333.tinkersschema.config.ToolConfig;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.library.utils.ToolBuilder;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class TraitApplier {

    private static final int DEFAULT_COLOR = 0xFFFFFF;

    private static final int[] COLOR_MAP = {
            0x000000, 0x0000AA, 0x00AA00, 0x00AAAA,
            0xAA0000, 0xAA00AA, 0xFFAA00, 0xAAAAAA,
            0x555555, 0x5555FF, 0x55FF55, 0x55FFFF,
            0xFF5555, 0xFF55FF, 0xFFFF55, 0xFFFFFF
    };

    private static boolean ready = false;

    private TraitApplier() {
    }

    public static void markReady() {
        ready = true;
    }

    public static void apply(ToolConfig config, NBTTagCompound root) {
        List<String> traits = config.getTraits();
        if (traits == null || traits.isEmpty()) return;
        Map<String, String> colors = config.getTraitColors();
        for (String name : traits) {
            ITrait trait = resolve(name);
            if (trait != null) {
                ToolBuilder.addTrait(root, trait, resolveColor(colors, name));
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

    private static int resolveColor(Map<String, String> colors, String traitName) {
        if (colors == null) return DEFAULT_COLOR;
        String name = colors.get(traitName);
        if (name == null) return DEFAULT_COLOR;
        try {
            TextFormatting formatting = TextFormatting.valueOf(name.toUpperCase(Locale.ROOT));
            int index = formatting.getColorIndex();
            return index >= 0 && index < COLOR_MAP.length ? COLOR_MAP[index] : DEFAULT_COLOR;
        } catch (IllegalArgumentException e) {
            TinkersSchema.LOGGER.warn("Unknown color '{}' for trait '{}'", name, traitName);
            return DEFAULT_COLOR;
        }
    }
}