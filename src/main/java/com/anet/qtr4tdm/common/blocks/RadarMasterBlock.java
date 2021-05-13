package com.anet.qtr4tdm.common.blocks;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.tiles.RadarWorkerTile;
import com.anet.qtr4tdm.uebki.BlockTileEntity;
import com.anet.qtr4tdm.common.tiles.RadarWorkerTile;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RadarMasterBlock extends BlockTileEntity<RadarWorkerTile> {
    public RadarMasterBlock() {
  
        super("radar_master", Material.IRON, 3.0f, 10.0f, SoundType.METAL);
    }
    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState p_180633_3_) {
        if (!world.isRemote)
        ((RadarWorkerTile)world.getTileEntity(pos)).firstCheck();
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
    public void breakBlock (World world, BlockPos pos, IBlockState state) {
        ((RadarWorkerTile)world.getTileEntity(pos)).Decomposition();
    }
}
