package com.anet.qtr4tdm.uebki;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class uebokblock extends Block {
    public uebokblock(String name, Material material, float hardness, float resistanse, SoundType soundType) {

        super(material);

        this.setRegistryName(name);
        this.setUnlocalizedName(name);
        this.setHardness(hardness);
        this.setResistance(resistanse);
        this.setSoundType(soundType);
    }
}
