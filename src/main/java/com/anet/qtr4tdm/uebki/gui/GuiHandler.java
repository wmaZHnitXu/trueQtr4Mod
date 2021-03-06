package com.anet.qtr4tdm.uebki.gui;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.tiles.BaseTile;
import com.anet.qtr4tdm.uebki.gui.BaseGui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case TdmMod.GUI_BASE:
                return new BaseContainer(player.inventory, (BaseTile)world.getTileEntity(new BlockPos(x,y,z)));
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case TdmMod.GUI_BASE:
                return new BaseGui(player.inventory, (BaseTile)world.getTileEntity(new BlockPos(x,y,z)));
        }
        return null;
    }
    
}
