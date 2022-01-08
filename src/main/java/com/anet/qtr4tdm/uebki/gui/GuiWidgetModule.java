package com.anet.qtr4tdm.uebki.gui;

import java.util.ArrayList;

import com.anet.qtr4tdm.common.supers.IDefenceSystem;
import com.anet.qtr4tdm.common.supers.IHasAmmo;
import com.anet.qtr4tdm.common.supers.IHasEnergy;
import com.anet.qtr4tdm.common.supers.IRadar;
import com.anet.qtr4tdm.common.supers.TileEntityDefence;
import com.anet.qtr4tdm.uebki.messages.primitive.DefenceDataToPlayer.DefDataStruct;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class GuiWidgetModule extends GuiWidget implements IScrollest, IChildHasScrolls {

    private String moduleName = "Модуль не выбран.";
    private int range;
    private int ammo;
    private BlockPos pos;
    private int maxAmmo;
    private int charged = -2;
    private ItemStack moduleStack;
    private Scroll modulesScroll;
    private ArrayList<DefDataStruct> structList;
    BaseGui base;

    public GuiWidgetModule(boolean rightSide, int x, int y, int dwidth, int dheight, Minecraft mc,
            ResourceLocation icon, BaseGui base) {
        super(rightSide, x, y, dwidth, dheight, mc, icon);
        this.base = base;
        name = "Модули";
        modulesScroll = new Scroll(xPos + 115, yPos, 85, new String[0], this, mc, true, dheight);
        modulesScroll.enabled = true;
    }

    public void insertModule (DefDataStruct struct) {
        if (struct == null) {

            moduleName = "Модуль не выбран.";
            ammo = 0;
            range = 0;
            pos = null;
            maxAmmo = 0;
            charged = -2;
            moduleStack = null;
            base = null;

            return;
        }
        this.pos = struct.pos;
        IBlockState state = Minecraft.getMinecraft().world.getBlockState(pos);
        moduleName = state.getBlock().getLocalizedName();
        moduleStack = new ItemStack(state.getBlock());
        TileEntity te = Minecraft.getMinecraft().world.getTileEntity(pos);
        if (te != null) {
            if (te instanceof IHasAmmo) { ammo = struct.ammoCount; maxAmmo =  ((IHasAmmo)te).getMaxAmmo();}
            if (te instanceof IHasEnergy) { charged = struct.chargeStatus; }
            if (te instanceof IDefenceSystem) { range = ((IDefenceSystem)te).getRange(); }
        }
    }

    public void InsertModules (ArrayList<DefDataStruct> structList) {
        this.structList = structList;
        String[] scrollSections = new String[structList.size()];
        for (int i = 0; i < scrollSections.length; i++) {
            scrollSections[i] = structList.get(i).getName();
        }
        modulesScroll.sections = scrollSections;
    }

    @Override
    public void MouseClick(int x, int y) {
        modulesScroll.Click(x, y);
        super.MouseClick(x, y);
    }

    @Override
    public void drawContent (int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        modulesScroll.draw(mouseX, mouseY);
        int fieldcount = 0;
        if (moduleName != null && !moduleName.equals("")) {
            drawString(mc.fontRenderer, moduleName, xPos + 5, yPos + 18, 0xFFFFFFFF); fieldcount++;
        }
        if (moduleStack != null && moduleStack != ItemStack.EMPTY) {
            int itemScale = 3;
            GlStateManager.pushMatrix();
                GlStateManager.color(1, 1, 1);
                GlStateManager.scale(itemScale, itemScale, itemScale);
                mc.getRenderItem().renderItemAndEffectIntoGUI(moduleStack, (xPos + 5) / itemScale, (yPos + 18 + fieldcount * 15) / itemScale);
            GlStateManager.popMatrix();
            fieldcount += itemScale + 1;
        }
        if (pos != null) {
            drawString(mc.fontRenderer, 
            "Позиция: x:" + pos.getX()
            + " y:" + pos.getY() 
            + " z:" + pos.getZ(), xPos + 5, yPos + 18 + fieldcount * 15, 0xFFFFFFFF); fieldcount++;
        }
        if (range != 0) {
            drawString(mc.fontRenderer, "Дальность: " + range, xPos + 5, yPos + 18 + fieldcount * 15, 0xFFFFFFFF); fieldcount++;
        }
        if (maxAmmo != 0) {
            drawString(mc.fontRenderer, "Боеприпасы: " + ammo + "/" + maxAmmo, xPos + 5, yPos + 18 + fieldcount * 15, 0xFFFFFFFF); fieldcount++;
        }
        if (charged != -2) {
            drawString(mc.fontRenderer, "Питание: " + (charged != 0 ? "Запитан" : "Обесточен"), xPos + 5, yPos + 18 + fieldcount * 15, 0xFFFFFFFF); fieldcount++;
        }
        GlStateManager.popMatrix();
    }

    @Override
    public void SetPosition(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;       
    }

    @Override
    public void ScrollClick(int id) {
        DefDataStruct selectedModule = structList.get(id);
        base.SetSelectedDef(selectedModule);
        insertModule(selectedModule);
    }

    @Override
    public void mouseWheelMove(int mouseX, int mouseY, int wheelDelta) {
        modulesScroll.handleMouseWheel(mouseX, mouseY, wheelDelta);
    }
    
}
