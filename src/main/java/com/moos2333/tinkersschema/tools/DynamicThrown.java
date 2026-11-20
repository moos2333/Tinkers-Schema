package com.moos2333.tinkersschema.tools;

import com.moos2333.tinkersschema.config.ToolConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.utils.ToolHelper;

public class DynamicThrown extends DynamicProjectile {

    private final float throwSpeed;
    private final int cooldown;

    public DynamicThrown(ToolConfig config) {
        super(config);
        this.throwSpeed = Math.max(0.1f, config.getThrowSpeed());
        this.cooldown = Math.max(0, config.getCooldown());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (ToolHelper.isBroken(stack)) {
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }
        if (cooldown > 0) {
            player.getCooldownTracker().setCooldown(stack.getItem(), cooldown);
        }
        if (!world.isRemote) {
            boolean usedAmmo = useAmmo(stack, player);
            world.spawnEntity(getProjectile(stack, stack, world, player, throwSpeed, 0f, 1f, usedAmmo));
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}
