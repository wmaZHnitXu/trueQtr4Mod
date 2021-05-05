package com.anet.qtr4tdm.common.blocks;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.tiles.EnergyConsumerTile;
import com.anet.qtr4tdm.init.BlocksInit;
import com.anet.qtr4tdm.uebki.BlockTileEntity;
import com.anet.qtr4tdm.uebki.gui.EnergyConsumerGui;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.xml.dtd.impl.WordBerrySethi;

public class EnergyConsumerBlock extends BlockTileEntity<EnergyConsumerTile> {

    public static final PropertyInteger active = PropertyInteger.create("active", Integer.valueOf(0), Integer.valueOf(4));

    public EnergyConsumerBlock() {
        super("energy_consumer", Material.IRON, 3, 3, SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(active, Integer.valueOf(0)));
        this.setCreativeTab(TdmMod.qtr4);
    }
    

    @Override
    public Class getTileEntityClass() {
        return EnergyConsumerTile.class;
    }

    @Override
    public EnergyConsumerTile createTileEntity(World world, IBlockState blockState) {
        return new EnergyConsumerTile();
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState state = this.getDefaultState();
        return state.withProperty(active, Integer.valueOf(meta));
    }

    public static void SetState (World world, BlockPos pos, int stateCode) {
        TileEntity tile = world.getTileEntity(pos);

        world.setBlockState(pos, BlocksInit.ENERGYCONSUMER.getDefaultState().withProperty(EnergyConsumerBlock.active, Integer.valueOf(stateCode)));

        if (tile != null) {
            tile.validate();
            world.setTileEntity(pos, tile);
        }
    }


    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(active);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {active});
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
            EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            ClientGuiOpen(worldIn, pos);
            IBlockState stae  = worldIn.getBlockState(pos);            
        }
        return true;
    }
    @SideOnly(Side.CLIENT)
    public void ClientGuiOpen (World worldIn, BlockPos pos) {
        Minecraft.getMinecraft().displayGuiScreen(new EnergyConsumerGui((EnergyConsumerTile)worldIn.getTileEntity(pos)));
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        ((EnergyConsumerTile)worldIn.getTileEntity(pos)).FireIC2DisconnectEvent();
        super.breakBlock(worldIn, pos, state);
    }
}
