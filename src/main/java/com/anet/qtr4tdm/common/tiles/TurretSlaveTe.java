package com.anet.qtr4tdm.common.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TurretSlaveTe extends TileEntity {
    
    public TurretMasterTe getMaster () {
        for (int x = pos.getX() - 1; x < pos.getX() + 2; x++) {
            for (int z = pos.getZ() - 1; z < pos.getZ() + 2; z++) {
                TileEntity te = world.getTileEntity(new BlockPos(x, pos.getY(), z));
                if (te instanceof TurretMasterTe) return (TurretMasterTe)te;
            }
        }
        return null;
    }

    public void Destroy () {
        TurretMasterTe master = getMaster();
        if (master != null) master.Disassemble(pos);
    }
}
