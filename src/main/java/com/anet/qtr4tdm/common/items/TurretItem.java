package com.anet.qtr4tdm.common.items;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.init.BlocksInit;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class TurretItem extends Item implements IMetadataItem {

    public TurretItem () {
        this.setHasSubtypes(true);
        this.setRegistryName("turret_item");
        this.setMaxDamage(0);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack item) {
        int metadata = item.getItemDamage();
        return "turret_item_" + (metadata);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        super.getSubItems(tab, items);
        if (tab != TdmMod.qtr4) return;
        for (int type = 0; type < GetMetaCount(); type++) {
            items.add(new ItemStack(BlocksInit.TURRETITEM, 1, type));
        }
    }

    @Override
    public int GetMetaCount() {
        return 1;
    }
    
}
