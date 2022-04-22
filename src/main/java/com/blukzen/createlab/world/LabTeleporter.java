package com.blukzen.createlab.world;

import com.blukzen.createlab.CreateLab;
import com.blukzen.createlab.block.LabBlocks;
import com.blukzen.createlab.data.ILaboratory;
import com.blukzen.createlab.data.LaboratoryCapability;
import com.blukzen.createlab.dimension.LabDimensions;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.TeleportationRepositioner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameType;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nullable;
import java.util.function.Function;

public class LabTeleporter implements ITeleporter {
    private final BlockPos spawn = new BlockPos(0, 64, 0);

    protected final ServerWorld level;

    public LabTeleporter(ServerWorld level) {
        this.level = level;
    }

    @Nullable
    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerWorld dest, Function<ServerWorld, PortalInfo> defaultPortalInfo) {
        CreateLab.LOGGER.info("Teleporting " + entity.getDisplayName() + " to " + dest.dimension().getRegistryName().getPath());
        if (dest.dimension() == LabDimensions.LABDIM) {
            if (entity instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) entity;
                entity.getCapability(LaboratoryCapability.LABORATORY_CAPABILITY).ifPresent(lab -> {
                    lab.savePlayerData(player);
                    lab.resetPlayer(player);
                    player.setGameMode(GameType.CREATIVE);
                });
            }

            return new PortalInfo(Vector3d.atCenterOf(spawn), Vector3d.ZERO, entity.yRot, entity.xRot);
        } else {
            if (entity instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) entity;
                ILaboratory lab = entity.getCapability(LaboratoryCapability.LABORATORY_CAPABILITY).orElseThrow(RuntimeException::new);

                lab.resetPlayer(player);
                lab.restorePlayerData(player);
                return new PortalInfo(Vector3d.atCenterOf(lab.getPosition()), Vector3d.ZERO, entity.yRot, entity.xRot);
            }

            return new PortalInfo(Vector3d.atCenterOf(spawn), Vector3d.ZERO, entity.yRot, entity.xRot);
        }
    }

    public Boolean hasPortal() {
        return this.level.getBlockState(new BlockPos(0, 64, 0)).getBlock() == LabBlocks.LAB_PORTAL.get();
    }

    public void createPortal(Direction.Axis p_242956_2_) {
        BlockPos portalPosition = this.spawn;
        Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, p_242956_2_);
        double d0 = -1.0D;
        BlockPos blockpos = null;
        double d1 = -1.0D;
        BlockPos blockpos1 = null;
        WorldBorder worldborder = this.level.getWorldBorder();
        int i = this.level.getHeight() - 1;
        BlockPos.Mutable blockpos$mutable = portalPosition.mutable();

        for (BlockPos.Mutable blockpos$mutable1 : BlockPos.spiralAround(portalPosition, 16, Direction.EAST, Direction.SOUTH)) {
            int j = Math.min(i, this.level.getHeight(Heightmap.Type.MOTION_BLOCKING, blockpos$mutable1.getX(), blockpos$mutable1.getZ()));
            int k = 1;
            if (worldborder.isWithinBounds(blockpos$mutable1) && worldborder.isWithinBounds(blockpos$mutable1.move(direction, 1))) {
                blockpos$mutable1.move(direction.getOpposite(), 1);

                for (int l = j; l >= 0; --l) {
                    blockpos$mutable1.setY(l);
                    if (this.level.isEmptyBlock(blockpos$mutable1)) {
                        int i1;
                        for (i1 = l; l > 0 && this.level.isEmptyBlock(blockpos$mutable1.move(Direction.DOWN)); --l) {
                        }

                        if (l + 4 <= i) {
                            int j1 = i1 - l;
                            if (j1 <= 0 || j1 >= 3) {
                                blockpos$mutable1.setY(l);
                                if (this.canHostFrame(blockpos$mutable1, blockpos$mutable, direction, 0)) {
                                    double d2 = portalPosition.distSqr(blockpos$mutable1);
                                    if (this.canHostFrame(blockpos$mutable1, blockpos$mutable, direction, -1) && this.canHostFrame(blockpos$mutable1, blockpos$mutable, direction, 1) && (d0 == -1.0D || d0 > d2)) {
                                        d0 = d2;
                                        blockpos = blockpos$mutable1.immutable();
                                    }

                                    if (d0 == -1.0D && (d1 == -1.0D || d1 > d2)) {
                                        d1 = d2;
                                        blockpos1 = blockpos$mutable1.immutable();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (d0 == -1.0D && d1 != -1.0D) {
            blockpos = blockpos1;
            d0 = d1;
        }

        if (d0 == -1.0D) {
            blockpos = (new BlockPos(portalPosition.getX(), MathHelper.clamp(portalPosition.getY(), 70, this.level.getHeight() - 10), portalPosition.getZ())).immutable();
            Direction direction1 = direction.getClockWise();
            if (!worldborder.isWithinBounds(blockpos)) {
                return;
            }

            for (int l1 = -1; l1 < 2; ++l1) {
                for (int k2 = 0; k2 < 2; ++k2) {
                    for (int i3 = -1; i3 < 3; ++i3) {
                        BlockState blockstate1 = i3 < 0 ? Blocks.SMOOTH_QUARTZ.defaultBlockState() : Blocks.AIR.defaultBlockState();
                        blockpos$mutable.setWithOffset(blockpos, k2 * direction.getStepX() + l1 * direction1.getStepX(), i3, k2 * direction.getStepZ() + l1 * direction1.getStepZ());
                        this.level.setBlockAndUpdate(blockpos$mutable, blockstate1);
                    }
                }
            }
        }

        for (int k1 = -1; k1 < 3; ++k1) {
            for (int i2 = -1; i2 < 4; ++i2) {
                if (k1 == -1 || k1 == 2 || i2 == -1 || i2 == 3) {
                    blockpos$mutable.setWithOffset(blockpos, k1 * direction.getStepX(), i2, k1 * direction.getStepZ());
                    this.level.setBlock(blockpos$mutable, Blocks.SMOOTH_QUARTZ.defaultBlockState(), 3);
                }
            }
        }

        BlockState blockstate = LabBlocks.LAB_PORTAL.get().defaultBlockState().setValue(NetherPortalBlock.AXIS, p_242956_2_);

        for (int j2 = 0; j2 < 2; ++j2) {
            for (int l2 = 0; l2 < 3; ++l2) {
                blockpos$mutable.setWithOffset(blockpos, j2 * direction.getStepX(), l2, j2 * direction.getStepZ());
                this.level.setBlock(blockpos$mutable, blockstate, 18);
            }
        }

        new TeleportationRepositioner.Result(blockpos.immutable(), 2, 3);
    }

    private boolean canHostFrame(BlockPos p_242955_1_, BlockPos.Mutable p_242955_2_, Direction p_242955_3_, int p_242955_4_) {
        Direction direction = p_242955_3_.getClockWise();

        for (int i = -1; i < 3; ++i) {
            for (int j = -1; j < 4; ++j) {
                p_242955_2_.setWithOffset(p_242955_1_, p_242955_3_.getStepX() * i + direction.getStepX() * p_242955_4_, j, p_242955_3_.getStepZ() * i + direction.getStepZ() * p_242955_4_);
                if (j < 0 && !this.level.getBlockState(p_242955_2_).getMaterial().isSolid()) {
                    return false;
                }

                if (j >= 0 && !this.level.isEmptyBlock(p_242955_2_)) {
                    return false;
                }
            }
        }

        return true;
    }
}
