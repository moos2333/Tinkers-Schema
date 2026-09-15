package com.moos2333.tinkersschema.config;

import com.moos2333.tinkersschema.Tags;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;

public class ToolConfig {

    private static final StatsConfig DEFAULT_STATS = new StatsConfig();

    private String id;
    private String tooltype;
    private String station = "table";
    private List<String> categories;
    private List<PartConfig> parts;
    private List<String> effectiveMaterials;
    private String harvestToolClass;
    private List<String> guiSlots;
    private List<String> traits;
    private int[] repairParts;
    private Map<String, Float> repairModifiers;
    private int[] aoeRange;
    private StatsConfig stats = new StatsConfig();

    private transient ResourceLocation cachedLocation;

    public String getId() { return id; }
    public String getTooltype() { return tooltype; }
    public String getStation() { return station != null ? station : "table"; }
    public List<String> getCategories() { return categories; }
    public List<PartConfig> getParts() { return parts; }
    public List<String> getEffectiveMaterials() { return effectiveMaterials; }
    public String getHarvestToolClass() { return harvestToolClass; }
    public List<String> getGuiSlots() { return guiSlots; }
    public List<String> getTraits() { return traits; }
    public int[] getRepairParts() { return repairParts; }
    public Map<String, Float> getRepairModifiers() { return repairModifiers; }
    public int[] getAoeRange() { return aoeRange; }
    public StatsConfig getStats() { return stats != null ? stats : DEFAULT_STATS; }

    public ResourceLocation getResourceLocation() {
        if (cachedLocation == null) {
            int colon = id.indexOf(':');
            if (colon > 0) {
                cachedLocation = new ResourceLocation(id.substring(0, colon), id.substring(colon + 1));
            } else {
                cachedLocation = new ResourceLocation(Tags.MOD_ID, id);
            }
        }
        return cachedLocation;
    }

    public String getTranslationKey() {
        return id.replace(':', '.');
    }

    public static class PartConfig {
        private String type;
        private String item;
        public String getType() { return type; }
        public String getItem() { return item; }
    }

    public static class StatsConfig {
        private float damagePotential = 1.0f;
        private double attackSpeed = 1.6d;
        private float knockback = 1.0f;
        private float bonusAttack = 0.0f;
        private float attackMultiplier = 1.0f;
        private float durabilityMultiplier = 1.0f;
        private float repairModifier = 1.0f;
        private float miningSpeedModifier = 1.0f;
        private float damageCutoff = 15.0f;

        public float getDamagePotential() { return damagePotential; }
        public double getAttackSpeed() { return attackSpeed; }
        public float getKnockback() { return knockback; }
        public float getBonusAttack() { return bonusAttack; }
        public float getAttackMultiplier() { return attackMultiplier; }
        public float getDurabilityMultiplier() { return durabilityMultiplier; }
        public float getRepairModifier() { return repairModifier; }
        public float getMiningSpeedModifier() { return miningSpeedModifier; }
        public float getDamageCutoff() { return damageCutoff; }
    }
}