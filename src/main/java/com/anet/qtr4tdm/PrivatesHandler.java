package com.anet.qtr4tdm;

import com.anet.qtr4tdm.common.bases.InWorldBasesManager;
import com.anet.qtr4tdm.common.bases.baseInfo;
import com.anet.qtr4tdm.uebki.IDSmanager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.world.BlockEvent;

public class PrivatesHandler {
    public static PrivatesHandler instance;

    public static void OnPlayerBreakBlock (BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (player != null) {
            if (!isPlayerAllowedToEditHere(player, event.getPos())) {
                event.setCanceled(true);
                player.sendMessage(new TextComponentString("§4Вы не можете ломать блоки §lздесь."));
            }
        }
    }

    public static void OnPlayerPlaceBlock (BlockEvent.EntityPlaceEvent event) {
        EntityPlayer player = (EntityPlayer)event.getEntity();
        if (player != null) {
            if (!isPlayerAllowedToEditHere(player, event.getPos())) {
                event.setCanceled(true);
                player.sendMessage(new TextComponentString("§4Вы не можете ставить блоки §lздесь."));
            }
        }
    }

    public static boolean isPlayerAllowedToEditHere (EntityPlayer player, BlockPos pos) {
        boolean result = true;
        baseInfo base = InWorldBasesManager.GetBaseOfTerritory(pos);
        if (base != null) {
            result = base.isMember(IDSmanager.GetPlayerId(player));
        }
        return result;
    }
}
