package com.moos2333.tinkersschema.tools;

import com.moos2333.tinkersschema.config.ToolConfig;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.Category;
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
        applyCategories(config.getCategories());
    }

    private void applyCategories(List<String> categories) {
        if (categories == null) return;
        for (String cat : categories) {
            if ("WEAPON".equalsIgnoreCase(cat)) addCategory(Category.WEAPON);
            else if ("HARVEST".equalsIgnoreCase(cat)) addCategory(Category.HARVEST);
        }
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
        int[] parts = config.getRepairParts();
        return parts != null ? parts : new int[]{1};
    }

    @Override
    public float getRepairModifierForPart(int index) {
        Float modifier = config.getRepairModifiers() != null
                ? config.getRepairModifiers().get(String.valueOf(index))
                : null;
        return modifier != null ? modifier : config.getStats().getRepairModifier();
    }

    @Override
    public void addMaterialTraits(NBTTagCompound root, List<Material> materials) {
        super.addMaterialTraits(root, materials);
        TraitApplier.apply(config, root);
    }

    @Override
    protected ToolNBT buildTagData(List<Material> materials) {
        return ToolNbtBuilder.build(config, materials);
    }
}