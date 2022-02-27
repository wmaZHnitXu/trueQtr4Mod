package com.anet.qtr4tdm.uebki.gui;

import com.anet.qtr4tdm.common.items.TurretItem;
import com.anet.qtr4tdm.common.supers.DroneSmallEntity;
import com.anet.qtr4tdm.common.tiles.DroneBaseTile;
import com.anet.qtr4tdm.common.tiles.TurretMasterTe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TurretConatainer extends Container {

    public TurretMasterTe tile;
    private int numRows;
    private EntityPlayer player;
    private double charge;
    private double maxCharge;

    public TurretConatainer (InventoryPlayer player, TurretMasterTe tile) {

        numRows = 3;

        //storage
        
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 9; x++) {
                    this.addSlotToContainer(new Slot(tile, x + y * 9, 8 + x * 18, 8 + y * 18));
                }
            }

        //tile
        this.tile = tile;
        this.addSlotToContainer(new SlotForTurret(tile, 27, 8, 64));


        //player
        this.player = player.player;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlotToContainer(new Slot(player, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(player, x, 8 + x * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
 
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
 
            if (index < this.numRows * 9)
            {
                if (!this.mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.numRows * 9, false) && index != 27)
            {
                return ItemStack.EMPTY;
            }
 
            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }
 
        return itemstack;
	}

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, tile);
    }

    
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);

            if (this.charge != this.tile.getField(0))
            {
                icontainerlistener.sendWindowProperty(this, 0, this.tile.getField(0));
            }

            if (this.maxCharge != this.tile.getField(1))
            {
                icontainerlistener.sendWindowProperty(this, 1, this.tile.getField(1));
            }
        }

        this.charge = this.tile.getField(0);
        this.maxCharge = this.tile.getField(1);
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) //Если возьмешь за основу, сохраняй название этого метода.
    {
        this.tile.setField(id, data);
    }

    public class SlotForTurret extends Slot {

        public SlotForTurret(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return stack.getItem() instanceof TurretItem;
        }
    }
}
