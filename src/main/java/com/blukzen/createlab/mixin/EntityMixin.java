package com.blukzen.createlab.mixin;

import com.blukzen.createlab.dimension.LabDimensions;
import com.blukzen.createlab.util.GUIUtil;
import com.blukzen.createlab.util.IEntityMixin;
import com.blukzen.createlab.world.LabTeleporter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntityMixin {
    @Unique
    private boolean insideLabPortal;
    @Unique
    private int labPortalTime = 0;
    @Unique
    private int labPortalCooldown = 0;
    @Unique
    private final int labPortalWaittime = 50;

    @Shadow
    public World level;

    @Shadow
    public abstract Entity changeDimension(ServerWorld destination, ITeleporter teleporter);

    @Shadow
    public abstract boolean isPassenger();

    @Shadow
    public abstract boolean is(Entity p_70028_1_);

    @Shadow
    public abstract EntityType<?> getType();

    @Inject(method = "baseTick", at = @At("TAIL"))
    public void baseTick(CallbackInfo ci) {
        this.handleLabPortal();
    }

    @Override
    public void handleInsideLabPortal() {
        if (this.isOnLabPortalCooldown()) {
            this.setLabPortalCooldown();
        } else {
            this.insideLabPortal = true;
        }
    }

    @Override
    public void handleLabPortal() {
        if (this.level instanceof ServerWorld) {
            int i = this.labPortalWaittime;
            ServerWorld serverWorld = (ServerWorld) this.level;

            if (this.insideLabPortal) {
                MinecraftServer server = serverWorld.getServer();
                RegistryKey<World> registryKey = this.level.dimension() == LabDimensions.LABDIM ? World.OVERWORLD : LabDimensions.LABDIM;
                ServerWorld destinationWorld = server.getLevel(registryKey);

                if (destinationWorld != null && !this.isPassenger() && this.labPortalTime++ >= i) {
                    this.level.getProfiler().push("portal");
                    this.labPortalTime = 0;
                    this.setLabPortalCooldown();
                    this.changeDimension(destinationWorld, new LabTeleporter());
                    this.level.getProfiler().pop();
                }
            } else {
                if (this.labPortalTime > 0) {
                    this.labPortalTime--;
                }
            }

            if (getType() == EntityType.PLAYER) {
                GUIUtil guiUtil = GUIUtil.INSTANCE;
                guiUtil.addDebugMessage("In Portal", String.valueOf(this.insideLabPortal));
                guiUtil.addDebugMessage("Portal Timer", String.valueOf(this.labPortalTime));
                guiUtil.addDebugMessage("Portal Countdown", String.valueOf(this.labPortalCooldown));
            }

            this.insideLabPortal = false;
            this.processLabPortalCooldown();
        }
    }

    @Override
    public boolean isInsideLabPortal() {
        return this.insideLabPortal;
    }

    @Override
    public boolean isOnLabPortalCooldown() {
        return this.labPortalCooldown > 0;
    }

    @Override
    public void setLabPortalCooldown() {
        this.labPortalCooldown = 20;
    }

    @Override
    public int getLabPortalCooldown() {
        return this.labPortalCooldown;
    }

    protected void processLabPortalCooldown() {
        if (this.isOnLabPortalCooldown()) {
            --this.labPortalCooldown;
        }
    }
}
