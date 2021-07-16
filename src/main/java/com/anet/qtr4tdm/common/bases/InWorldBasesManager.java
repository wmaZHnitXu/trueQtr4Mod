package com.anet.qtr4tdm.common.bases;

import java.util.ArrayList;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.savedata.WorldBasesSavedData;
import com.anet.qtr4tdm.common.tiles.BaseTile;
import com.anet.qtr4tdm.uebki.IDSmanager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import scala.reflect.internal.Trees.Return;

public class InWorldBasesManager {
    ArrayList<baseInfo> bases;
    private static ArrayList<BlockPos> delayed;
    public static InWorldBasesManager instance;
    public ArrayList<Integer> freeIds;

    public InWorldBasesManager() {
        instance = this;
        WorldBasesSavedData data = WorldBasesSavedData.get((World)TdmMod.currentServer.getWorld(0));
        bases = data.bases;
        freeIds = data.freeIds;
        if (bases == null) bases = new ArrayList<baseInfo>();
        TdmMod.logger.info(bases.size());
        //Resolve delayed
        if (delayed != null) {
            System.out.println("resolvedel");
            for (BlockPos pos : delayed) {
                System.out.println("resolvedelb");
                BaseTile baseTile = (BaseTile)(TdmMod.currentServer.getWorld(0).getTileEntity(pos));
                for (baseInfo base : bases) {
                    System.out.println(base.pos.toString() + " == " + pos.toString() + " : " + base.pos.equals(pos));
                    if (base.pos.equals(pos)) baseTile.InsertDirectInfo(base);
                }
            }
        }
    }

    private void SaveData () {
        WorldBasesSavedData.get((World)TdmMod.currentServer.getWorld(0)).SetData(bases);
    }

    public static baseInfo AddNormalBase (BlockPos pos, EntityPlayer owner, int dimension) {
        int level = 1;
        int OwnerId = IDSmanager.instance.GetPlayerId(owner);
        int id; if (instance.freeIds.size() == 0) id = instance.bases.size(); else {id = instance.freeIds.get(0); instance.freeIds.remove(0); }
        ChunkPos[] chunks = new ChunkPos[1];
        chunks[0] = owner.world.getChunkFromBlockCoords(pos).getPos();
        baseInfo base = new baseInfo(pos, OwnerId, id, level, chunks, dimension);
        instance.bases.add(base);
        instance.SaveData();
        return base;
    }

    public static void RemoveBase (BlockPos pos) {
        for (int i = 0; i < instance.bases.size(); i++) {
            baseInfo base = instance.bases.get(i);
            if (base == null || base.pos.equals(pos)) {
                instance.freeIds.add(base.id);
                instance.bases.get(i).pos = new BlockPos(0, 0, 0);
                i--;
            }
        }
        instance.SaveData();
    }

    public static baseInfo GetInfo (BlockPos pos) { //Лучше заменить на tile
        if (instance == null) { AddToDelayedInit(pos); System.out.println("delay"); return null;}
        for (baseInfo base : instance.bases) {
            if (base.pos.equals(pos)) return base;
        }
        System.out.println("nomatch");
        return null;
    }

    public static void AddToDelayedInit(BlockPos pos) {
        if (delayed == null) delayed = new ArrayList<BlockPos>();
        delayed.add(pos);
    }

    public static boolean DeleteAllBases () {
        if (instance == null || instance.bases == null) return false;
        instance.bases.clear();
        instance.SaveData();
        return true;
    }

    public static boolean IsPositionClaimed (BlockPos pos) {
        for (baseInfo base : instance.bases) {
            if (base.ContainsChunk(ChunkPosFromBlockPos(pos))) return true;
        }
        return false;
    }

    public static ChunkPos ChunkPosFromBlockPos (BlockPos pos) {
        int x = Math.floorDiv(pos.getX(), 16);
        int z = Math.floorDiv(pos.getZ(), 16);
        return new ChunkPos(x,z);
    }
}
