package com.moos2333.tinkersschema.tools;

import com.google.common.collect.ImmutableList;
import com.moos2333.tinkersschema.config.MaterialResolver;
import com.moos2333.tinkersschema.config.ToolConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tools.AoeToolCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.ToolHelper;

import java.util.List;
import java.util.Set;

public class DynamicAoeTool extends AoeToolCore implements DynamicTool {

    private final ToolConfig config;
    private final Set<net.minecraft.block.material.Material> effectiveMaterials;

    public DynamicAoeTool(ToolConfig config) {
        super(PartTypeResolver.resolveAll(config.getParts()));
        this.config = config;
        this.effectiveMaterials = MaterialResolver.resolve(config.getEffectiveMaterials());
        setRegistryName(config.getResourceLocation());
        setTranslationKey(config.getTranslationKey());
        applyCategories(config.getCategories());
        if (config.getHarvestToolClass() != null) {
            setHarvestLevel(config.getHarvestToolClass(), 0);
        }
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
    public boolean isEffective(IBlockState state) {
        return effectiveMaterials.contains(state.getMaterial());
    }

    @Override
    public ImmutableList<BlockPos> getAOEBlocks(ItemStack stack, World world, EntityPlayer player, BlockPos origin) {
        int[] range = config.getAoeRange();
        if (range == null || range.length < 3) {
            range = new int[]{1, 1, 1};
        }
        return ToolHelper.calcAOEBlocks(stack, world, player, origin, range[0], range[1], range[2]);
    }

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