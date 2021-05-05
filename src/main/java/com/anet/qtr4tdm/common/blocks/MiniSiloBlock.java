package com.anet.qtr4tdm.common.blocks;

import com.anet.qtr4tdm.common.tiles.MiniSiloTile;
import com.anet.qtr4tdm.uebki.BlockTileEntity;
import com.jcraft.jorbis.Block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MiniSiloBlock extends BlockTileEntity<MiniSiloTile> {

    public MiniSiloBlock() {
        super("minisilo", Material.TNT, 3, 3, SoundType.METAL);
    }

    @Override
    public Class<MiniSiloTile> getTileEntityClass() {
        return MiniSiloTile.class;
    }

    @Override
    public MiniSiloTile createTileEntity(World world, IBlockState blockState) {
        return new MiniSiloTile();
    }
    
}
