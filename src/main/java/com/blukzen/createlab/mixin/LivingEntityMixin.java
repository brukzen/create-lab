package com.blukzen.createlab.mixin;


import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {
    @Shadow public abstract boolean addEffect(EffectInstance p_195064_1_);

    protected LivingEntityMixin(Class<Entity> baseClass) {
        super(baseClass);
    }
}
