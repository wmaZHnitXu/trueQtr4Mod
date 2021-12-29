package com.anet.qtr4tdm.common.blocks;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.supers.BlockDefenceSystem;
import com.anet.qtr4tdm.common.tiles.Mrk1Tile;
import com.anet.qtr4tdm.uebki.BlockTileEntity;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Mrk1Block extends BlockDefenceSystem<Mrk1Tile> {

    public static PropertyBool FACING = PropertyBool.create("facing");
    public static PropertyBool ARMED = PropertyBool.create("armed");

    public Mrk1Block() {
        super("mrk1", Material.IRON, 5, 3, SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, false).withProperty(ARMED, false));
        this.setCreativeTab(TdmMod.qtr4);
    }

    @Override
    public Class getTileEntityClass() {
        return Mrk1Tile.class;
    }

    public static void SetArmed (World world, BlockPos pos, boolean arm) {
        TileEntity tile = world.getTileEntity(pos);

        IBlockState state = world.getBlockState(pos);
        boolean highlight = false;
        if (state.getBlock() instanceof Mrk1Block) {
            world.setBlockState(pos, state.withProperty(ARMED, arm));
        }

        if (tile != null) {
            tile.validate();
            world.setTileEntity(pos, tile);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
            EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof Mrk1Tile) { 
            ((Mrk1Tile)tile).Interaction(playerIn);
            return true;
        }
        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof Mrk1Tile) {
            ((Mrk1Tile)tile).Destruction();
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public Mrk1Tile createTileEntity(World world, IBlockState blockState) {
        return new Mrk1Tile();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING, ARMED});
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, meta % 2 == 0).withProperty(ARMED, (meta - meta % 2) == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        if (state.getValue(FACING)) meta += 1;
        if (state.getValue(ARMED)) meta += 2;
        return meta;
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
                return getDefaultState().withProperty(FACING, facing.getHorizontalIndex() % 2 == 0);
    }
}
