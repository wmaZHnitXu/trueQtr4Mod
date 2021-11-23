package com.anet.qtr4tdm.uebki.gui.KAZGuiMisc;

import com.anet.qtr4tdm.common.blocks.Kaz1Block;
import com.anet.qtr4tdm.common.items.KAZAmmoItem;
import com.anet.qtr4tdm.common.tiles.Kaz1Tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class kazContainer extends Container {

    private Kaz1Tile te;

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    public kazContainer (InventoryPlayer player, Kaz1Tile tile) {
        te = tile;

        addSlotToContainer(new Slot(tile, 0, 83, 30));

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
            if (!(itemstack1.getItem() instanceof KAZAmmoItem)) return ItemStack.EMPTY;
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

    class KazSlot extends Slot {

        public KazSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }
        
        @Override
        public boolean isItemValid(ItemStack stack) {
            return stack != null && stack.getItem() instanceof KAZAmmoItem;
        }
    }
}
