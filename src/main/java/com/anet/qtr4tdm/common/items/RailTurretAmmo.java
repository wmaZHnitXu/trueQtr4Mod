package com.anet.qtr4tdm.common.items;

import com.anet.qtr4tdm.TdmMod;

import net.minecraft.item.Item;

public class RailTurretAmmo extends Item {

    public RailTurretAmmo () {
        this.setRegistryName("railturretammo");
        this.setUnlocalizedName("railturretammo");
        this.setMaxStackSize(1);
        this.setCreativeTab(TdmMod.qtr4);
    }
}
