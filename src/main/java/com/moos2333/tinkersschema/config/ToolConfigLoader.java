package com.moos2333.tinkersschema.config;

import com.google.gson.Gson;
import com.moos2333.tinkersschema.Tags;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ToolConfigLoader {

    private static final Gson GSON = new Gson();
    private static final List<ToolConfig> CONFIGS = new ArrayList<>();
    private static final Set<String> IDS = new HashSet<>();

    private ToolConfigLoader() {
    }

    public static void load(File configDir, Logger logger) {
        CONFIGS.clear();
        IDS.clear();
        File dir = new File(configDir, Tags.MOD_ID + "/tools");
        if (!dir.exists() && !dir.mkdirs()) {
            logger.warn("Failed to create directory: {}", dir);
            return;
        }
        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (files == null) return;
        for (File file : files) {
            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
                ToolConfig config = GSON.fromJson(reader, ToolConfig.class);
                if (!addConfig(config)) {
                    logger.warn("Skipped tool config: {}", file.getName());
                }
            } catch (Exception e) {
                logger.error("Failed to load: {}", file.getName(), e);
            }
        }
        logger.info("Loaded {} tool config(s)", CONFIGS.size());
    }

    public static boolean addConfig(ToolConfig config) {
        if (config == null || !isValid(config)) return false;
        if (!IDS.add(config.getId())) return false;
        CONFIGS.add(config);
        return true;
    }

    public static ToolConfig parseJson(String json) {
        return GSON.fromJson(json, ToolConfig.class);
    }

    private static boolean isValid(ToolConfig config) {
        return config.getId() != null
                && config.getTooltype() != null
                && config.getParts() != null
                && !config.getParts().isEmpty();
    }

    public static List<ToolConfig> getConfigs() {
        return Collections.unmodifiableList(CONFIGS);
    }
}