package com.anet.qtr4tdm.uebki;

import com.anet.qtr4tdm.TdmMod;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TdmMod.MODID)

public class Debugger {
    @SubscribeEvent
    public static void PlayerDoesSmth (PlayerInteractEvent evt) {
        Chunk c = evt.getWorld().getChunkFromBlockCoords(evt.getPos());
        String answer = "x:" + c.x + " z:" + c.z; 
        evt.getEntityPlayer().sendMessage(new TextComponentString(answer));
    }
}
