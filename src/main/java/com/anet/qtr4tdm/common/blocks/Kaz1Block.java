package com.anet.qtr4tdm.common.blocks;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.supers.TileEntityDefence;
import com.anet.qtr4tdm.common.tiles.Kaz1Tile;
import com.anet.qtr4tdm.uebki.BlockTileEntity;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.registries.GameData;

public class Kaz1Block extends BlockTileEntity<Kaz1Tile> {

    public static PropertyBool POWERED = PropertyBool.create("powered");
    public static PropertyBool CONNECTED = PropertyBool.create("connected");
    public static PropertyInteger AMMO = PropertyInteger.create("ammo", 0, 3);
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public Kaz1Block() {
        this("kaz1");
    }

    public Kaz1Block(String name) {
        super(name, Material.IRON, 5, 3, SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(CONNECTED, false).withProperty(POWERED, false).withProperty(AMMO, 0).withProperty(FACING, EnumFacing.NORTH));
        this.setCreativeTab(TdmMod.qtr4);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof Kaz1Tile) {
            Kaz1Tile kaztile = (Kaz1Tile)tile;
            boolean connected = kaztile.highlightTime > 0;
            boolean powered = kaztile.highlightTime > 0;
            state = state.withProperty(CONNECTED, connected).withProperty(POWERED, powered);
        }
        return state;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof Kaz1Tile) ((Kaz1Tile)tile).Interaction(playerIn);
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof Kaz1Tile) {
            Kaz1Tile kaztile = (Kaz1Tile)te;
            kaztile.Destruction();
        }
        super.breakBlock(worldIn, pos, state);
    }

    public static void SetAmmo(World world, BlockPos pos, int ammo) {
        
        ammo = Math.min(ammo, 3);
        TileEntity tile = world.getTileEntity(pos);

        IBlockState state = world.getBlockState(pos);
        boolean highlight = false;
        if (state.getBlock() instanceof Kaz1Block) {
            if (state.getValue(AMMO) > ammo && tile != null && tile instanceof Kaz1Tile) {
                ((Kaz1Tile)tile).highlightTime = 40;
                highlight = true;
            }
            world.setBlockState(pos, state.withProperty(AMMO, ammo).withProperty(POWERED, highlight).withProperty(CONNECTED, highlight));
        }

        if (tile != null) {
            tile.validate();
            world.setTileEntity(pos, tile);
        }
    }

    @Override
    public Class<Kaz1Tile> getTileEntityClass() {
        return Kaz1Tile.class;
    }

    @Override
    public Kaz1Tile createTileEntity(World world, IBlockState blockState) {
        return new Kaz1Tile(1);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) { 
        IBlockState state = this.getDefaultState();
        int rot = meta / 4;
        int ammo = meta % 4;
        return state.withProperty(AMMO, ammo).withProperty(FACING, EnumFacing.getHorizontal(rot));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int result = state.getValue(AMMO);
        result += state.getValue(FACING).getHorizontalIndex() * 4;
        return result;
    }

    @Override
    protected BlockStateContainer createBlockState() {

        return new BlockStateContainer(this, new IProperty[] {POWERED, CONNECTED, AMMO, FACING});
        
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
            float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
            facing = EnumFacing.NORTH;
            if (placer != null) {
                BlockPos poscorrect = new BlockPos(pos.getX(), placer.getPosition().getY(), pos.getZ());
                facing = EnumFacing.getDirectionFromEntityLiving(poscorrect, placer);
            }
        }
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(facing.getHorizontalIndex()));
    }
}