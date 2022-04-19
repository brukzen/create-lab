package com.blukzen.createlab.mixin;

import com.blukzen.createlab.CreateLabConfig;
import com.blukzen.createlab.dimension.LabDimensions;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    Predicate<ItemStack> DISABLED_ITEMS = (ItemStack itemStack) -> CreateLabConfig.disabledItems.get().contains(itemStack.getItem().getRegistryName().toString());

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (this.level.dimension().equals(LabDimensions.LABDIM)) {
            if (DISABLED_ITEMS.test(getMainHandItem())) {
                getMainHandItem().setCount(0);
            }
        }
    }
}
