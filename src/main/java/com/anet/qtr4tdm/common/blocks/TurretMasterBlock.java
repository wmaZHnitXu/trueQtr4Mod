package com.anet.qtr4tdm.common.blocks;

import java.util.ArrayList;

import com.anet.qtr4tdm.common.tiles.TurretMasterTe;
import com.anet.qtr4tdm.init.BlocksInit;
import com.anet.qtr4tdm.uebki.BlockTileEntity;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TurretMasterBlock extends BlockTileEntity<TurretMasterTe> {

    public TurretMasterBlock() {
        super("turret_master", Material.IRON, 3, 3, SoundType.METAL);
    }

    @Override
    public Class<TurretMasterTe> getTileEntityClass() {
        return TurretMasterTe.class;
    }

    @Override
    public TurretMasterTe createTileEntity(World world, IBlockState blockState) {
        return new TurretMasterTe();
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
            int fortune) {
        drops.add(new ItemStack(Item.getItemFromBlock(BlocksInit.TURRETBASE)));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
            EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TurretMasterTe && !te.isInvalid()) {
            ((TurretMasterTe)te).Interaction(playerIn);
        }
        return true;
    }
    
}
