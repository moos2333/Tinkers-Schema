package com.moos2333.tinkersschema.config;

import com.moos2333.tinkersschema.TinkersSchema;
import net.minecraft.block.material.Material;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class MaterialResolver {

    private static final Map<String, Material> BY_NAME = new HashMap<>();

    static {
        BY_NAME.put("AIR", Material.AIR);
        BY_NAME.put("GRASS", Material.GRASS);
        BY_NAME.put("GROUND", Material.GROUND);
        BY_NAME.put("WOOD", Material.WOOD);
        BY_NAME.put("ROCK", Material.ROCK);
        BY_NAME.put("IRON", Material.IRON);
        BY_NAME.put("ANVIL", Material.ANVIL);
        BY_NAME.put("WATER", Material.WATER);
        BY_NAME.put("LAVA", Material.LAVA);
        BY_NAME.put("LEAVES", Material.LEAVES);
        BY_NAME.put("PLANTS", Material.PLANTS);
        BY_NAME.put("VINE", Material.VINE);
        BY_NAME.put("SPONGE", Material.SPONGE);
        BY_NAME.put("CLOTH", Material.CLOTH);
        BY_NAME.put("FIRE", Material.FIRE);
        BY_NAME.put("SAND", Material.SAND);
        BY_NAME.put("CIRCUITS", Material.CIRCUITS);
        BY_NAME.put("CARPET", Material.CARPET);
        BY_NAME.put("GLASS", Material.GLASS);
        BY_NAME.put("REDSTONE_LIGHT", Material.REDSTONE_LIGHT);
        BY_NAME.put("TNT", Material.TNT);
        BY_NAME.put("CORAL", Material.CORAL);
        BY_NAME.put("ICE", Material.ICE);
        BY_NAME.put("PACKED_ICE", Material.PACKED_ICE);
        BY_NAME.put("SNOW", Material.SNOW);
        BY_NAME.put("CRAFTED_SNOW", Material.CRAFTED_SNOW);
        BY_NAME.put("CACTUS", Material.CACTUS);
        BY_NAME.put("CLAY", Material.CLAY);
        BY_NAME.put("GOURD", Material.GOURD);
        BY_NAME.put("DRAGON_EGG", Material.DRAGON_EGG);
        BY_NAME.put("PORTAL", Material.PORTAL);
        BY_NAME.put("CAKE", Material.CAKE);
        BY_NAME.put("WEB", Material.WEB);
        BY_NAME.put("PISTON", Material.PISTON);
        BY_NAME.put("BARRIER", Material.BARRIER);
        BY_NAME.put("STRUCTURE_VOID", Material.STRUCTURE_VOID);
    }

    private MaterialResolver() {
    }

    public static Set<Material> resolve(List<String> names) {
        if (names == null || names.isEmpty()) return Collections.emptySet();
        Set<Material> result = new HashSet<>(names.size());
        for (String name : names) {
            Material material = BY_NAME.get(name.toUpperCase(Locale.ROOT));
            if (material != null) {
                result.add(material);
            } else {
                TinkersSchema.LOGGER.warn("Unknown block material: {}", name);
            }
        }
        return result;
    }
}