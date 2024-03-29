package com.anet.qtr4tdm.common.blocks;

import java.util.ArrayList;
import java.util.List;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.items.rocketItem;
import com.anet.qtr4tdm.common.supers.BlockDefenceSystem;
import com.anet.qtr4tdm.common.tiles.MiniSiloTile;
import com.anet.qtr4tdm.init.BlocksInit;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@Mod.EventBusSubscriber(modid = TdmMod.MODID)
public class MiniSiloBlock extends BlockDefenceSystem<MiniSiloTile> {

    public static final PropertyBool armed = PropertyBool.create("armed");

    public MiniSiloBlock() {
        super("minisilo", Material.TNT, 1, 1, SoundType.METAL);
        this.setCreativeTab(TdmMod.qtr4);
    }

    @Override
    public Class<MiniSiloTile> getTileEntityClass() {
        return MiniSiloTile.class;
    }

    @Override
    public MiniSiloTile createTileEntity(World world, IBlockState blockState) {
        return new MiniSiloTile();
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
        return state.withProperty(armed, meta == 1);
    }

    public static void SetState (World world, BlockPos pos, boolean state) {
        TileEntity tile = world.getTileEntity(pos);

        world.setBlockState(pos, BlocksInit.MINISILO.getDefaultState().withProperty(MiniSiloBlock.armed, state));

        if (tile != null) {
            tile.validate();
            world.setTileEntity(pos, tile);
        }
    }


    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(armed) ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {armed});
    }

    @SubscribeEvent
    public static void PlayerRoketoiTyk(PlayerInteractEvent event) {
        if (!event.getWorld().isRemote) {
            IBlockState state = event.getWorld().getBlockState(event.getPos());
            if (state.getBlock() instanceof MiniSiloBlock) {
                MiniSiloTile tile = (MiniSiloTile)event.getWorld().getTileEntity(event.getPos());

                if (event.getItemStack().getItem() instanceof rocketItem && !tile.armed && !event.getWorld().provider.isNether()) {
                    event.getItemStack().setCount(event.getItemStack().getCount() - 1);
                    tile.Arm();
                }
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity te = worldIn.getTileEntity(pos);
        ((MiniSiloTile)te).DisconnectFromBase();
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        items.add(new ItemStack(Item.getItemFromBlock(state.getBlock())));
        if (state.getValue(armed)) items.add(new ItemStack(BlocksInit.ROCKET));
        return items;
    }

    @Override
    public boolean hasTileEntity(IBlockState blockState) {
        return true;
    }

    @Override
    public boolean hasTileEntity() {
        // TODO Auto-generated method stub
        return true;
    }
}
