package com.anet.qtr4tdm.common.tiles;

import java.util.ArrayList;
import java.util.List;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.blocks.TurretCollisionBlock;
import com.anet.qtr4tdm.common.blocks.TurretCollisionCapBlock;
import com.anet.qtr4tdm.common.entities.CannonTurretEntity;
import com.anet.qtr4tdm.common.entities.LaserTurretEntity;
import com.anet.qtr4tdm.common.entities.RailTurretEntity;
import com.anet.qtr4tdm.common.entities.TurretEntity;
import com.anet.qtr4tdm.common.supers.TEDefenceEnrg;
import com.anet.qtr4tdm.init.BlocksInit;
import com.anet.qtr4tdm.uebki.gui.TurretConatainer;
import com.jcraft.jorbis.Block;

import ic2.api.energy.tile.IEnergyEmitter;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

public class TurretMasterTe extends TEDefenceEnrg implements ISidedInventory {

    public NonNullList<ItemStack> items = NonNullList.<ItemStack>withSize(28, ItemStack.EMPTY);
    public int NetworkEnergy;
    public int NetworkMaxEnergy;
    public TurretConatainer conatainer;
    private boolean disassembling;
    private TurretEntity entity;

    @Override
    public void onLoad() {
        super.onLoad();
        TdmMod.logger.info("loaded");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        {
            ItemStackHelper.saveAllItems(compound, this.items);
        }

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.items = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

        {
            ItemStackHelper.loadAllItems(compound, this.items);
        }

    }

    public void Interaction (EntityPlayer player) {
        if (!world.isRemote) {
            player.openGui(TdmMod.instance, TdmMod.GUI_TURRETMASTER, world, pos.getX(), pos.getY(), pos.getZ());
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

    public void ConnectEntity (TurretEntity e) {
        entity = e;
        items.set(27, getItemStackFromSentry(e));

    }

    private void SpawnSentry (ItemStack stack) {
        TurretEntity entityToSpawn = getSentryForItemStack(stack);
        entityToSpawn.setPosition(pos.getX() + 0.5d, pos.getY() + 1, pos.getZ() + 0.5d);
        this.entity = entityToSpawn;
        world.spawnEntity(entityToSpawn);
        TdmMod.logger.info(entity == null ? "null" : "nonnull");

        ConstructCollider();
    }

    private void ConstructCollider () {

        int i = 0;

        for (int x = pos.getX() - 1; x <= pos.getX() + 1; x++ ) {
            for (int z = pos.getZ() - 1; z <= pos.getZ() + 1; z++) {
                IBlockState blkState = BlocksInit.TURRETCOLLIDER.getDefaultState().withProperty(TurretCollisionBlock.LOCATION, i);
                world.setBlockState(new BlockPos(x, pos.getY() + 1, z), blkState, 3);
                i++;
            } 
        }

        i = 0;

        for (int x = pos.getX() - 1; x <= pos.getX() + 1; x++ ) {
            for (int z = pos.getZ() - 1; z <= pos.getZ() + 1; z++) {
                IBlockState blkState = BlocksInit.TURRETCOLLIDERCAP.getDefaultState().withProperty(TurretCollisionCapBlock.LOCATION, i);
                world.setBlockState(new BlockPos(x, pos.getY() + 2, z), blkState, 3);
                i++;
            } 
        }
        
    }

    private void RemoveCollider () {

        IBlockState airstate = BlockAir.getStateById(0);

        for (int x = pos.getX() - 1; x <= pos.getX() + 1; x++ ) {
            for (int z = pos.getZ() - 1; z <= pos.getZ() + 1; z++) {
                world.setBlockState(new BlockPos(x, pos.getY() + 1, z), airstate, 3);
            } 
        }

        for (int x = pos.getX() - 1; x <= pos.getX() + 1; x++ ) {
            for (int z = pos.getZ() - 1; z <= pos.getZ() + 1; z++) {
                world.setBlockState(new BlockPos(x, pos.getY() + 2, z), airstate, 3);
            } 
        }

    }

    private TurretEntity getSentryForItemStack (ItemStack stack) {
        switch (stack.getItemDamage()) {
            case 0: return new LaserTurretEntity(world);
            case 1: return new CannonTurretEntity(world);
            case 2: return new RailTurretEntity(world);
            default: return null;
        }
    }

    private ItemStack getItemStackFromSentry (TurretEntity entity) {
        if (entity instanceof LaserTurretEntity) return new ItemStack(BlocksInit.TURRETITEM, 1, 0);
        if (entity instanceof RailTurretEntity) return new ItemStack(BlocksInit.TURRETITEM, 1, 2); //Да, именно так, просто railturret у нас так же является cannonturret
        if (entity instanceof CannonTurretEntity) return new ItemStack(BlocksInit.TURRETITEM, 1, 1);
        return ItemStack.EMPTY;
    }

    private void DespawnSentry () {
        if (entity != null) {
            entity.setDead();
            entity = null;
        }

        RemoveCollider();
    }

    public void DropSentry () {
        ItemStack sentryStack = items.get(27);
        items.set(27, ItemStack.EMPTY);
        InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), sentryStack);
        DespawnSentry();
    }

    public void Disassemble (BlockPos caused) {
        if (disassembling) return;
        disassembling = true;
        DespawnSentry();
        DisconnectFromBase();
        InventoryHelper.dropInventoryItems(world, pos.up(), this);
        IBlockState baseState = BlocksInit.TURRETBASE.getDefaultState();
        for (int x = pos.getX() - 1; x < pos.getX() + 2; x++) {
            for (int z = pos.getZ() - 1; z < pos.getZ() + 2; z++) {
                BlockPos changePos = new BlockPos(x, pos.getY(), z);
                if (!changePos.equals(caused)) {
                    world.setBlockState(changePos, baseState, 3);
                }
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return 28;
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

        ItemStack startTurret = items.get(27); //TODO ______________________________________________

        if (index == 27 && !world.isRemote) {
            if (this.items.get(27).equals(ItemStack.EMPTY)) {
                SpawnSentry(stack);
            }
            else {
                DespawnSentry();
            }
        }

        this.items.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        markDirty();
    }

    public boolean containsItemInside (Item item) {
        for (ItemStack stack : items) {
            if (stack.getItem().equals(item)) return true;
        }
        return false;
    }

    public ItemStack transferAmmoInTurret (Item item) {
        ItemStack result = ItemStack.EMPTY;
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (stack.getItem().equals(item)) {
                if (stack.getCount() == 1) {
                    result = stack;
                    items.set(i, ItemStack.EMPTY);
                }
                else {
                    stack.setCount(stack.getCount()-1);
                    result = stack.copy();
                    result.setCount(1);
                }
                break;
            }
        }
        return result;
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
    public double getEnergy() {
        if (entity != null) return entity.getEnergy();
        return 0;
    }

    @Override
    public double getMaxEnergy() {
        if (entity != null) return entity.getMaxEnergy();
        return 0;
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
        return 2;
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

    @Override
    public double getDemandedEnergy() {
        if (entity != null) {
            return entity.getDemandedEnergy();
        }
        else return 0;
    }

    @Override
    public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
        if (entity != null) {
            entity.injectEnergy(amount);
        }
        return 0;
    }
    
}
