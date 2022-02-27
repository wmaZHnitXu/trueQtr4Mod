package com.anet.qtr4tdm.common.blocks;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.tiles.TurretBaseTe;
import com.anet.qtr4tdm.uebki.BlockTileEntity;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TurretBaseBlock extends BlockTileEntity<TurretBaseTe> {

    public TurretBaseBlock() {
        super("turret_base", Material.IRON, 3f, 1f, SoundType.METAL);
        setCreativeTab(TdmMod.qtr4);
    }

    @Override
    public Class getTileEntityClass() {
        return TurretBaseTe.class;
    }

    @Override
    public TurretBaseTe createTileEntity(World world, IBlockState blockState) {
        return new TurretBaseTe();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
            EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TurretBaseTe && !te.isInvalid()) {
                ((TurretBaseTe)te).proceedInteraction();
            }
        }
        return true;

    }
    
}
