package com.blukzen.createlab.item;

import com.blukzen.createlab.util.GUIUtil;
import com.blukzen.createlab.world.LabPortal;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class PortalCatalystItem extends Item {
    public PortalCatalystItem() {
        super(LabItems.defaultBuilder().stacksTo(1));
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        BlockState blockState = world.getBlockState(context.getClickedPos());

        if (FMLEnvironment.dist.isClient()) {
            GUIUtil.INSTANCE.addDebugMessage("Catalyst Target", blockState.getBlock().getName().getString());
        }

        LabPortal.findEmptyPortalShape(world, context.getClickedPos().above(), Direction.Axis.Z).ifPresent((labPortal -> {
            labPortal.createPortalBlocks();
        }));

        return super.useOn(context);
    }
}
