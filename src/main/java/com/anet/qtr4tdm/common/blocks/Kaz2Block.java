package com.anet.qtr4tdm.common.blocks;

import com.anet.qtr4tdm.common.tiles.Kaz1Tile;


import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class Kaz2Block extends Kaz1Block {

    public Kaz2Block () {
        super("kaz2");
    }

    @Override
    public Kaz1Tile createTileEntity(World world, IBlockState blockState) {
        return new Kaz1Tile(2);
    }

}
