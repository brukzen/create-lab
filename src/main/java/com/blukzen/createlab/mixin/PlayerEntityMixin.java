package com.blukzen.createlab.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    protected PlayerEntityMixin(Class<Entity> baseClass) {
        super(baseClass);
    }
}
