package com.moos2333.tinkersschema.tools;

import com.moos2333.tinkersschema.config.ToolConfig;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.IToolPart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class PartTypeResolver {

    private static final Map<String, String[]> TYPES = new HashMap<>();

    static {
        TYPES.put("head",       new String[]{"head"});
        TYPES.put("handle",     new String[]{"handle"});
        TYPES.put("extra",      new String[]{"extra"});
        TYPES.put("bowlimb",    new String[]{"bow", "head"});
        TYPES.put("bowstring",  new String[]{"bowstring"});
        TYPES.put("arrowhead",  new String[]{"head", "projectile"});
        TYPES.put("arrowshaft", new String[]{"shaft"});
        TYPES.put("fletching",  new String[]{"fletching"});
    }

    private PartTypeResolver() {
    }

    public static PartMaterialType[] resolveAll(List<ToolConfig.PartConfig> parts) {
        PartMaterialType[] types = new PartMaterialType[parts.size()];
        for (int i = 0; i < parts.size(); i++) {
            types[i] = resolve(parts.get(i));
        }
        return types;
    }

    public static boolean usesStat(String type, String statId) {
        if (type == null || type.isEmpty()) return false;
        if (type.indexOf(',') < 0) {
            String key = type.trim().toLowerCase(Locale.ROOT);
            String[] predefined = TYPES.get(key);
            if (predefined != null) {
                for (String id : predefined) {
                    if (id.equals(statId)) return true;
                }
                return false;
            }
            return key.equals(statId);
        }
        for (String raw : type.toLowerCase(Locale.ROOT).split(",")) {
            String key = raw.trim();
            if (key.isEmpty()) continue;
            String[] predefined = TYPES.get(key);
            if (predefined != null) {
                for (String id : predefined) {
                    if (id.equals(statId)) return true;
                }
            } else if (key.equals(statId)) {
                return true;
            }
        }
        return false;
    }

    private static PartMaterialType resolve(ToolConfig.PartConfig config) {
        String itemName = config.getItem();
        if (itemName == null) {
            throw new IllegalArgumentException("Part item is null");
        }
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
        if (!(item instanceof IToolPart)) {
            throw new IllegalArgumentException("Not a Tinkers' tool part: " + itemName);
        }
        String[] statIds = expand(config.getType());
        if (statIds.length == 0) {
            throw new IllegalArgumentException("Part type resolves to no stat IDs: '" + config.getType() + "' for " + itemName);
        }
        return new PartMaterialType((IToolPart) item, statIds);
    }

    private static String[] expand(String type) {
        if (type == null || type.isEmpty()) return new String[0];
        List<String> statIds = new ArrayList<>(2);
        for (String raw : type.toLowerCase(Locale.ROOT).split(",")) {
            String key = raw.trim();
            if (key.isEmpty()) continue;
            String[] predefined = TYPES.get(key);
            if (predefined != null) {
                for (String id : predefined) {
                    if (!statIds.contains(id)) statIds.add(id);
                }
            } else if (!statIds.contains(key)) {
                statIds.add(key);
            }
        }
        return statIds.toArray(new String[0]);
    }
}