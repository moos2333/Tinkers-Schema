package com.moos2333.tinkersschema.tools;

import com.moos2333.tinkersschema.TinkersSchema;
import com.moos2333.tinkersschema.config.ToolConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import slimeknights.tconstruct.library.materials.ArrowShaftMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.FletchingMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ProjectileNBT;
import slimeknights.tconstruct.library.tools.ranged.ProjectileCore;
import slimeknights.tconstruct.tools.common.entity.EntityArrow;
import slimeknights.tconstruct.tools.common.entity.EntityBolt;
import slimeknights.tconstruct.tools.common.entity.EntityShuriken;

import java.util.ArrayList;
import java.util.List;

public abstract class DynamicProjectile extends ProjectileCore implements DynamicTool {

    private static final Category[] DEFAULT_CATEGORIES = {Category.NO_MELEE, Category.PROJECTILE};
    private static final int[] DEFAULT_REPAIR_PARTS = {1};

    private static final int SHURIKEN = 0;
    private static final int ARROW = 1;
    private static final int BOLT = 2;

    private final ToolConfig config;
    private final int projectileKind;
    private final int[] repairParts;

    protected DynamicProjectile(ToolConfig config) {
        super(PartTypeResolver.resolveAll(config.getParts()));
        this.config = config;
        this.projectileKind = resolveProjectile(config.getProjectile());
        this.repairParts = DynamicTool.sanitizeRepairParts(config, DEFAULT_REPAIR_PARTS);
        setDurabilityPerAmmo(Math.max(1, config.getDurabilityPerAmmo()));

        setRegistryName(config.getResourceLocation());
        setTranslationKey(config.getTranslationKey());
        Category[] categories = resolveCategories();
        addCategory(categories.length > 0 ? categories : DEFAULT_CATEGORIES);
    }

    private static int resolveProjectile(String name) {
        if (name == null || name.isEmpty() || "shuriken".equalsIgnoreCase(name)) return SHURIKEN;
        if ("arrow".equalsIgnoreCase(name)) return ARROW;
        if ("bolt".equalsIgnoreCase(name)) return BOLT;
        TinkersSchema.LOGGER.warn("Unknown projectile '{}', falling back to shuriken", name);
        return SHURIKEN;
    }

    @Override
    public ToolConfig getConfig() { return config; }

    @Override
    public float damagePotential() { return config.getStats().getDamagePotential(); }

    @Override
    public int[] getRepairParts() { return repairParts; }

    @Override
    public float getRepairModifierForPart(int index) {
        return DynamicTool.super.getRepairModifierForPart(index);
    }

    @Override
    public void addMaterialTraits(NBTTagCompound root, List<Material> materials) {
        super.addMaterialTraits(root, materials);
        TraitApplier.apply(config, root);
    }

    @Override
    public ProjectileNBT buildTagData(List<Material> materials) {
        List<HeadMaterialStats> heads = new ArrayList<>();
        List<ArrowShaftMaterialStats> shafts = new ArrayList<>();
        List<FletchingMaterialStats> fletchings = new ArrayList<>();
        List<ExtraMaterialStats> extras = new ArrayList<>();

        int size = Math.min(requiredComponents.length, materials.size());
        for (int i = 0; i < size; i++) {
            PartMaterialType part = requiredComponents[i];
            Material material = materials.get(i);
            if (part.usesStat("head")) {
                heads.add(material.getStatsOrUnknown("head"));
            }
            if (part.usesStat("shaft")) {
                shafts.add(material.getStatsOrUnknown("shaft"));
            }
            if (part.usesStat("fletching")) {
                fletchings.add(material.getStatsOrUnknown("fletching"));
            }
            if (part.usesStat("extra")) {
                extras.add(material.getStatsOrUnknown("extra"));
            }
        }

        ProjectileNBT data = new ProjectileNBT();
        if (!heads.isEmpty())      data.head(heads.toArray(new HeadMaterialStats[0]));
        if (!extras.isEmpty())     data.extra(extras.toArray(new ExtraMaterialStats[0]));
        if (!fletchings.isEmpty()) data.fletchings(fletchings.toArray(new FletchingMaterialStats[0]));
        if (!shafts.isEmpty())     data.shafts(this, shafts.toArray(new ArrowShaftMaterialStats[0]));

        ToolConfig.StatsConfig stats = config.getStats();
        data.attack     *= stats.getAttackMultiplier();
        data.attack     += stats.getBonusAttack();
        data.durability *= stats.getDurabilityMultiplier();

        applyModifiers(data);
        return data;
    }

    @Override
    public EntityProjectileBase getProjectile(ItemStack stack, ItemStack launcher, World world, EntityPlayer player,
                                              float speed, float inaccuracy, float progress, boolean usedAmmo) {
        ItemStack projectile = getProjectileStack(stack, world, player, usedAmmo);
        float deviation = 1f - 1f / Math.max(0.01f, ProjectileNBT.from(stack).accuracy);
        float spread = inaccuracy - deviation * speed * 0.5f;
        switch (projectileKind) {
            case ARROW: return new EntityArrow(world, player, speed, spread, progress, projectile, launcher);
            case BOLT:  return new EntityBolt(world, player, speed, spread, progress, projectile, launcher);
            default:    return new EntityShuriken(world, player, speed, spread, projectile, launcher);
        }
    }
}
