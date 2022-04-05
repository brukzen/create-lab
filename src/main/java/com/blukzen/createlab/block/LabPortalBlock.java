package com.blukzen.createlab.block;

import com.blukzen.createlab.util.IEntityMixin;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class LabPortalBlock extends Block {
    public static final EnumProperty<Direction.Axis> AXIS;
    protected static final VoxelShape X_AXIS_AABB;
    protected static final VoxelShape Z_AXIS_AABB;

    static {
        AXIS = BlockStateProperties.HORIZONTAL_AXIS;
        X_AXIS_AABB = Block.box(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
        Z_AXIS_AABB = Block.box(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);
    }

    public LabPortalBlock(Properties props) {
        super(props);
    }

    @Override
    @Deprecated
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.getValue(AXIS)) {
            case Z:
                return Z_AXIS_AABB;
            case X:
            default:
                return X_AXIS_AABB;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(new Property[]{AXIS});
    }

    @Override
    public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!entityIn.isOnPortalCooldown()) {
            ((IEntityMixin) entityIn).setInLabPortal();
        }
    }


}
