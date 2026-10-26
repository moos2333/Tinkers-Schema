package com.moos2333.tinkersschema.tools;

import com.moos2333.tinkersschema.TinkersSchema;
import com.moos2333.tinkersschema.config.ToolConfig;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.BowStringMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.ProjectileLauncherNBT;
import slimeknights.tconstruct.library.tools.ranged.BowCore;
import slimeknights.tconstruct.library.tools.ranged.IAmmo;
import slimeknights.tconstruct.tools.ranged.TinkerRangedWeapons;

import java.util.ArrayList;
import java.util.List;

public class DynamicBow extends BowCore implements DynamicTool {

    private final ToolConfig config;
    private final List<Item> ammoItems;
    private final Float drawMoveSpeed;
    private final float baseProjectileSpeed;

    public DynamicBow(ToolConfig config) {
        super(PartTypeResolver.resolveAll(config.getParts()));
        this.config = config;
        this.ammoItems = resolveAmmo(config.getAmmo());
        this.drawMoveSpeed = config.getStats().getDrawMoveSpeed();
        this.baseProjectileSpeed = 3.0f * config.getStats().getProjectileSpeedMultiplier();

        setRegistryName(config.getResourceLocation());
        setTranslationKey(config.getTranslationKey());
        addCategory(resolveCategories());
    }

    private static List<Item> resolveAmmo(List<String> ids) {
        if (ids == null || ids.isEmpty()) return null;
        List<Item> result = new ArrayList<>(ids.size());
        for (String id : ids) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
            if (item == null) {
                TinkersSchema.LOGGER.warn("Unknown ammo item: {}", id);
                continue;
            }
            if (!(item instanceof IAmmo) && !(item instanceof ItemArrow)) {
                TinkersSchema.LOGGER.warn("Item '{}' is not a valid ammo type, skipping", id);
                continue;
            }
            result.add(item);
        }
        if (result.isEmpty()) {
            TinkersSchema.LOGGER.warn("No valid ammo entries, falling back to default arrows");
            return null;
        }
        return result;
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
    public float damageCutoff() { return config.getStats().getDamageCutoff(); }

    @Override
    public float miningSpeedModifier() { return config.getStats().getMiningSpeedModifier(); }

    @Override
    public float baseProjectileDamage() { return config.getStats().getBaseProjectileDamage(); }

    @Override
    public float projectileDamageModifier() { return config.getStats().getProjectileDamageModifier(); }

    @Override
    protected float baseInaccuracy() { return config.getStats().getBaseInaccuracy(); }

    @Override
    protected float baseProjectileSpeed() { return baseProjectileSpeed; }

    @Override
    protected List<Item> getAmmoItems() {
        return ammoItems != null ? ammoItems : TinkerRangedWeapons.getDiscoveredArrows();
    }

    @Override
    public int[] getRepairParts() {
        return getRepairParts(new int[]{0, 1});
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (drawMoveSpeed != null) {
            preventSlowDown(entityIn, drawMoveSpeed);
        }
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public void addMaterialTraits(NBTTagCompound root, List<Material> materials) {
        super.addMaterialTraits(root, materials);
        TraitApplier.apply(config, root);
    }

    @Override
    public ProjectileLauncherNBT buildTagData(List<Material> materials) {
        List<HeadMaterialStats> heads = new ArrayList<>();
        List<BowMaterialStats> limbs = new ArrayList<>();
        List<ExtraMaterialStats> extras = new ArrayList<>();
        List<BowStringMaterialStats> bowstrings = new ArrayList<>();

        List<ToolConfig.PartConfig> parts = config.getParts();
        int size = Math.min(parts.size(), materials.size());
        for (int i = 0; i < size; i++) {
            String type = parts.get(i).getType();
            Material material = materials.get(i);
            if (PartTypeResolver.usesStat(type, "bow")) {
                heads.add(material.getStatsOrUnknown("head"));
                limbs.add(material.getStatsOrUnknown("bow"));
            }
            if (PartTypeResolver.usesStat(type, "bowstring")) {
                bowstrings.add(material.getStatsOrUnknown("bowstring"));
            }
            if (PartTypeResolver.usesStat(type, "extra")) {
                extras.add(material.getStatsOrUnknown("extra"));
            }
        }

        ProjectileLauncherNBT data = new ProjectileLauncherNBT();
        if (!heads.isEmpty())      data.head(heads.toArray(new HeadMaterialStats[0]));
        if (!limbs.isEmpty())      data.limb(limbs.toArray(new BowMaterialStats[0]));
        if (!extras.isEmpty())     data.extra(extras.toArray(new ExtraMaterialStats[0]));
        if (!bowstrings.isEmpty()) data.bowstring(bowstrings.toArray(new BowStringMaterialStats[0]));

        ToolConfig.StatsConfig stats = config.getStats();
        data.drawSpeed   = Math.max(0.001f, data.drawSpeed * stats.getDrawSpeedMultiplier());
        data.range       = Math.max(0.001f, data.range * stats.getRangeMultiplier());
        data.bonusDamage = data.bonusDamage * stats.getBonusDamageMultiplier();

        data.attack     *= stats.getAttackMultiplier();
        data.attack     += stats.getBonusAttack();
        data.durability *= stats.getDurabilityMultiplier();

        applyModifiers(data);
        return data;
    }
}