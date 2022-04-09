package com.blukzen.createlab.mixin;

import com.blukzen.createlab.accessor.ClientPlayerEntityAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntityMixin implements ClientPlayerEntityAccessor {

    @Shadow
    @Final
    protected Minecraft minecraft;

    @Shadow
    public abstract void closeContainer();

    @Unique
    protected float oldLabPortalTime;

    @Override
    public float getOldLabPortalTime() {
        return this.oldLabPortalTime;
    }

    protected ClientPlayerEntityMixin(Class<Entity> baseClass) {
        super(baseClass);
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    public void aiStep(CallbackInfo ci) {
        this.handleLabPortalClient();
    }

    private void handleLabPortalClient() {
        this.oldLabPortalTime = this.labPortalTime;

        if (this.insideLabPortal) {
            if (this.minecraft.screen != null && !this.minecraft.screen.isPauseScreen()) {
                if (this.minecraft.screen instanceof ContainerScreen) {
                    this.closeContainer();
                }

                this.minecraft.setScreen(null);
            }

            if (this.labPortalTime == 0.0f) {
                this.minecraft.getSoundManager().play(
                        SimpleSound.forLocalAmbience(
                                SoundEvents.PORTAL_TRIGGER,
                                this.random.nextFloat() * 0.4f + 0.8f,
                                0.25F
                        )
                );
            }

            this.labPortalTime += 0.02f;

            if (this.labPortalTime >= 1.0f) {
                this.labPortalTime = 1.0f;
            }

            this.insideLabPortal = false;
        } else {
            if (this.labPortalTime > 0.0F) {
                this.labPortalTime -= 0.05F;
            }

            if (this.labPortalTime < 0.0F) {
                this.labPortalTime = 0.0F;
            }
        }
    }
}
