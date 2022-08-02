package com.anet.qtr4tdm.common.supers;

import java.util.List;

import com.anet.qtr4tdm.uebki.BlockTileEntity;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderTippedArrow;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockDefenceSystem<T extends IDefenceSystem> extends BlockTileEntity {

    public BlockDefenceSystem(String name, Material material, float hardness, float resistanse, SoundType soundType) {
        super(name, material, hardness, resistanse, soundType);
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
        try {
            T te = (T)getTileEntityClass().newInstance();
            tooltip.add("§7Мощность: §r" + te.getPoints());
            if (te.getRange() != 0)
                tooltip.add("§7Дальность действия: §r" + (te).getRange());

            if (te instanceof TEDefenceEnrg) {
                if (((TEDefenceEnrg)te).getConsumptionPerTick() != 0)
                    tooltip.add("§7Потребление энергии: §r" + ((TEDefenceEnrg)te).getConsumptionPerTick() + "/t");
                    tooltip.add("§7Энергоуровень: §r" + ((TEDefenceEnrg)te).getSinkTier());
            }



        }
        catch (Exception e) {
            System.out.println(e);
        }
        super.addInformation(stack, player, tooltip, advanced);
    }
    
}
