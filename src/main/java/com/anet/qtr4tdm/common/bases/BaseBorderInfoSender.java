package com.anet.qtr4tdm.common.bases;

import java.util.List;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.uebki.messages.primitive.BaseBorderMessage;

import net.minecraft.entity.player.EntityPlayerMP;

public class BaseBorderInfoSender {

    public static BaseBorderInfoSender instance;

    public BaseBorderInfoSender () {
        instance = this;
    }
    
    public static void send () {
        if (instance == null) return;
        instance.upt();
    }

    private void upt () {

        List<EntityPlayerMP> players = TdmMod.currentServer.getPlayerList().getPlayers();
        for (EntityPlayerMP player : players) {
            for (baseInfo base : InWorldBasesManager.instance.bases) {
                if (base.isValidBase() && base.pos.getDistance(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()) < 300) {
                    TdmMod.wrapper.sendTo(new BaseBorderMessage(base), player);
                }
            }
        }
    }

}
