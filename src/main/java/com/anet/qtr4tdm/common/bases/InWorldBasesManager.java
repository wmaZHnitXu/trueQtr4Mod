package com.anet.qtr4tdm.common.bases;

import java.util.ArrayList;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.savedata.WorldBasesSavedData;
import com.anet.qtr4tdm.common.tiles.BaseTile;
import com.anet.qtr4tdm.uebki.IDSmanager;
import com.anet.qtr4tdm.uebki.gui.GuiHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

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
        int OwnerId = IDSmanager.GetPlayerId(owner);
        int[] members = new int[1];
        members[0] = OwnerId;
        int id = GetNewBaseId();
        ChunkPos[] chunks = new ChunkPos[1];
        chunks[0] = owner.world.getChunkFromBlockCoords(pos).getPos();
        baseInfo base = new baseInfo(pos, OwnerId, id, level, chunks, dimension, owner.getName() + "'s base", members);
        if (instance.freeIds.contains(id)) { 
            instance.freeIds.remove(0);
            for (int i = 0; i < instance.bases.size(); i++) {
                if (instance.bases.get(i).id == id) {instance.bases.remove(i); i--;}
            } 
        }
        instance.bases.add(base);
        instance.SaveData();
        return base;
    }

    public static int GetNewBaseId () {
        int id; if (instance.freeIds != null && instance.freeIds.size() > 0) id = instance.freeIds.get(0); else {id = instance.bases.size();}
        return id;
    }

    public static void RemoveBase (BlockPos pos) {
        for (int i = 0; i < instance.bases.size(); i++) {
            baseInfo base = instance.bases.get(i);
            if (base == null || base.pos.equals(pos)) {
                instance.freeIds.add(base.id);
                instance.bases.get(i).pos = new BlockPos(0, 0, 0);
                instance.bases.get(i).chunks = new ChunkPos[0];
                i--;
            }
        }
        instance.SaveData();
    }

    public static baseInfo GetInfo (BlockPos pos) { //Лучше заменить на tile P.S. ИСПОЛЬЗУЕТСЯ ДЛЯ ИНИЦИАЛИЗАЦИИ ТАЙЛА
        if (instance == null) { AddToDelayedInit(pos); System.out.println("delay"); return null;}
        for (baseInfo base : instance.bases) {
            if (base.pos.equals(pos)) return base;
        }
        System.out.println("nomatch");
        return null;
    }

    public static baseInfo GetInfo (int id) {
        baseInfo result = instance.bases.get(id);
        if (result.id == id) return result;
        for (baseInfo base : instance.bases) {
            if (base.id == id) return base;
        }
        return null;
    }

    public static baseInfo GetBaseOnTerritory (BlockPos pos) {
        return GetBaseOfTerritory(new ChunkPos(pos));
    }

    public static baseInfo GetBaseOfTerritory (ChunkPos pos) {
        for (baseInfo base : instance.bases) {
            if (base.ContainsChunk(pos)) return base;
        }
        return null;
    }

    public static void AddToDelayedInit(BlockPos pos) {
        if (delayed == null) delayed = new ArrayList<BlockPos>();
        delayed.add(pos);
    }

    public static boolean DeleteAllBases () {
        if (instance == null || instance.bases == null) return false;
        instance.bases.clear();
        if (instance.freeIds != null)
            instance.freeIds.clear();
        instance.SaveData();
        return true;
    }

    public static boolean IsPositionClaimed (BlockPos pos) {
        for (baseInfo base : instance.bases) {
            if (base.ContainsChunk(ChunkPosFromBlockPos(pos)) && !base.pos.equals(BlockPos.ORIGIN)) return true;
        }
        return false;
    }

    public static int PlayerBaseCount (String PlayerName) {
        int PlayerId = IDSmanager.GetPlayerId(PlayerName);
        int count = 0;
        for (baseInfo base : instance.bases) {
            if (base.OwnerId == PlayerId && !base.pos.equals(BlockPos.ORIGIN)) count++;
        }
        return count;
    }

    public static ChunkPos ChunkPosFromBlockPos (BlockPos pos) {
        int x = Math.floorDiv(pos.getX(), 16);
        int z = Math.floorDiv(pos.getZ(), 16);
        return new ChunkPos(x,z);
    }

    public static boolean DoBaseUpgrade (int Baseid, ChunkPos chunk) {
        baseInfo upgradeBase = GetInfo(Baseid);
        //if (upgradeBase == null) return false;
        TdmMod.logger.info(upgradeBase.pos);
        TileEntity t = TdmMod.currentServer.getWorld(upgradeBase.dimenision).getTileEntity(upgradeBase.pos); //if (t == null || !(t instanceof BaseTile)) return false;
        BaseTile te = (BaseTile)t;
        //if (te.isEmpty()) return false;
        te.decrStackSize(0, (upgradeBase.chunks.length < 32 ? upgradeBase.chunks.length % 8 + 1 : 8));
        upgradeBase.level++;
        ChunkPos[] newChunks = new ChunkPos[upgradeBase.chunks.length + 1];
        int i = 0;
        for (; i < upgradeBase.chunks.length; i++) {
            newChunks[i] = upgradeBase.chunks[i];
        }
        newChunks[i] = chunk;
        upgradeBase.chunks = newChunks;
        //Рассылаем игрокам обновленную инфу о базах
        GuiHandler.UpdateBaseGuis();
        if (upgradeBase.container != null) upgradeBase.container.detectAndSendChanges();
        return true;
    }

    public static void SetMember (BlockPos pos, String name, int level) {
        baseInfo b = GetInfo(pos);
        int id = IDSmanager.GetPlayerId(name);
        if (id == -1) return;
        if (level != -1) {
            int[] currentm = b.members;
            int[] nm = new int[currentm.length+1];
            for (int i = 0; i < currentm.length; i++) {
                if (currentm[i] == id) return;
                nm[i] = currentm[i];
            }
            nm[nm.length-1] = id;
            b.members = nm;
        }
        else {
            int[] currentm = b.members;
            int[] nm = new int[currentm.length-1];
            boolean ba = false;
            for (int i = 0; i < nm.length; i++) {
                if (currentm[i] == id) {i++; ba = true;}
                nm[ba ? i-1 : i] = currentm[i];
            }
            b.members = nm;
        }
    }
}
