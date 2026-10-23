package com.moos2333.tinkersschema.tools;

import com.moos2333.tinkersschema.config.ToolConfig;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tools.ToolNBT;

import java.util.ArrayList;
import java.util.List;

public interface DynamicTool {

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
        List<Category> result = new ArrayList<>(2);
        for (String cat : categories) {
            if ("WEAPON".equalsIgnoreCase(cat)) result.add(Category.WEAPON);
            else if ("HARVEST".equalsIgnoreCase(cat)) result.add(Category.HARVEST);
        }
        return result.toArray(new Category[0]);
    }
}