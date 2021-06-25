package com.anet.qtr4tdm.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;

import com.anet.qtr4tdm.common.entities.Radar3Entity;
import com.anet.qtr4tdm.common.tiles.RadarBaseTile;
import com.anet.qtr4tdm.common.tiles.RadarBaseTile;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.text.html.parser.Entity;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.uebki.BlockTileEntity;

public class RadarBaseBlock extends BlockTileEntity<RadarBaseTile> {

    public RadarBaseBlock() {
  
      super("radar_base", Material.IRON, 1.0f, 1.0f, SoundType.METAL);
      this.setCreativeTab(TdmMod.qtr4); //К примеру BuildingBlocks
    }
    @Override
    public boolean onBlockActivated(World world, BlockPos position, IBlockState blockState, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

        if (!world.isRemote) {
            if (world.provider.getDimension() != 0) return true;
            RadarBaseTile tileEntity = getTileEntity(world, position);
            tileEntity.CheckStructureComplete();
        }

       

        return true;
    }

    @Override
    public Class<RadarBaseTile> getTileEntityClass() {

        return RadarBaseTile.class;
    }

    @Override
    public RadarBaseTile createTileEntity(World world, IBlockState blockState) {

        return new RadarBaseTile();
    }
}