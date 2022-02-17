package com.anet.qtr4tdm.common.bases;

import java.util.ArrayList;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.savedata.WorldBasesSavedData;
import com.anet.qtr4tdm.common.supers.IBaseConnectable;
import com.anet.qtr4tdm.common.supers.RadarTrackingInfo;
import com.anet.qtr4tdm.common.tiles.BaseTile;
import com.anet.qtr4tdm.uebki.IDSmanager;
import com.anet.qtr4tdm.uebki.gui.GuiHandler;
import com.anet.qtr4tdm.uebki.messages.primitive.TopBarMessage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class InWorldBasesManager {
    ArrayList<baseInfo> bases;
    private static ArrayList<BlockPos> delayed;
    private static ArrayList<IBaseConnectable> needConnections;
    public static InWorldBasesManager instance;

    public static InWorldBasesManager GetInstance () {
        if (instance == null) instance = new InWorldBasesManager();
        return instance;
    }

    public ArrayList<Integer> freeIds;

    public InWorldBasesManager() {
        System.out.println("delay3");
        instance = this;

        if (TdmMod.currentServer == null) return;

        WorldBasesSavedData data = WorldBasesSavedData.get((World)TdmMod.currentServer.getWorld(0));
        if (data.bases != null) bases = data.bases; else bases = new ArrayList<baseInfo>();
        if (data.freeIds != null) freeIds = data.freeIds; else freeIds = new ArrayList<Integer>();

        TdmMod.logger.info(bases.size());
        //Resolve delayed
        if (delayed != null) {
            System.out.println("resolvedel");
            for (BlockPos pos : delayed) {
                System.out.println("resolvedelb");
                BaseTile baseTile = (BaseTile)(TdmMod.currentServer.getWorld(0).getTileEntity(pos));
                for (baseInfo base : bases) {
                    System.out.println(base.pos.toString() + " == " + pos.toString() + " : " + base.pos.equals(pos));

                    if (base.pos.equals(pos)) {
                        System.out.println("insert");
                        baseTile.InsertDirectInfo(base);
                    }
                }
            }
        }
        if (needConnections != null) {
            System.out.println("resolvedel");
            for (IBaseConnectable con : needConnections) {
                con.ConnectToBase();
            }
        }
    }

    private void SaveData () {
        WorldBasesSavedData.get((World)TdmMod.currentServer.getWorld(0)).SetData(bases, freeIds);
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
        if (GetInstance().freeIds.contains(id)) { 
            GetInstance().freeIds.remove(0);
            for (int i = 0; i < GetInstance().bases.size(); i++) {
                if (GetInstance().bases.get(i).id == id) {GetInstance().bases.remove(i); i--;}
            } 
        }
        GetInstance().bases.add(base);
        GetInstance().SaveData();
        ConnectDefenceFromChunk(base, chunks[0]);
        return base;
    }

    public static int GetNewBaseId () {
        int id; if (GetInstance().freeIds != null && GetInstance().freeIds.size() > 0) id = GetInstance().freeIds.get(0); else {id = GetInstance().bases.size();}
        return id;
    }

    public static void RemoveBase (BlockPos pos) {
        for (int i = 0; i < GetInstance().bases.size(); i++) {
            baseInfo base = GetInstance().bases.get(i);
            if (base == null || base.pos.equals(pos)) {
                GetInstance().freeIds.add(base.id);
                GetInstance().bases.get(i).pos = new BlockPos(0, 0, 0);
                GetInstance().bases.get(i).chunks = new ChunkPos[0];
            }
            if (base != null) base.DisconnectAllDefenceSystems();
            EntityPlayer owner = IDSmanager.GetPlayer(base.OwnerId);
            if (owner != null) TdmMod.wrapper.sendTo(new TopBarMessage("§cВаша база уничтожена.", "Ну нет её больше...", 1, 500), (EntityPlayerMP)owner);
        }
        GetInstance().SaveData();
    }

    public static baseInfo GetInfo (BlockPos pos) { //Лучше заменить на tile P.S. ИСПОЛЬЗУЕТСЯ ДЛЯ ИНИЦИАЛИЗАЦИИ ТАЙЛА
        if (instance == null) { AddToDelayedInit(pos);
             System.out.println("delay"); return null;}
        for (baseInfo base : GetInstance().bases) {
            if (base.pos.equals(pos)) return base;
        }
        return null;
    }

    public static void GetBaseConnection (IBaseConnectable con) {
        if (GetInstance() == null) { AddDelayedToConnect(con); System.out.println("IBaseConnectableDelay");}
        else con.ConnectToBase();
    }

    public static void ConnectDefenceFromChunk (baseInfo base, ChunkPos pos) {
        World world = TdmMod.currentServer.getWorld(base.dimenision);
        for (int x = pos.getXStart(); x <= pos.getXEnd(); x++) {
            for (int z = pos.getZStart(); z <= pos.getZEnd(); z++) {
                int y = world.getHeight(x, z)-1;
                BlockPos pos2 = new BlockPos(x,y,z);
                TileEntity te = world.getTileEntity(pos2);
                System.out.println(x + " - x; " + y + " - y" + z + " - z; " + (te == null ? "da" : "net"));
                if (te instanceof IBaseConnectable) {
                    ((IBaseConnectable)te).ConnectToBase();
                }
            }
        }
    }

    private static void AddDelayedToConnect (IBaseConnectable con) {
        if (con.getPosForBase().equals(BlockPos.ORIGIN)) return;
        if (needConnections == null) needConnections = new ArrayList<IBaseConnectable>();
        for (int i = 0; i < needConnections.size(); i++) {
            if (needConnections.get(i).getPosForBase().equals(con.getPosForBase())) {
                needConnections.set(i, con); return;
            }
        }
        needConnections.add(con);
    }

    public static baseInfo GetInfo (int id) {
        baseInfo result = GetInstance().bases.get(id);
        if (result.id == id) return result;
        for (baseInfo base : GetInstance().bases) {
            if (base.id == id) return base;
        }
        return null;
    }

    public static baseInfo GetBaseOfTerritory (BlockPos pos) {
        return GetBaseOfTerritory(new ChunkPos(pos));
    }

    public static baseInfo GetBaseOfTerritory (ChunkPos pos) {
        for (baseInfo base : GetInstance().bases) {
            if (base.ContainsChunk(pos)) return base;
        }
        return null;
    }

    public static void AddToDelayedInit(BlockPos pos) {
        if (delayed == null) delayed = new ArrayList<BlockPos>();
        if (!delayed.contains(pos)) delayed.add(pos);
    }

    public static boolean DeleteAllBases () {
        if (instance == null || GetInstance().bases == null) return false;
        GetInstance().bases.clear();
        if (GetInstance().freeIds != null)
            GetInstance().freeIds.clear();
        GetInstance().SaveData();
        return true;
    }

    public static int GetPlayerRelationsWithBase (EntityPlayer p, baseInfo b) {
        int result = -2;
        int id = IDSmanager.GetPlayerId(p);
        if (b.isMember(id)) result = 0;
        if (b.OwnerId == id) result = 2;
        return result;
    }

    public static boolean IsPositionClaimed (BlockPos pos) {
        for (baseInfo base : GetInstance().bases) {
            if (base.ContainsChunk(new ChunkPos(pos)) && !base.pos.equals(BlockPos.ORIGIN)) return true;
        }
        return false;
    }

    public static int PlayerBaseCount (String PlayerName) {
        int PlayerId = IDSmanager.GetPlayerId(PlayerName);
        int count = 0;
        for (baseInfo base : GetInstance().bases) {
            if (base.OwnerId == PlayerId && !base.pos.equals(BlockPos.ORIGIN)) count++;
        }
        return count;
    }

    public static boolean DoBaseUpgrade (int Baseid, ChunkPos chunk) {
        baseInfo upgradeBase = GetInfo(Baseid);
        TdmMod.logger.info(upgradeBase.pos);
        if (upgradeBase.ContainsChunk(chunk)) return false;
        TileEntity t = TdmMod.currentServer.getWorld(upgradeBase.dimenision).getTileEntity(upgradeBase.pos);
        BaseTile te = (BaseTile)t;
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
        ConnectDefenceFromChunk(upgradeBase, chunk);
        GetInstance().SaveData();
        return true;
    }

    public static void SetMember (BlockPos pos, String name, int level) {
        baseInfo b = GetInfo(pos);
        b.SetMember(name, level);
        GetInstance().SaveData();
    }

    public static void PlayerDeadSubmit (LivingDeathEvent evt) {
        if (instance == null) return;
        for (baseInfo base : instance.bases) {
            if (base.radarTrackingData != null)
            for (int i = 0; i < base.radarTrackingData.size(); i++) {
                RadarTrackingInfo info = base.radarTrackingData.get(i);
                if (info != null && info.ent == evt.getEntity()) {
                    base.AddDeadPlayer(evt);
                }
            }
        }
    }
}
