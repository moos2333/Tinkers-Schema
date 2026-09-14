package com.moos2333.tinkersschema.config;

import com.google.gson.Gson;
import com.moos2333.tinkersschema.Tags;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ToolConfigLoader {

    private static final Gson GSON = new Gson();
    private static final List<ToolConfig> CONFIGS = new ArrayList<>();

    private ToolConfigLoader() {
    }

    public static void load(File configDir, Logger logger) {
        CONFIGS.clear();
        File dir = new File(configDir, Tags.MOD_ID + "/tools");
        if (!dir.exists() && !dir.mkdirs()) {
            logger.warn("Failed to create directory: {}", dir);
            return;
        }
        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (files == null) return;
        Set<String> ids = new HashSet<>();
        for (File file : files) {
            try (FileReader reader = new FileReader(file)) {
                ToolConfig config = GSON.fromJson(reader, ToolConfig.class);
                if (!isValid(config)) {
                    logger.warn("Skipping invalid config: {}", file.getName());
                    continue;
                }
                if (!ids.add(config.getId())) {
                    logger.warn("Duplicate tool id '{}' in {}", config.getId(), file.getName());
                    continue;
                }
                CONFIGS.add(config);
            } catch (Exception e) {
                logger.error("Failed to load: {}", file.getName(), e);
            }
        }
        logger.info("Loaded {} tool config(s)", CONFIGS.size());
    }

    private static boolean isValid(ToolConfig config) {
        return config != null
                && config.getId() != null
                && config.getTooltype() != null
                && config.getParts() != null
                && !config.getParts().isEmpty();
    }

    public static List<ToolConfig> getConfigs() {
        return Collections.unmodifiableList(CONFIGS);
    }
}