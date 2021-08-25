package com.anet.qtr4tdm.common.blocks;

import java.util.List;

import com.anet.qtr4tdm.common.entities.RadarThermal1Entity;
import com.anet.qtr4tdm.common.tiles.ThermalBaseTile;
import com.anet.qtr4tdm.uebki.BlockTileEntity;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ThermalBaseBlock extends BlockTileEntity<ThermalBaseTile> {

    public ThermalBaseBlock() {
        super("thermal_base", Material.IRON, 3, 1, SoundType.METAL);
    }

    @Override
    public Class<ThermalBaseTile> getTileEntityClass() {
        return ThermalBaseTile.class;
    }

    @Override
    public ThermalBaseTile createTileEntity(World world, IBlockState blockState) {
        return new ThermalBaseTile();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
            ItemStack stack) {
        if (!worldIn.isRemote) {
            RadarThermal1Entity e = new RadarThermal1Entity(worldIn);
            e.setPosition(pos.getX(), pos.up().getY(), pos.getZ());
            worldIn.spawnEntity(e);
        }
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        AxisAlignedBB bb = new AxisAlignedBB(pos.up());
        List<RadarThermal1Entity> entities = worldIn.getEntitiesWithinAABB(RadarThermal1Entity.class, bb);
        if (entities.size() != 0) entities.get(0).setDead();
        super.breakBlock(worldIn, pos, state);
    }
    
}
