package com.anet.qtr4tdm.common.blocks;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.tiles.ThermalBaseTile;
import com.anet.qtr4tdm.uebki.BlockTileEntity;

import net.minecraft.block.BlockFurnace;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ThermalRadarBlock extends BlockTileEntity<ThermalBaseTile> {

    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    protected static final AxisAlignedBB AABB_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.65D, 1.0D);

    public ThermalRadarBlock() {
        super("thermal_base", Material.IRON, 3, 1, SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(ACTIVE, false));
        setLightOpacity(0);
        setCreativeTab(TdmMod.qtr4);
    }

    @Override
    public Class<ThermalBaseTile> getTileEntityClass() {
        return ThermalBaseTile.class;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public ThermalBaseTile createTileEntity(World world, IBlockState blockState) {
        return new ThermalBaseTile();
    }

    public static void SetActive (World world, BlockPos pos, boolean active) {
        TileEntity tile = world.getTileEntity(pos);

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof ThermalRadarBlock) {
            world.setBlockState(pos, state.withProperty(ACTIVE, active));
        }

        if (tile != null) {
            tile.validate();
            world.setTileEntity(pos, tile);
        }
    }
   
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
            ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        ((ThermalBaseTile)worldIn.getTileEntity(pos)).Destruction();
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean isBurning(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB_HALF;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{ACTIVE});
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ACTIVE) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(ACTIVE, meta == 1);
    }
    
}
