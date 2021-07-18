package com.anet.qtr4tdm.common.blocks;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.tiles.BaseTile;
import com.anet.qtr4tdm.uebki.BlockTileEntity;
import com.anet.qtr4tdm.uebki.gui.BaseSetupGui;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseBlock extends BlockTileEntity<BaseTile>  {

    public BaseBlock() {
        super("base_block", Material.IRON, 1, 1, SoundType.METAL);
        this.setCreativeTab(TdmMod.qtr4);
    }

    @Override
    public Class getTileEntityClass() {
        return BaseTile.class;
    }

    @Override
    public BaseTile createTileEntity(World world, IBlockState blockState) {
        return new BaseTile();
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof BaseTile) ((BaseTile)tile).Interaction(playerIn);
        }
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof BaseTile) ((BaseTile)tile).BaseBlockDestroy();
        }
        super.breakBlock(worldIn, pos, state);
    }
}
