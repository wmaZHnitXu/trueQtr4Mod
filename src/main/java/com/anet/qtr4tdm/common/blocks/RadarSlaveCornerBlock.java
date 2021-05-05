package com.anet.qtr4tdm.common.blocks;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.tiles.RadarWorkerTile;
import com.anet.qtr4tdm.uebki.BlockTileEntity;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class RadarSlaveCornerBlock extends BlockTileEntity<RadarWorkerTile> {
    public RadarSlaveCornerBlock() {
  
        super("radar_slavecorner", Material.IRON, 3.0f, 10.0f, SoundType.METAL);
        this.setCreativeTab(TdmMod.qtr4); //К примеру BuildingBlocks
    }    
    @Override
    public Class<RadarWorkerTile> getTileEntityClass() {

        return RadarWorkerTile.class;
    }

    @Override
    public RadarWorkerTile createTileEntity(World world, IBlockState blockState) {

        return new RadarWorkerTile();
    }
}
