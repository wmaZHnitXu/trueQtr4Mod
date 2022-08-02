package com.anet.qtr4tdm.common.blocks;

import java.util.ArrayList;

import com.anet.qtr4tdm.common.tiles.TurretMasterTe;
import com.anet.qtr4tdm.common.tiles.TurretSlaveTe;
import com.anet.qtr4tdm.init.BlocksInit;
import com.anet.qtr4tdm.uebki.BlockTileEntity;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TurretSlaveBlock extends BlockTileEntity<TurretSlaveTe> {

    public TurretSlaveBlock() {
        super("turret_slave", Material.IRON, 3, 3, SoundType.METAL);
    }

    @Override
    public Class<TurretSlaveTe> getTileEntityClass() {
        return TurretSlaveTe.class;
    }

    @Override
    public TurretSlaveTe createTileEntity(World world, IBlockState blockState) {
        return new TurretSlaveTe();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
            EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TurretSlaveTe && !te.isInvalid()) {
                TurretMasterTe master = ((TurretSlaveTe)te).getMaster();
                if (master != null && !master.isInvalid()) master.Interaction(playerIn);
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TurretSlaveTe && !te.isInvalid()) {
                ((TurretSlaveTe)te).Destroy();
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
            int fortune) {
        drops.clear();
        drops.add(new ItemStack(BlocksInit.TURRETBASE));

    }
    
}
