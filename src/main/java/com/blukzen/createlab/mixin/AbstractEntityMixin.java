package com.blukzen.createlab.mixin;

import com.blukzen.createlab.util.IEntityMixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class AbstractEntityMixin extends Entity {
    public AbstractEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void baseTick(CallbackInfo ci) {
        ((IEntityMixin) this).handleLabPortal();
    }
}
