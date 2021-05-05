package com.anet.qtr4tdm.common.blocks;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.tiles.TerminalRadarTile;
import com.anet.qtr4tdm.uebki.BlockTileEntity;
import com.anet.qtr4tdm.uebki.gui.TerminalRadarGui;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TerminalRadarBlock extends BlockTileEntity<TerminalRadarTile> {

    public TerminalRadarBlock() {
        super("terminal_radar", Material.IRON, 3.0f, 10.0f, SoundType.ANVIL);
        this.setCreativeTab(TdmMod.qtr4);
    }

    @Override
    public Class<TerminalRadarTile> getTileEntityClass() {
        return TerminalRadarTile.class;
    }

    @Override
    public TerminalRadarTile createTileEntity(World world, IBlockState blockState) {
        return new TerminalRadarTile();
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote)
            ClientGuiOpen(worldIn, pos);
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public void ClientGuiOpen (World worldIn, BlockPos pos) {
        Minecraft.getMinecraft().displayGuiScreen(new TerminalRadarGui((TerminalRadarTile)worldIn.getTileEntity(pos)));
    }
    
}
