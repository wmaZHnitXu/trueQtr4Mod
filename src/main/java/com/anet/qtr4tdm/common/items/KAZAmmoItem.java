package com.anet.qtr4tdm.common.items;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.init.BlocksInit;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class KAZAmmoItem extends Item implements IMetadataItem {

    public KAZAmmoItem () {
        this.setHasSubtypes(true);
        this.setRegistryName("kazammo");
        this.setMaxDamage(0);
        //this.setCreativeTab(TdmMod.qtr4);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack item) {
        int metadata = item.getItemDamage();
        return "kazammo" + (metadata + 1);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        super.getSubItems(tab, items);
        if (tab != TdmMod.qtr4) return;
        for (int level = 0; level < GetMetaCount(); level++) {
            items.add(new ItemStack(BlocksInit.KAZAMMO, 1, level));
        }
    }

    @Override
    public int GetMetaCount() {
        return 3;
    }
}

