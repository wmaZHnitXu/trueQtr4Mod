package com.anet.qtr4tdm.uebki.gui.KAZGuiMisc;

import com.anet.qtr4tdm.common.blocks.Kaz1Block;
import com.anet.qtr4tdm.common.items.KAZAmmoItem;
import com.anet.qtr4tdm.common.tiles.Kaz1Tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class kazContainer extends Container {

    private Kaz1Tile te;
    private boolean connected;
    private boolean powered;

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    public kazContainer (InventoryPlayer player, Kaz1Tile tile) {
        te = tile;

        addSlotToContainer(new KazSlot(tile, 0, 83, 30));

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
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (!(te.isItemValidForSlot(0, itemstack1))) return ItemStack.EMPTY;
            if (index < 1)
            {
                if (!this.mergeItemStack(itemstack1, 1, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, 1, false))
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
        listener.sendAllWindowProperties(this, te);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);

            if (this.connected != (this.te.getField(0) == 1))
            {
                icontainerlistener.sendWindowProperty(this, 0, this.te.getField(0));
            }

            if (this.powered != (this.te.getField(1) == 1))
            {
                icontainerlistener.sendWindowProperty(this, 1, this.te.getField(1));
            }
        }

        this.connected = this.te.getField(0) == 1;
        this.powered = this.te.getField(1) == 1;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        this.te.setField(id, data);
    }

    class KazSlot extends Slot {

        public KazSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }
        
        @Override
        public boolean isItemValid(ItemStack stack) {
            return te.isItemValidForSlot(0, stack);
        }
    }
}
