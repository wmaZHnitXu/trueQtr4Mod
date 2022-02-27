package com.anet.qtr4tdm.common.tiles;

import java.util.ArrayList;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.init.BlocksInit;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TurretBaseTe extends TileEntity {

    public void proceedInteraction () {
        BlockPos center = getCenterIfComplete();
        if (center != null && isAssembled(center)) {
            Transform(center);
        }
    }

    public boolean isAssembled (BlockPos center) {
        //Только для 3х3
        TdmMod.logger.info("Checking with center: "  + center);
        for (int i = center.getX() - 1; i < center.getX() + 2; i++) {
            for (int j = center.getZ() - 1; j < center.getZ() + 2; j++) {
                if (!world.getBlockState(new BlockPos(i, center.getY(), j)).getBlock().equals(world.getBlockState(pos).getBlock())) return false;
            }
        }
        TdmMod.logger.info("Assembled");
        return true;
    }

    public BlockPos getCenterIfComplete () {
        ArrayList<EnumFacing> neighboursDirs = new ArrayList<EnumFacing>(); 
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (world.getBlockState(pos).getBlock().equals(world.getBlockState(pos.add(facing.getDirectionVec())).getBlock())) {
                neighboursDirs.add(facing);
            }
        }

        //Только для 3х3
        if (neighboursDirs.size() == 4) return pos;
        if (neighboursDirs.size() == 3) {
            int x = 0;
            int z = 0;
            for (EnumFacing face : neighboursDirs) {
                x += Math.abs(face.getDirectionVec().getX());
                z += Math.abs(face.getDirectionVec().getZ());
            }
            for (EnumFacing face : neighboursDirs) {
                if (x == 1 && face.getDirectionVec().getX() != 0) return pos.add(face.getDirectionVec());
                if (z == 1 && face.getDirectionVec().getZ() != 0) return pos.add(face.getDirectionVec()); 
            }

        }
        if (neighboursDirs.size() == 2) {
            BlockPos centerPos = new BlockPos(pos);
            for (EnumFacing face : neighboursDirs) {
                centerPos = centerPos.add(face.getDirectionVec());
            }
            return centerPos;
        }
        return null;
    }

    public void Transform (BlockPos center) {
        TdmMod.logger.info("assembled turret base with center: " + center);
        IBlockState masterState = BlocksInit.TURRETMASTER.getDefaultState();
        IBlockState slaveState = BlocksInit.TURRETSLAVE.getDefaultState();
        for (int x = center.getX() - 1; x < center.getX() + 2; x++) {
            for (int z = center.getZ() - 1; z < center.getZ() + 2; z++) {
                BlockPos positionToChange = new BlockPos(x, pos.getY(), z);
                if (positionToChange.equals(center)) world.setBlockState(positionToChange, masterState, 3);
                else world.setBlockState(positionToChange, slaveState, 3);
            }
        }
    }
}
