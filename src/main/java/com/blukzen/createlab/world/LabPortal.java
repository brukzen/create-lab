package com.blukzen.createlab.world;

import com.blukzen.createlab.CreateLab;
import com.blukzen.createlab.block.LabBlocks;
import com.blukzen.createlab.block.LabPortalBlock;
import net.minecraft.block.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

public class LabPortal {
    private static final AbstractBlock.IPositionPredicate FRAME = (p_242966_0_, p_242966_1_, p_242966_2_) -> {
        return p_242966_0_.getBlock().is(Blocks.SMOOTH_QUARTZ);
    };
    private final IWorld level;
    private final Direction.Axis axis;
    private final Direction rightDir;
    private int numPortalBlocks;
    @Nullable
    private BlockPos bottomLeft;
    private int height;
    private int width;

    public static Optional<LabPortal> findEmptyPortalShape(IWorld p_242964_0_, BlockPos p_242964_1_, Direction.Axis p_242964_2_) {
        return findPortalShape(p_242964_0_, p_242964_1_, (p_242968_0_) -> {
            return p_242968_0_.isValid() && p_242968_0_.numPortalBlocks == 0;
        }, p_242964_2_);
    }

    public static Optional<LabPortal> findPortalShape(IWorld p_242965_0_, BlockPos p_242965_1_, Predicate<LabPortal> p_242965_2_, Direction.Axis p_242965_3_) {
        Optional<LabPortal> optional = Optional.of(new LabPortal(p_242965_0_, p_242965_1_, p_242965_3_)).filter(p_242965_2_);
        if (optional.isPresent()) {
            return optional;
        } else {
            Direction.Axis direction$axis = p_242965_3_ == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
            return Optional.of(new LabPortal(p_242965_0_, p_242965_1_, direction$axis)).filter(p_242965_2_);
        }
    }

    public LabPortal(IWorld level, BlockPos p_i48740_2_, Direction.Axis p_i48740_3_) {
        this.level = level;
        this.axis = p_i48740_3_;
        this.rightDir = p_i48740_3_ == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
        this.bottomLeft = this.calculateBottomLeft(p_i48740_2_);
        if (this.bottomLeft == null) {
            this.bottomLeft = p_i48740_2_;
            this.width = 1;
            this.height = 1;
        } else {
            this.width = this.calculateWidth();
            if (this.width > 0) {
                this.height = this.calculateHeight();
            }
        }

        CreateLab.LOGGER.info("Portal Definition");
    }

    @Nullable
    private BlockPos calculateBottomLeft(BlockPos pos) {
        for (int i = Math.max(0, pos.getY() - 21); pos.getY() > i && isEmpty(this.level.getBlockState(pos.below())); pos = pos.below()) {
        }

        Direction direction = this.rightDir.getOpposite();
        int j = this.getDistanceUntilEdgeAboveFrame(pos, direction) - 1;
        return j < 0 ? null : pos.relative(direction, j);
    }

    private int calculateWidth() {
        int i = this.getDistanceUntilEdgeAboveFrame(this.bottomLeft, this.rightDir);
        return i >= 2 && i <= 21 ? i : 0;
    }

    private int getDistanceUntilEdgeAboveFrame(BlockPos p_242972_1_, Direction p_242972_2_) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for (int i = 0; i <= 21; ++i) {
            blockpos$mutable.set(p_242972_1_).move(p_242972_2_, i);
            BlockState blockstate = this.level.getBlockState(blockpos$mutable);
            if (!isEmpty(blockstate)) {
                if (FRAME.test(blockstate, this.level, blockpos$mutable)) {
                    return i;
                }
                break;
            }

            BlockState blockstate1 = this.level.getBlockState(blockpos$mutable.move(Direction.DOWN));
            if (!FRAME.test(blockstate1, this.level, blockpos$mutable)) {
                break;
            }
        }

        return 0;
    }

    private int calculateHeight() {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        int i = this.getDistanceUntilTop(blockpos$mutable);
        return i >= 3 && i <= 21 && this.hasTopFrame(blockpos$mutable, i) ? i : 0;
    }

    private boolean hasTopFrame(BlockPos.Mutable p_242970_1_, int p_242970_2_) {
        for (int i = 0; i < this.width; ++i) {
            BlockPos.Mutable blockpos$mutable = p_242970_1_.set(this.bottomLeft).move(Direction.UP, p_242970_2_).move(this.rightDir, i);
            if (!FRAME.test(this.level.getBlockState(blockpos$mutable), this.level, blockpos$mutable)) {
                return false;
            }
        }

        return true;
    }

    private int getDistanceUntilTop(BlockPos.Mutable p_242969_1_) {
        for (int i = 0; i < 21; ++i) {
            p_242969_1_.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, -1);
            if (!FRAME.test(this.level.getBlockState(p_242969_1_), this.level, p_242969_1_)) {
                return i;
            }

            p_242969_1_.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, this.width);
            if (!FRAME.test(this.level.getBlockState(p_242969_1_), this.level, p_242969_1_)) {
                return i;
            }

            for (int j = 0; j < this.width; ++j) {
                p_242969_1_.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, j);
                BlockState blockstate = this.level.getBlockState(p_242969_1_);
                if (!isEmpty(blockstate)) {
                    return i;
                }

                if (blockstate.is(Blocks.NETHER_PORTAL)) {
                    ++this.numPortalBlocks;
                }
            }
        }

        return 21;
    }

    private static boolean isEmpty(BlockState p_196900_0_) {
        return p_196900_0_.isAir() || p_196900_0_.is(BlockTags.FIRE) || p_196900_0_.is(Blocks.NETHER_PORTAL);
    }

    public boolean isValid() {
        return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
    }

    public void createPortalBlocks() {
        BlockState blockstate = LabBlocks.LAB_PORTAL.get().defaultBlockState().setValue(LabPortalBlock.AXIS, this.axis);
        BlockPos.betweenClosed(this.bottomLeft, this.bottomLeft.relative(Direction.UP, this.height - 1).relative(this.rightDir, this.width - 1)).forEach((p_242967_2_) -> {
            this.level.setBlock(p_242967_2_, blockstate, 18);
        });
    }

    public boolean isComplete() {
        return this.isValid() && this.numPortalBlocks == this.width * this.height;
    }
}
