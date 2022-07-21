package com.anet.qtr4tdm.common.blocks;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.tiles.TurretMasterTe;
import com.anet.qtr4tdm.common.tiles.TurretSlaveTe;
import com.anet.qtr4tdm.uebki.uebokblock;

import net.minecraft.block.BlockGlass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TurretCollisionBlock extends uebokblock {
    /*
     *   0  1  2
     *   3  4  5
     *   6  7  8
     */

    public static final AxisAlignedBB boundingBox = new AxisAlignedBB(-0.5d, 0, -0.5d, 1.5d, 1.7d, 1.5d);
    public static final PropertyInteger LOCATION = PropertyInteger.create("location", 0, 8);

    public TurretCollisionBlock() {
        super("turretcollision", Material.IRON, 3, 1, SoundType.METAL);
        setDefaultState(this.blockState.getBaseState().withProperty(LOCATION, 4));
        setLightOpacity(0);
        setCreativeTab(TdmMod.qtr4);
    }

    private AxisAlignedBB getBoxFromLocation (int location) {

        switch (location) {

            case 2:
                return new AxisAlignedBB(0.5d, 0, 0d, 1d, 1.7d, 0.5d);
            case 5://
                return new AxisAlignedBB(0d, 0d, 0d, 1d, 1.7d, 0.5d);
            case 8:
                return new AxisAlignedBB(0d, 0d, 0d, 0.5d, 1.7d, 0.5d);
            case 1://
                return new AxisAlignedBB(0.5d, 0d, 0d, 1d, 1.7d, 1d);
            case 4:
                return new AxisAlignedBB(0d, 0d, 0d, 1d, 1.7d, 1d);
            case 7://
                return new AxisAlignedBB(0d, 0d, 0d, 0.5d, 1.7d, 1d);
            case 0:
                return new AxisAlignedBB(0.5d, 0d, 0.5d, 1d, 1.7d, 1d);
            case 3://
                return new AxisAlignedBB(0, 0d, 0.5d, 1d, 1.7d, 1d);
            case 6:
                return new AxisAlignedBB(0, 0d, 0.5d, 0.5d, 1.7d, 1d);
        }

        return new AxisAlignedBB(0,0,0,1,1,1);

    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return getBoxFromLocation(state.getValue(LOCATION));
    }
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        //int loc = state.getValue(LOCATION);

        TileEntity te = worldIn.getTileEntity(pos.down());
        if (te instanceof TurretSlaveTe) {
            TurretMasterTe master = ((TurretSlaveTe)te).getMaster();
            master.DropSentry();
        }
        
    }



    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        return;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{LOCATION});
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LOCATION);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(LOCATION, meta);
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
            EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
            
        TileEntity te = worldIn.getTileEntity(pos.down());
        if (te instanceof TurretSlaveTe) {
            TurretMasterTe master = ((TurretSlaveTe)te).getMaster();
            master.Interaction(playerIn);
        }
                
        return te instanceof TurretSlaveTe;
    }
    
}
