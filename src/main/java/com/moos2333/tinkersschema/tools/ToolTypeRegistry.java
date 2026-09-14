package com.moos2333.tinkersschema.tools;

import com.moos2333.tinkersschema.config.ToolConfig;
import slimeknights.tconstruct.library.tools.ToolCore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public final class ToolTypeRegistry {

    private static final Map<String, Function<ToolConfig, ToolCore>> FACTORIES = new HashMap<>();

    static {
        register("sword", DynamicSword::new);
        register("aoe", DynamicAoeTool::new);
    }

    private ToolTypeRegistry() {
    }

    public static void register(String type, Function<ToolConfig, ToolCore> factory) {
        FACTORIES.put(type.toLowerCase(Locale.ROOT), factory);
    }

    public static ToolCore create(ToolConfig config) {
        Function<ToolConfig, ToolCore> factory = FACTORIES.get(config.getTooltype().toLowerCase(Locale.ROOT));
        if (factory == null) {
            throw new IllegalArgumentException("Unknown tooltype: " + config.getTooltype());
        }
        return factory.apply(config);
    }
}