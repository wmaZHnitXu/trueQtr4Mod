package com.anet.qtr4tdm;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TdmMod.MODID)
public class WorldEventsCatcher {
    @SubscribeEvent
    public static void BlockDestroyEvt (BlockEvent.BreakEvent evt) {
        if (!evt.getWorld().isRemote) {
            PrivatesHandler.OnPlayerBreakBlock(evt);
        }
        else {

        }
    }

    public static void BlockPlacedEvt (BlockEvent.EntityPlaceEvent evt) {
        if (!evt.getWorld().isRemote) {
            if (evt.getEntity() instanceof EntityPlayer) {
                PrivatesHandler.OnPlayerPlaceBlock(evt);
            }
        }
        else {
            
        }
    }
}
