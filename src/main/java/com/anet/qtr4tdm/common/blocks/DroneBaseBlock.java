package com.anet.qtr4tdm.common.blocks;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.supers.BlockDefenceSystem;
import com.anet.qtr4tdm.common.tiles.DroneBaseTile;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DroneBaseBlock extends BlockDefenceSystem<DroneBaseTile> {

    public DroneBaseBlock() {
        super("dronebase", Material.IRON, 5, 2, SoundType.METAL);
        setCreativeTab(TdmMod.qtr4);
    }

    @Override
    public Class getTileEntityClass() {
        return DroneBaseTile.class;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState blockState) {
        return new DroneBaseTile();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
            EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof DroneBaseTile) {
                ((DroneBaseTile)te).openInventory(playerIn);
            }
        }
        return true;
    }
}
