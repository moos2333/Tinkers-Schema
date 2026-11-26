package com.moos2333.tinkersschema.tools;

import com.moos2333.tinkersschema.config.ToolConfig;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ToolNBT;

import java.util.ArrayList;
import java.util.List;

public final class ToolNbtBuilder {

    private ToolNbtBuilder() {
    }

    public static ToolNBT build(ToolConfig.StatsConfig stats, PartMaterialType[] parts, List<Material> materials) {
        List<HeadMaterialStats> heads = new ArrayList<>();
        List<HandleMaterialStats> handles = new ArrayList<>();
        List<ExtraMaterialStats> extras = new ArrayList<>();

        int size = Math.min(parts.length, materials.size());
        for (int i = 0; i < size; i++) {
            PartMaterialType part = parts[i];
            Material material = materials.get(i);
            if (part.usesStat("head")) {
                heads.add(material.getStatsOrUnknown("head"));
            }
            if (part.usesStat("handle")) {
                handles.add(material.getStatsOrUnknown("handle"));
            }
            if (part.usesStat("extra")) {
                extras.add(material.getStatsOrUnknown("extra"));
            }
        }

        ToolNBT data = new ToolNBT();
        if (!heads.isEmpty())   data.head(heads.toArray(new HeadMaterialStats[0]));
        if (!extras.isEmpty())  data.extra(extras.toArray(new ExtraMaterialStats[0]));
        if (!handles.isEmpty()) data.handle(handles.toArray(new HandleMaterialStats[0]));

        data.attack     *= stats.getAttackMultiplier();
        data.attack     += stats.getBonusAttack();
        data.durability *= stats.getDurabilityMultiplier();
        return data;
    }
}
