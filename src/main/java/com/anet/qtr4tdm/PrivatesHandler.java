package com.anet.qtr4tdm;

import com.anet.qtr4tdm.common.bases.InWorldBasesManager;
import com.anet.qtr4tdm.common.bases.baseInfo;
import com.anet.qtr4tdm.uebki.IDSmanager;
import com.anet.qtr4tdm.uebki.messages.primitive.TopBarMessage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.world.BlockEvent;

public class PrivatesHandler {
    public static PrivatesHandler instance;

    public static void OnPlayerBreakBlock (BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (player != null) {
            if (!isPlayerAllowedToEditHere(player, event.getPos())) {
                event.setCanceled(true);
                TdmMod.wrapper.sendTo(new TopBarMessage("Вы не можете ломать блоки здесь.", "Территория запривачена.", 1, 300), (EntityPlayerMP)player);
                //player.sendMessage(new TextComponentString("§4Вы не можете ломать блоки §lздесь."));
            }
        }
    }

    public static void OnPlayerPlaceBlock (BlockEvent.EntityPlaceEvent event) {
        EntityPlayer player = (EntityPlayer)event.getEntity();
        if (player != null) {
            if (!isPlayerAllowedToEditHere(player, event.getPos())) {
                event.setCanceled(true);
                TdmMod.wrapper.sendTo(new TopBarMessage("Вы не можете ставить блоки здесь.", "Территория запривачена.", 1, 300), (EntityPlayerMP)player);
            }
        }
    }

    public static boolean isPlayerAllowedToEditHere (EntityPlayer player, BlockPos pos) {
        boolean result = true;
        if (player.isCreative()) return true;
        baseInfo base = InWorldBasesManager.GetBaseOfTerritory(pos);
        if (base != null) {
            result = base.isMember(IDSmanager.GetPlayerId(player));
        }
        return result;
    }

    public static void OnPlayerEntersChunk (EntityPlayer player, ChunkPos oldPos, ChunkPos newPos) {
        baseInfo oldBase = InWorldBasesManager.GetBaseOfTerritory(oldPos);
        baseInfo newBase = InWorldBasesManager.GetBaseOfTerritory(newPos);
        
        if (newBase != null && newBase != oldBase) {
            boolean newIsMember = newBase.isMember(IDSmanager.GetPlayerId(player));
            TdmMod.wrapper.sendTo(new TopBarMessage("Это база.", newIsMember ? "(Ваша)" : "(Не ваша)", 2, 400), (EntityPlayerMP)player);
        }
        else {
            if (oldBase != null && newBase == null) {
                TdmMod.wrapper.sendTo(new TopBarMessage("Это больше не база.", "", 2, 400), (EntityPlayerMP)player);
            }
        }
    }
}
