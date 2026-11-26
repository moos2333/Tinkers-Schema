package com.moos2333.tinkersschema.tools;

import com.moos2333.tinkersschema.config.ToolConfig;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tools.ToolNBT;

import java.util.ArrayList;
import java.util.Arrays;
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

    static int[] sanitizeRepairParts(ToolConfig config, int[] fallback) {
        int[] configured = config.getRepairParts();
        int[] source = configured != null ? configured : fallback;
        if (source.length == 0) return source;
        int count = config.getParts().size();
        int[] valid = new int[source.length];
        int size = 0;
        for (int index : source) {
            if (index >= 0 && index < count) valid[size++] = index;
        }
        if (size == 0) return new int[]{0};
        return size == source.length ? source : Arrays.copyOf(valid, size);
    }

    default float getRepairModifierForPart(int index) {
        Map<String, Float> modifiers = getConfig().getRepairModifiers();
        Float modifier = modifiers != null ? modifiers.get(String.valueOf(index)) : null;
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