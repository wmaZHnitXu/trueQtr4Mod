package com.anet.qtr4tdm.common.supers;

import com.anet.qtr4tdm.uebki.BlockTileEntity;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockOwnableTE<T extends TileEntityOwnable> extends BlockTileEntity<T> {

    public BlockOwnableTE(String name, Material material, float hardness, float resistanse, SoundType soundType) {
        super(name, material, hardness, resistanse, soundType);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
            ItemStack stack) {
        // TODO Auto-generated method stub
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }
    
}
