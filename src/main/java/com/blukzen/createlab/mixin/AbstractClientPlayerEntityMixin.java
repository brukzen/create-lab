package com.blukzen.createlab.mixin;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntityMixin {
    protected AbstractClientPlayerEntityMixin(Class<Entity> baseClass) {
        super(baseClass);
    }
}
