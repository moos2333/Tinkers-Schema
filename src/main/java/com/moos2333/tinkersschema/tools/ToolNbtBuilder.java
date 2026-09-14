package com.moos2333.tinkersschema.tools;

import com.moos2333.tinkersschema.config.ToolConfig;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.ToolNBT;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ToolNbtBuilder {

    private ToolNbtBuilder() {
    }

    public static ToolNBT build(ToolConfig config, List<Material> materials) {
        List<HeadMaterialStats> heads = new ArrayList<>();
        List<HandleMaterialStats> handles = new ArrayList<>();
        List<ExtraMaterialStats> extras = new ArrayList<>();

        List<ToolConfig.PartConfig> parts = config.getParts();
        int size = Math.min(parts.size(), materials.size());
        for (int i = 0; i < size; i++) {
            String type = parts.get(i).getType().toLowerCase(Locale.ROOT);
            Material material = materials.get(i);
            switch (type) {
                case "head":   heads.add(material.getStatsOrUnknown("head"));     break;
                case "handle": handles.add(material.getStatsOrUnknown("handle")); break;
                case "extra":  extras.add(material.getStatsOrUnknown("extra"));   break;
            }
        }

        ToolNBT data = new ToolNBT();
        if (!heads.isEmpty())   data.head(heads.toArray(new HeadMaterialStats[0]));
        if (!extras.isEmpty())  data.extra(extras.toArray(new ExtraMaterialStats[0]));
        if (!handles.isEmpty()) data.handle(handles.toArray(new HandleMaterialStats[0]));

        ToolConfig.StatsConfig stats = config.getStats();
        data.attack     *= stats.getAttackMultiplier();
        data.attack     += stats.getBonusAttack();
        data.durability *= stats.getDurabilityMultiplier();
        return data;
    }
}