package com.moos2333.tinkersschema.tools;

import com.moos2333.tinkersschema.config.ToolConfig;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.IToolPart;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public final class PartTypeResolver {

    private static final Map<String, Function<IToolPart, PartMaterialType>> FACTORIES = new HashMap<>();

    static {
        FACTORIES.put("head", PartMaterialType::head);
        FACTORIES.put("handle", PartMaterialType::handle);
        FACTORIES.put("extra", PartMaterialType::extra);
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

    private static PartMaterialType resolve(ToolConfig.PartConfig config) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(config.getItem()));
        if (item == null) {
            throw new IllegalArgumentException("Unknown part item: " + config.getItem());
        }
        if (!(item instanceof IToolPart)) {
            throw new IllegalArgumentException("Item is not a Tinkers' tool part: " + config.getItem());
        }
        IToolPart part = (IToolPart) item;
        String type = config.getType().toLowerCase(Locale.ROOT);
        Function<IToolPart, PartMaterialType> factory = FACTORIES.get(type);
        return factory != null ? factory.apply(part) : new PartMaterialType(part, type);
    }
}