package com.anet.qtr4tdm.common.tiles;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.bases.InWorldBasesManager;
import com.anet.qtr4tdm.common.bases.baseInfo;
import com.anet.qtr4tdm.uebki.gui.BaseSetupGui;
import com.typesafe.config.ConfigException.Null;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseTile extends TileEntity {
    private boolean active;
    private int baseId;
    private baseInfo directInfo;

    @Override
    public void onLoad() {
        if (!world.isRemote) {
            directInfo = InWorldBasesManager.GetInfo(pos);
            if (directInfo != null) TdmMod.logger.info("base with id " + directInfo.id + " loaded, pos:" + directInfo.pos.toString());
        }
        super.onLoad();
    }

    public void Interaction (EntityPlayer player) {
        if (!active) {
            SetupGuiOpen(world, pos);
            TdmMod.logger.info("base setup");
        }
    }

    @SideOnly(Side.CLIENT)
    public void SetupGuiOpen (World worldIn, BlockPos pos) {
        Minecraft.getMinecraft().displayGuiScreen(new BaseSetupGui(this));
    }

    public void InsertDirectInfo (baseInfo info) {
        directInfo = info;
        if (directInfo != null) TdmMod.logger.info("base with id " + directInfo.id + " loaded, pos:" + directInfo.pos.toString());
    }

    public void BaseBlockDestroy () {
        InWorldBasesManager.RemoveBase(pos);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        baseId = compound.getInteger("id");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("id", baseId);
        return super.writeToNBT(compound);
    }

    public void InsertClientData (int baseId, boolean active) {
        this.baseId = baseId;
        this.active = active;
    }
}
