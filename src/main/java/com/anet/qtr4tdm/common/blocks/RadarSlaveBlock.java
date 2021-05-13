package com.anet.qtr4tdm.common.blocks;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.tiles.RadarWorkerTile;
import com.anet.qtr4tdm.uebki.BlockTileEntity;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RadarSlaveBlock extends BlockTileEntity<RadarWorkerTile> {
    public RadarSlaveBlock() {
  
        super("radar_slave", Material.IRON, 3.0f, 10.0f, SoundType.METAL);
    }    
    @Override
    public Class<RadarWorkerTile> getTileEntityClass() {

        return RadarWorkerTile.class;
    }

    @Override
    public RadarWorkerTile createTileEntity(World world, IBlockState blockState) {

        return new RadarWorkerTile();
    }
    @Override
    public void breakBlock (World worldIn, BlockPos pos, IBlockState state) {
        RadarWorkerTile tile = (RadarWorkerTile)worldIn.getTileEntity(pos);
        tile.GetMaster().RemoveSlave(tile);
    }
}
