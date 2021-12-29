package com.anet.qtr4tdm.common.bases;


import java.util.ArrayList;

import com.anet.qtr4tdm.common.supers.IBaseConnectable;
import com.anet.qtr4tdm.common.supers.IDefenceSystem;
import com.anet.qtr4tdm.common.supers.IRadar;
import com.anet.qtr4tdm.common.supers.RadarTrackingInfo;
import com.anet.qtr4tdm.uebki.IDSmanager;
import com.anet.qtr4tdm.uebki.gui.baseGuiMisc.BaseContainer;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class baseInfo {
    public BlockPos pos;
    public int OwnerId;
    public int id;
    public int level;
    public ChunkPos[] chunks;
    public int dimenision;
    public int[] members;
    public String name;
    public baseStatus status;

    //TEMP
    public BaseContainer container;
    public ArrayList<RadarTrackingInfo> radarTrackingData;
    public ArrayList<IDefenceSystem> defenders;
    public int defcount; // clientside only
    public int timeExisted;

    public baseInfo (BlockPos pos, int OwnerId, int id, int level, ChunkPos[] chunks, int dimension, String name) {
        this.pos = pos;
        this.OwnerId = OwnerId;
        this.id = id;
        this.level = level;
        this.chunks = chunks;
        this.dimenision = dimension;
        this.name = name;
        this.members = new int[0];
        this.status = baseStatus.Peace;

        defenders = new ArrayList<IDefenceSystem>();
        
    }

    public baseInfo (BlockPos pos, int OwnerId, int id, int level, ChunkPos[] chunks, int dimension, String name, int[] members) {
        this(pos, OwnerId, id, level, chunks, dimension, name);
        this.members = members;
    }

    public boolean ContainsChunk (ChunkPos pos) {
        for (ChunkPos chunk : chunks) {
            if (pos.equals(chunk)) return true;
        }
        return false;
        
    }

    public boolean isMember (int id) {
        if (id == OwnerId) return true;
        if (members == null) return false;
        for (int i : members) if (i == id) return true;
        return false;
    }

    public int GetDefCount () {
        return 2 + level * 2;
    }

    public boolean ConnectDefenceSystem (IBaseConnectable sys) {
        if (sys instanceof IDefenceSystem) {

                if (GetDefCount() > defenders.size() && !defenders.contains(sys)) {

                    for (int i = 0; i < defenders.size(); i++) {
                        if (sys.getPosForBase().equals(defenders.get(i).getPosForBase())) { defenders.remove(i); i--;}
                    }
                    defenders.add((IDefenceSystem)sys);
                    return true;
                }

        }
        return false;
    }

    public void DisconnectDefenceSystem (IBaseConnectable sys) {
        if (sys instanceof IDefenceSystem) {

                for (int i = 0; i < defenders.size(); i++) {
                    if (defenders.get(i) == null) {
                        defenders.remove(i);
                        i--;
                    }
                }
                defenders.remove((IDefenceSystem)sys);
        }
    }

    public void DisconnectAllDefenceSystems () {
        for (IBaseConnectable sys : defenders) {
            sys.DisconnectFromBase();
        }
    }

    public void SetMember (String name, int level) {
        int id = IDSmanager.GetPlayerId(name);
        if (id == -1) return;
        if (level != -1) {
            int[] currentm = this.members;
            int[] nm = new int[currentm.length+1];
            for (int i = 0; i < currentm.length; i++) {
                if (currentm[i] == id) return;
                nm[i] = currentm[i];
            }
            nm[nm.length-1] = id;
            this.members = nm;
        }
        else {
            int[] currentm = this.members;
            int[] nm = new int[currentm.length-1];
            boolean ba = false;
            for (int i = 0; i < nm.length; i++) {
                if (currentm[i] == id) {i++; ba = true;}
                nm[ba ? i-1 : i] = currentm[i];
            }
            this.members = nm;
        }
    }

    public void RefreshAllSystems () {
        for (IBaseConnectable s : defenders) {
            s.Refresh();
        }
    }

    public void UpdateAllRadars () {
        ArrayList<RadarTrackingInfo> result = new ArrayList<RadarTrackingInfo>();
        for (IDefenceSystem s : defenders) {
            if (s instanceof IRadar) {
                IRadar rad = (IRadar)s;
                for (RadarTrackingInfo info : rad.ReportTargetsToBase(this)) {
                    if (!result.contains(info)) {
                        result.add(info);
                    }
                }
            }
        }
        radarTrackingData = null;
        radarTrackingData = result;

        SendTargets();
    }

    public void SendTargets () {
        ArrayList<Entity> validTargets = new ArrayList<Entity>();
        for (RadarTrackingInfo i : radarTrackingData) {
            if (i.type < 10 && i.ent != null && !i.ent.isDead) {
                validTargets.add(i.ent);
                if (status != baseStatus.Emergency) status = baseStatus.Threat;
                if (ContainsChunk(new ChunkPos(i.ent.getPosition()))) status = baseStatus.Emergency;
            }
        }

        if (validTargets.size() == 0) {
            status = baseStatus.Peace;
        }
        
        for (IDefenceSystem def : defenders) {
            def.SetTargetsFromBase(validTargets);
        }
    }

    public void Tick () {
        timeExisted++;
        if (timeExisted % 10 == 0) {
            UpdateAllRadars();
        } 
    }
}
