package com.anet.qtr4tdm.common.tiles;

import java.util.ArrayList;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.supers.TEDefenceEnrg;

import ic2.api.energy.tile.IEnergyEmitter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TurretMasterTe extends TEDefenceEnrg implements ISidedInventory {

    public ArrayList<ItemStack> items;
    public int NetworkEnergy;
    public int NetworkMaxEnergy;

    @Override
    public void onLoad() {
        super.onLoad();
        items = new ArrayList<ItemStack>();
        for (int i = 0; i < 28; i++) {
            items.add(ItemStack.EMPTY);
        }
    }

    public void Interaction (EntityPlayer player) {
        if (!world.isRemote) {
            player.openGui(TdmMod.instance, TdmMod.GUI_COMMONDEF, world, pos.getX(), pos.getY(), pos.getZ());
        }
    }

    @Override
    public int getSinkTier() {
        return 3;
    }

    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
        return side == EnumFacing.DOWN;
    }

    @Override
    public int getConsumptionPerTick() {
        return 0;
    }

    @Override
    public void SetTargetsFromBase(ArrayList<Entity> targets) {
        
    }

    @Override
    public int getPoints() {
        return 0;
    }

    @Override
    public int getRange() {
        return 0;
    }

    @Override
    public void Refresh() {
        
    }

    private void SpawnSentry () {

    }

    private Entity getSentryForItemStack () {
        return null; //TODO ______________________________________________________
    }

    private void DespawnSentry () {
        //TODO ______________________________________________________
    }

    public void Destruction (BlockPos cause) {
        DespawnSentry();
        InventoryHelper.dropInventoryItems(world, pos.up(), this);
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(items, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(items, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.items.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }
        markDirty();
    }

    

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0: return Double.valueOf(getEnergy()).intValue();
            case 1: return Double.valueOf(getMaxEnergy()).intValue();
            default: return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                NetworkEnergy = value;
            break;
            case 1:
                NetworkMaxEnergy = value;
            break;
        }
        
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (ItemStack item : items) {
            item = ItemStack.EMPTY;
        }
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return null;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }
    
}
