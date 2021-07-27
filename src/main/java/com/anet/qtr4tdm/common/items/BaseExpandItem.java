package com.anet.qtr4tdm.common.items;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.init.BlocksInit;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class BaseExpandItem extends Item implements IMetadataItem {

    public BaseExpandItem () {
        this.setHasSubtypes(true);
        this.setRegistryName("base_expander");
        this.setMaxDamage(0);
        this.setCreativeTab(TdmMod.qtr4);
    }

    @Override
    public String getUnlocalizedName(ItemStack item) {
        int metadata = item.getItemDamage();
        return "base_expander_lvl" + metadata;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int level = 0; level < 3; level++) {
            items.add(new ItemStack(BlocksInit.BASEEXPANDER, 1, level));
        }
        super.getSubItems(tab, items);
    }

    @Override
    public int GetMetaCount() {
        return 4;
    }
}
