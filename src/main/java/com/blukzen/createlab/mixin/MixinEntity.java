package com.blukzen.createlab.mixin;

import com.blukzen.createlab.dimension.LabDimensions;
import com.blukzen.createlab.util.GUIUtil;
import com.blukzen.createlab.util.IEntityMixin;
import com.blukzen.createlab.world.LabTeleporter;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.INameable;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(Entity.class)
public abstract class MixinEntity extends net.minecraftforge.common.capabilities.CapabilityProvider<Entity> implements INameable, ICommandSource, net.minecraftforge.common.extensions.IForgeEntity, IEntityMixin {
    @Unique
    protected boolean insideLabPortal;
    @Unique
    protected float labPortalTime = 0;
    @Unique
    private float labPortalCooldown = 0;
    @Unique
    private final float labPortalWaittime = 50;

    @Shadow
    public World level;

    protected MixinEntity(Class<Entity> baseClass) {
        super(baseClass);
    }

    @Shadow
    public abstract Entity changeDimension(ServerWorld destination, ITeleporter teleporter);

    @Shadow
    public abstract boolean isPassenger();

    @Shadow
    public abstract boolean is(Entity p_70028_1_);

    @Shadow
    public abstract EntityType<?> getType();

    @Shadow @Final protected Random random;

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
            ServerWorld serverWorld = (ServerWorld) this.level;

            if (this.insideLabPortal) {
                MinecraftServer server = serverWorld.getServer();
                RegistryKey<World> registryKey = this.level.dimension() == LabDimensions.LABDIM ? World.OVERWORLD : LabDimensions.LABDIM;
                ServerWorld destinationWorld = server.getLevel(registryKey);

                if (destinationWorld != null && !this.isPassenger() && this.labPortalTime++ >= this.labPortalWaittime) {
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
    public float getLabPortalCooldown() {
        return this.labPortalCooldown;
    }

    protected void processLabPortalCooldown() {
        if (this.isOnLabPortalCooldown()) {
            --this.labPortalCooldown;
        }
    }
}
