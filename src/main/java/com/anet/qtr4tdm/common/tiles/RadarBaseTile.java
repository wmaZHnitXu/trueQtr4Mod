package com.anet.qtr4tdm.common.tiles;

import java.util.ArrayList;
import java.util.Collection;

import com.anet.qtr4tdm.common.blocks.RadarBaseBlock;
import com.anet.qtr4tdm.init.BlocksInit;
import com.typesafe.config.ConfigException.Null;
import java.util.Collections;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import scala.actors.threadpool.Arrays;

public class RadarBaseTile extends TileEntity {
    private int count;
    private boolean isMaster;
    private ArrayList<RadarBaseTile> slaves;
    private RadarBaseTile master;
    public RadarBaseTile () {
    
    }
    public int getCount() {

        return this.count;
    }
    public void CheckBlocks() {
        slaves = new ArrayList<RadarBaseTile>();
        int Xstart = this.pos.getX() - 5;
        int Xend = this.pos.getX() + 5;
        int Zstart = this.pos.getZ() - 5;
        int Zend = this.pos.getZ() + 5;
        for (int x = Xstart; x < Xend; x++)
            for (int z = Zstart; z < Zend; z++) {
                BlockPos finderPos = new BlockPos(x, this.pos.getY(), z);
                if (world.getBlockState(finderPos).getBlock().getClass() == RadarBaseBlock.class)
                    slaves.add((RadarBaseTile)world.getTileEntity(finderPos));
            }
    }

    public void CheckStructureComplete () {
        CheckBlocks();
        int xCenter = 0;
        int zCenter = 0;
        BlockPos center;
        switch (slaves.size()) {
            case 5:
                for (int i = 0; i < 5; i++) {
                    BlockPos pos = slaves.get(i).pos;
                    xCenter += pos.getX();
                    zCenter += pos.getZ();
                }
                xCenter = xCenter / 5;
                zCenter = zCenter / 5; 
                center = new BlockPos(xCenter, slaves.get(0).getPos().getY(), zCenter);
                if (world.getBlockState(center.west()).getBlock().getClass() == RadarBaseBlock.class 
                && world.getBlockState(center.south()).getBlock().getClass() == RadarBaseBlock.class) {
                    MakeWorkingStructure(center);
                }
            break;
            case 9:
                for (int i = 0; i < 9; i++) {
                    BlockPos pos = slaves.get(i).pos;
                    xCenter += pos.getX();
                    zCenter += pos.getZ();
                }
                xCenter = xCenter / 9;
                zCenter = zCenter / 9;
                center = new BlockPos(xCenter, slaves.get(0).getPos().getY(), zCenter);
                if (world.getBlockState(center.west()).getBlock().getClass() == RadarBaseBlock.class 
                && world.getBlockState(center.south()).getBlock().getClass() == RadarBaseBlock.class
                && world.getBlockState(center.south().west()).getBlock().getClass() == RadarBaseBlock.class) {
                    MakeWorkingStructure(center);
                }
            break;
            case 21:
                for (int i = 0; i < 21; i++) {
                    BlockPos pos = slaves.get(i).pos;
                    xCenter += pos.getX();
                    zCenter += pos.getZ();
                }
                xCenter = xCenter / 21;
                zCenter = zCenter / 21;
                center = new BlockPos(xCenter, slaves.get(0).getPos().getY(), zCenter);
                boolean checkRes = true;
                for (int x = center.getX()-2; x < center.getX() + 3; x++) {
                    for (int z = center.getZ()-2; z < center.getZ() + 3; z++) {
                        if (world.getBlockState(new BlockPos(x, center.getY(), z)).getBlock().getClass() != RadarBaseBlock.class 
                        && !(Math.abs(xCenter - x) == 2 && Math.abs(zCenter - z) == 2))
                            checkRes = false;
                    }
                }
                if (checkRes) {
                    MakeWorkingStructure(center);
                }
            break;
        }
    }

    private void MakeWorkingStructure (BlockPos center) {
        world.setBlockState(center, BlocksInit.RADARM.getDefaultState());
    }
}   
