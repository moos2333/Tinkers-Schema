package com.moos2333.tinkersschema.tools;

import com.moos2333.tinkersschema.Tags;
import net.minecraft.util.ResourceLocation;
import slimeknights.tconstruct.common.ModelRegisterUtil;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.tools.TinkerModifiers;

public final class ModifierRegister {

    private ModifierRegister() {
    }

    public static void registerModifierModel(IModifier modifier, ResourceLocation location) {
        ModelRegisterUtil.registerModifierModel(modifier, location);
    }

    public static void registerAll() {
        register(TinkerModifiers.modSharpness, "sharpness");
        register(TinkerModifiers.modDiamond, "diamond");
        register(TinkerModifiers.modEmerald, "emerald");
        register(TinkerModifiers.modHaste, "haste");
        register(TinkerModifiers.modFiery, "fiery");
        register(TinkerModifiers.modKnockback, "knockback");
        register(TinkerModifiers.modBaneOfArthopods, "bane_of_arthopods");
        register(TinkerModifiers.modBeheading, "beheading");
        register(TinkerModifiers.modGlowing, "glowing");
        register(TinkerModifiers.modLuck, "luck");
        register(TinkerModifiers.modMendingMoss, "mending_moss");
        register(TinkerModifiers.modNecrotic, "necrotic");
        register(TinkerModifiers.modReinforced, "reinforced");
        register(TinkerModifiers.modShulking, "shulking");
        register(TinkerModifiers.modSilktouch, "silktouch");
        register(TinkerModifiers.modSmite, "smite");
        register(TinkerModifiers.modSoulbound, "soulbound");
        register(TinkerModifiers.modWebbed, "webbed");
    }

    private static void register(IModifier modifier, String name) {
        if (modifier == null) return;
        registerModifierModel(modifier, new ResourceLocation(Tags.MOD_ID, "models/item/modifiers/" + name));
    }
}