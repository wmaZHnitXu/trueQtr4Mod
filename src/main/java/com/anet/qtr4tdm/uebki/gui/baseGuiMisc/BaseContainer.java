package com.anet.qtr4tdm.uebki.gui.baseGuiMisc;

import com.anet.qtr4tdm.common.items.BaseExpandItem;
import com.anet.qtr4tdm.common.tiles.BaseTile;

import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class BaseContainer extends Container {

    public final BaseTile te;

    public BaseContainer (InventoryPlayer player, BaseTile baseTile) {
        te = baseTile;
        addSlotToContainer(new BaseUpgradeSlot(baseTile, 0, 135, -70 + 134));

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlotToContainer(new Slot(player, x + y * 9 + 9, 3 + x * 18, -70 + 239 + y * 18));
            }
        }

        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(player, x, 3 + x * 18, -70 + 297));
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
    class BaseUpgradeSlot extends Slot {

        public BaseUpgradeSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return stack.getItem() instanceof BaseExpandItem;
        }
    }
}
