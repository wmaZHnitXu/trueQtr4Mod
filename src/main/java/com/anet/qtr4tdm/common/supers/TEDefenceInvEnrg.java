package com.anet.qtr4tdm.common.supers;

import java.util.ArrayList;
import java.util.List;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.bases.InWorldBasesManager;
import com.anet.qtr4tdm.uebki.gui.TEDefInvEnrgGuiMisc.TEDefContainer;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;

public abstract class TEDefenceInvEnrg extends TEDefenceEnrg implements ISidedInventory, IHasAmmo {
    
    public TEDefContainer container;
    protected List<ItemStack> ammo;
    protected int maxAmmo;

    public abstract Item GetAmmoType ();

    protected abstract void doAmmoUpdate ();

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        TdmMod.logger.info(stack.getItem().equals(GetAmmoType()) ? "true" : "false");
        return stack.getItem().equals(GetAmmoType());
    }

    @Override
    public void onLoad() { //Идёт после инициализации характеристик

        if (ammo == null) {
            ammo = new ArrayList<ItemStack>();
            ammo.add(0, ItemStack.EMPTY);
        }

        if (!world.isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            DisconnectFromBase();
            InWorldBasesManager.GetBaseConnection(this);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("ammocount", ammo != null && ammo.get(0) != null ? ammo.get(0).getCount() : 0);
        compound.setInteger("ammometa", ammo != null && ammo.get(0) != null ? ammo.get(0).getItemDamage() : 0);
        compound.setInteger("compoundid", ammo != null && ammo.get(0) != null ? Item.getIdFromItem(ammo.get(0).getItem()) : 0);
        return super.writeToNBT(compound);
    }

    public void Interaction (EntityPlayer player) {
        if (!world.isRemote) {
            player.openGui(TdmMod.instance, TdmMod.GUI_COMMONDEF, world, pos.getX(), pos.getY(), pos.getZ());
        }
    }

    public void Destruction () {
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        DisconnectFromBase();
        InventoryHelper.dropInventoryItems(world, pos, this);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        ammo = new ArrayList<ItemStack>();
        ItemStack stack = new ItemStack(GetAmmoType(), compound.getInteger("ammocount"));
        stack.setItemDamage(compound.getInteger("ammometa"));
        if (compound.getInteger("compoundid") == Item.getIdFromItem(GetAmmoType())) ammo.add(0, stack); 
        else ammo.add(0, ItemStack.EMPTY);
        super.readFromNBT(compound);
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
        doAmmoUpdate();
    }

    

    @Override
    public int getInventoryStackLimit() {
        return maxAmmo;
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
    public int getField(int id) {
        switch (id) {
            case 0: return connected ? 1 : 0;
            case 1: return powered ? 1 : 0;
        }
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0: connected = value==1; break;
            case 1: powered = value==1; break;
        }
    }

    @Override
    public int getFieldCount() {
        return 2;
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
        if (direction != EnumFacing.UP && itemStackIn != null && isItemValidForSlot(0, itemStackIn) && index == 0 && (ammo.get(0).getCount() < maxAmmo)) {
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

    @Override
    public int getAmmo() {
        int ammocount = 0;
        if (ammo != null) {
            for (ItemStack s : ammo) {
                if (s != null && !s.isEmpty()) {
                    ammocount += s.getCount();
                }
            }
        }
        return ammocount;   
    }

    @Override
    public int getMaxAmmo() {
        return getSizeInventory() * getInventoryStackLimit();
    }
}
