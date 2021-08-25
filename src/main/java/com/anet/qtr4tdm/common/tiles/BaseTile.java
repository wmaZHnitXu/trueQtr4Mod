package com.anet.qtr4tdm.common.tiles;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.bases.InWorldBasesManager;
import com.anet.qtr4tdm.common.bases.baseInfo;
import com.anet.qtr4tdm.common.blocks.BaseBlock;
import com.anet.qtr4tdm.uebki.IDSmanager;
import com.anet.qtr4tdm.uebki.gui.BaseGui;
import com.anet.qtr4tdm.uebki.gui.BaseSetupGui;
import com.typesafe.config.ConfigException.Null;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseTile extends TileEntity implements IInventory, ITickable {
    private int baseId;
    private baseInfo directInfo;
    private BaseState state;
    private ItemStack item;

    enum BaseState {
        inactive,
        active,
        error
    }

    @Override
    public void onLoad() {
        if (!world.isRemote) {
            InsertDirectInfo(InWorldBasesManager.GetInfo(pos));
        }
        else {
            state = BaseState.values()[world.getBlockState(pos).getValue(BaseBlock.status)];
        }
        item = ItemStack.EMPTY;
        super.onLoad();
    }

    public void Interaction (EntityPlayer player) {
        if (player.world.isRemote) {
            GuiOpen(world, player, pos);
        }
        else if (state == BaseState.active) {
            if (directInfo.isMember(IDSmanager.GetPlayerId(player)))
                player.openGui(TdmMod.instance, TdmMod.GUI_BASE, world, pos.getX(), pos.getY(), pos.getZ());
            else player.sendMessage(new TextComponentString("Доступ запрещён."));
        }
    }

    public int GetId () {
        return directInfo.id;
    }

    @SideOnly(Side.CLIENT)
    public void GuiOpen (World worldIn, EntityPlayer player, BlockPos pos) {
        switch (state) {
            case inactive:
                TdmMod.logger.info("base setup");
                Minecraft.getMinecraft().displayGuiScreen(new BaseSetupGui(this));
            break;
            case active:
            break;
            case error:
            break;
        }
        
    }

    public void InsertDirectInfo (baseInfo info) {
        directInfo = info;
        if (directInfo != null) {
            TdmMod.logger.info("base with id " + directInfo.id + " loaded, pos:" + directInfo.pos.toString());
            state = BaseState.active;
            BaseBlock.SetState(world, pos, 1);
        }
        else {
            BaseBlock.SetState(world, pos, 0);
        }
    }

    public baseInfo GetDirectInfo () {
        return directInfo;
    }

    public void BaseBlockDestroy () {
        InWorldBasesManager.RemoveBase(pos);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        baseId = compound.getInteger("id");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("id", baseId);
        return super.writeToNBT(compound);
    }

    public void InsertClientData (int baseId, boolean active) {
        this.baseId = baseId;
        this.state = BaseState.values()[world.getBlockState(pos).getValue(BaseBlock.status)];
    }

    @Override
    public String getName() {
        return "Base";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return item == ItemStack.EMPTY;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return item;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack bs = item;
        if (!item.isEmpty() && item.getCount() - count > 0) item.setCount(item.getCount() - count);
        else item = ItemStack.EMPTY;
        return bs;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack RemovedSrack = item;
        item = ItemStack.EMPTY;
        return RemovedSrack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        item = stack;
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
        // TODO Auto-generated method stub
        
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
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
        item = ItemStack.EMPTY;
        
    }

    @Override
    public void update() {
        if (!world.isRemote && directInfo != null) directInfo.Tick();  
    }
}
