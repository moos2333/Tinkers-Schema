package com.moos2333.tinkersschema.tools;

import com.moos2333.tinkersschema.config.ToolConfig;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.SwordCore;
import slimeknights.tconstruct.library.tools.ToolNBT;

import java.util.List;

public class DynamicSword extends SwordCore implements DynamicTool {

    private final ToolConfig config;

    public DynamicSword(ToolConfig config) {
        super(PartTypeResolver.resolveAll(config.getParts()));
        this.config = config;
        setRegistryName(config.getResourceLocation());
        setTranslationKey(config.getTranslationKey());
        addCategory(resolveCategories());
    }

    @Override
    public ToolConfig getConfig() { return config; }

    @Override
    public float damagePotential() { return config.getStats().getDamagePotential(); }

    @Override
    public double attackSpeed() { return config.getStats().getAttackSpeed(); }

    @Override
    public float knockback() { return config.getStats().getKnockback(); }

    @Override
    public float miningSpeedModifier() { return config.getStats().getMiningSpeedModifier(); }

    @Override
    public float damageCutoff() { return config.getStats().getDamageCutoff(); }

    @Override
    public int[] getRepairParts() {
        return getRepairParts(new int[]{1});
    }

    @Override
    public void addMaterialTraits(NBTTagCompound root, List<Material> materials) {
        super.addMaterialTraits(root, materials);
        TraitApplier.apply(config, root);
    }

    @Override
    protected ToolNBT buildTagData(List<Material> materials) {
        ToolNBT data = ToolNbtBuilder.build(config, materials);
        applyModifiers(data);
        return data;
    }
}