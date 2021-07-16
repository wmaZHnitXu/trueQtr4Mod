package com.anet.qtr4tdm.common.tiles;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.bases.InWorldBasesManager;
import com.anet.qtr4tdm.common.bases.baseInfo;
import com.typesafe.config.ConfigException.Null;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

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
        if (directInfo == null) {
            directInfo = InWorldBasesManager.AddNormalBase(pos, player, world.provider.getDimension());
            TdmMod.logger.info("base setup");
        }
    }

    public void InsertDirectInfo (baseInfo info) {
        directInfo = info;
        if (directInfo != null) TdmMod.logger.info("base with id " + directInfo.id + " loaded, pos:" + directInfo.pos.toString());
    }

    public void BaseBlockDestroy () {
        InWorldBasesManager.RemoveBase(pos);
    }
}
