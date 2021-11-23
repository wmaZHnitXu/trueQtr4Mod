package com.anet.qtr4tdm.common.tiles;

import java.util.ArrayList;
import java.util.List;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.blocks.Kaz1Block;
import com.anet.qtr4tdm.common.items.KAZAmmoItem;
import com.anet.qtr4tdm.common.supers.TileEntityDefence;
import com.anet.qtr4tdm.init.BlocksInit;
import com.anet.qtr4tdm.uebki.gui.KAZGuiMisc.kazContainer;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.MinecraftForge;

public class Kaz1Tile extends TileEntityDefence implements ITickable, ISidedInventory, IEnergySink {

    public boolean connected;
    public boolean powered;
    private int maxAmmo = 3;
    private int range = 10;
    public List<ItemStack> ammo;

    private int interval = 10;
    private double energy = 0;
    private double maxEnergy = 100;
    private int ticksexisted;
    public kazContainer container;

    //TEMPTEMP
    private int currentammo;

    @Override
    public void onLoad() {
        if (ammo == null) {
            ammo = new ArrayList<ItemStack>();
            ammo.add(0, ItemStack.EMPTY);
        }
        if (!world.isRemote) {
            markDirty();
        }
        MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
        currentammo = world.getBlockState(pos).getValue(Kaz1Block.AMMO);
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
    }

    @Override
    public void invalidate() {
        //MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        super.invalidate();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("ammocount", ammo.get(0).getCount());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        ammo = new ArrayList<ItemStack>(); 
        ammo.add(0, new ItemStack(BlocksInit.KAZAMMO, compound.getInteger("ammocount")));
        super.readFromNBT(compound);
    }

    @Override
    public void SetTargetsFromBase(ArrayList<Entity> targets) {

    }

    public void Interaction (EntityPlayer player) {
        if (!world.isRemote)
            player.openGui(TdmMod.instance, TdmMod.GUI_KAZ, world, pos.getX(), pos.getY(), pos.getZ());
    }

    public void Destruction () {
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        DisconnectFromBase();
        //Снаряд выбрасывается в getdropitems класса блока.
    }

    public int getAmmoCount () {
        return ammo.get(0).getCount();
    }

    @Override
    public void Refresh() {
        
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            if (ticksexisted % interval == 0) {
                if (energy > 0) {
                    energy = Math.max(energy-10, 0);
                }
            }
            powered = energy > 0;
            AmmoUpdate();
        }
        ticksexisted++;
    }

    private void AmmoUpdate () {
        int _ammo = ammo.get(0).getCount();
        if (_ammo != currentammo) {
            Kaz1Block.SetAmmo(world, pos, _ammo);
            currentammo = _ammo;
        }
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return ammo.get(0).isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return ammo.get(0);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(ammo, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(ammo, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.ammo.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }
        markDirty(); 
    }

    @Override
    public int getInventoryStackLimit() {
        return 3;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack != null && stack.getItem() instanceof KAZAmmoItem;
    }

    @Override
    public int getField(int id) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getFieldCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void clear() {
        ammo.set(0, ItemStack.EMPTY);
        markDirty();
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasCustomName() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (side != EnumFacing.UP) {
            return new int[]{0};
        }
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        if (direction != EnumFacing.UP && itemStackIn != null && itemStackIn.getItem() instanceof KAZAmmoItem && index == 0 && (ammo.get(0).getCount() < 3)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }

    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
        if (side != EnumFacing.UP) return true;
        return false;
    }

    @Override
    public double getDemandedEnergy() {
        return maxEnergy - energy;
    }

    @Override
    public int getSinkTier() {
        return 2;
    }

    @Override
    public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
        energy = Math.min(energy + amount, maxEnergy);
        //return Math.max(energy - maxEnergy, 0); V DOKAX SKAZALI, 4TO TAK LY$WE DLYA PROIZVODITELNOSTI
        return 0;
    } 
}
