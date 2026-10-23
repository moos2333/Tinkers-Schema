package com.moos2333.tinkersschema.tools;

import com.moos2333.tinkersschema.config.ToolConfig;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tools.ToolNBT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface DynamicTool {

    Map<String, Category> CATEGORY_MAP = buildCategoryMap();

    static Map<String, Category> buildCategoryMap() {
        Map<String, Category> map = new HashMap<>();
        map.put("TOOL",       Category.TOOL);
        map.put("WEAPON",     Category.WEAPON);
        map.put("HARVEST",    Category.HARVEST);
        map.put("AOE",        Category.AOE);
        map.put("LAUNCHER",   Category.LAUNCHER);
        map.put("PROJECTILE", Category.PROJECTILE);
        map.put("NO_MELEE",   Category.NO_MELEE);
        return Collections.unmodifiableMap(map);
    }

    ToolConfig getConfig();

    default int[] getRepairParts(int... defaultParts) {
        int[] parts = getConfig().getRepairParts();
        return parts != null ? parts : defaultParts;
    }

    default float getRepairModifierForPart(int index) {
        Float modifier = getConfig().getRepairModifiers() != null
                ? getConfig().getRepairModifiers().get(String.valueOf(index))
                : null;
        return modifier != null ? modifier : getConfig().getStats().getRepairModifier();
    }

    default void applyModifiers(ToolNBT data) {
        Integer mods = getConfig().getModifiers();
        if (mods != null && mods >= 0) {
            data.modifiers = mods;
        }
    }

    default Category[] resolveCategories() {
        List<String> categories = getConfig().getCategories();
        if (categories == null || categories.isEmpty()) {
            return new Category[0];
        }
        List<Category> result = new ArrayList<>(categories.size());
        for (String cat : categories) {
            Category category = CATEGORY_MAP.get(cat.toUpperCase(Locale.ROOT));
            if (category != null) {
                result.add(category);
            }
        }
        return result.toArray(new Category[0]);
    }
}