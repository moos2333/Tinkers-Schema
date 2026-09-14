package com.moos2333.tinkersschema.client;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class SlotResolver {

    private static final Map<String, int[]> PRESETS = new HashMap<>();

    static {
        PRESETS.put("topleft",     new int[]{12, 26});
        PRESETS.put("top",         new int[]{30, 26});
        PRESETS.put("topright",    new int[]{48, 26});
        PRESETS.put("left",        new int[]{12, 44});
        PRESETS.put("center",      new int[]{30, 44});
        PRESETS.put("middle",      new int[]{30, 44});
        PRESETS.put("right",       new int[]{48, 44});
        PRESETS.put("bottomleft",  new int[]{12, 62});
        PRESETS.put("bottom",      new int[]{30, 62});
        PRESETS.put("bottomright", new int[]{48, 62});
    }

    private SlotResolver() {
    }

    public static int[] resolve(String slot) {
        if (slot == null || slot.isEmpty()) return null;
        int[] preset = PRESETS.get(slot.toLowerCase(Locale.ROOT));
        if (preset != null) return preset;
        int comma = slot.indexOf(',');
        if (comma > 0) {
            try {
                int x = Integer.parseInt(slot.substring(0, comma).trim());
                int y = Integer.parseInt(slot.substring(comma + 1).trim());
                return new int[]{x, y};
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }
}