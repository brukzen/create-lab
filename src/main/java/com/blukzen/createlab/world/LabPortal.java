package com.blukzen.createlab.world;

import com.blukzen.createlab.block.LabBlocks;
import com.blukzen.createlab.block.LabPortalBlock;
import com.blukzen.createlab.util.GUIUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.fml.loading.FMLEnvironment;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

public class LabPortal {
    private static final AbstractBlock.IPositionPredicate FRAME = (blockState, blockReader, blockPos) -> {
        return blockState.getBlock().is(Blocks.SMOOTH_QUARTZ);
    };
    private final IWorld level;
    private final Direction.Axis axis;
    private final Direction rightDir;
    private int numPortalBlocks;
    @Nullable
    private BlockPos bottomLeft;
    private int height;
    private int width;

    public static Optional<LabPortal> findEmptyPortalShape(IWorld world, BlockPos blockPos, Direction.Axis axis) {
        return findPortalShape(world, blockPos, (labPortal) -> {
            return labPortal.isValid() && labPortal.numPortalBlocks == 0;
        }, axis);
    }

    public static Optional<LabPortal> findPortalShape(IWorld world, BlockPos blockPos, Predicate<LabPortal> labPortalPredicate, Direction.Axis axis) {
        Optional<LabPortal> optional = Optional.of(new LabPortal(world, blockPos, axis)).filter(labPortalPredicate);
        if (optional.isPresent()) {
            return optional;
        } else {
            Direction.Axis direction$axis = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
            return Optional.of(new LabPortal(world, blockPos, direction$axis)).filter(labPortalPredicate);
        }
    }

    public LabPortal(IWorld level, BlockPos blockPos, Direction.Axis axis) {
        this.level = level;
        this.axis = axis;
        this.rightDir = axis == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
        this.bottomLeft = this.calculateBottomLeft(blockPos);
        if (this.bottomLeft == null) {
            this.bottomLeft = blockPos;
            this.width = 1;
            this.height = 1;
        } else {
            this.width = this.calculateWidth();
            if (this.width > 0) {
                this.height = this.calculateHeight();
            }
        }

        if (FMLEnvironment.dist.isClient()) {
            GUIUtil gui = GUIUtil.INSTANCE;
            gui.addDebugMessage("Portal Width", String.valueOf(width));
            gui.addDebugMessage("Portal Height", String.valueOf(height));
            gui.addDebugMessage("Portal Axis", this.axis.getName());
            gui.addDebugMessage("Portal Right", rightDir.getName());
            gui.addDebugMessage("Portal Bottom Left", bottomLeft.toString());
        }

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

    private int getDistanceUntilEdgeAboveFrame(BlockPos blockPos, Direction direction) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for (int i = 0; i <= 21; ++i) {
            blockpos$mutable.set(blockPos).move(direction, i);
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

    private boolean hasTopFrame(BlockPos.Mutable blockPos, int p_242970_2_) {
        for (int i = 0; i < this.width; ++i) {
            BlockPos.Mutable blockpos$mutable = blockPos.set(this.bottomLeft).move(Direction.UP, p_242970_2_).move(this.rightDir, i);
            if (!FRAME.test(this.level.getBlockState(blockpos$mutable), this.level, blockpos$mutable)) {
                return false;
            }
        }

        return true;
    }

    private int getDistanceUntilTop(BlockPos.Mutable blockPos) {
        for (int i = 0; i < 21; ++i) {
            blockPos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, -1);
            if (!FRAME.test(this.level.getBlockState(blockPos), this.level, blockPos)) {
                return i;
            }

            blockPos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, this.width);
            if (!FRAME.test(this.level.getBlockState(blockPos), this.level, blockPos)) {
                return i;
            }

            for (int j = 0; j < this.width; ++j) {
                blockPos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, j);
                BlockState blockstate = this.level.getBlockState(blockPos);
                if (!isEmpty(blockstate)) {
                    return i;
                }

                if (blockstate.is(LabBlocks.LAB_PORTAL.get())) {
                    ++this.numPortalBlocks;
                }
            }
        }

        return 21;
    }

    private static boolean isEmpty(BlockState blockState) {
        return blockState.isAir() || blockState.is(BlockTags.FIRE) || blockState.is(LabBlocks.LAB_PORTAL.get());
    }

    public boolean isValid() {
        return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
    }

    public void createPortalBlocks() {
        BlockState blockstate = LabBlocks.LAB_PORTAL.get().defaultBlockState().setValue(LabPortalBlock.AXIS, this.axis);
        BlockPos.betweenClosed(this.bottomLeft, this.bottomLeft.relative(Direction.UP, this.height - 1).relative(this.rightDir, this.width - 1)).forEach((blockPos) -> {
            this.level.setBlock(blockPos, blockstate, 18);
        });
    }

    public boolean isComplete() {
        return this.isValid() && this.numPortalBlocks == this.width * this.height;
    }
}
