package com.anet.qtr4tdm.common.items;

import com.anet.qtr4tdm.TdmMod;

import net.minecraft.item.Item;

public class HarvesterDroneItem extends Item {

    public HarvesterDroneItem () {
        this.setRegistryName("harvesterdrone1");
        this.setUnlocalizedName("harvesterdrone1");
        this.setMaxStackSize(1);
        this.setCreativeTab(TdmMod.qtr4);
    }
}
