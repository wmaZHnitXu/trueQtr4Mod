package com.anet.qtr4tdm;

import com.anet.qtr4tdm.common.bases.InWorldBasesManager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
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
    @SubscribeEvent
    public static void BlockPlacedEvt (BlockEvent.EntityPlaceEvent evt) {

        if (!evt.getWorld().isRemote) {
            if (evt.getEntity() instanceof EntityPlayer) {
                PrivatesHandler.OnPlayerPlaceBlock(evt);
            }
        }
        else {

        }
    }

    @SubscribeEvent
    public static void EntityEnterChunkEvt (EntityEvent.EnteringChunk evt) {

        //Player enter chunk serverside
        if (evt.getEntity() instanceof EntityPlayer && !evt.getEntity().world.isRemote) {
            PrivatesHandler.OnPlayerEntersChunk((EntityPlayer)evt.getEntity(), new ChunkPos(evt.getOldChunkX(), evt.getOldChunkZ()), new ChunkPos(evt.getNewChunkX(), evt.getNewChunkZ()));
        }
    }

    @SubscribeEvent
    public static void EntityLivingDeath (LivingDeathEvent evt) {

        //Player death serverside
        if (evt.getEntity() instanceof EntityPlayer && !evt.getEntity().world.isRemote) {
            InWorldBasesManager.PlayerDeadSubmit(evt);
        }
    }
}
