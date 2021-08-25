package com.anet.qtr4tdm.common.bases;


import java.util.ArrayList;

import com.anet.qtr4tdm.common.supers.IBaseConnectable;
import com.anet.qtr4tdm.common.supers.IDefenceSystem;
import com.anet.qtr4tdm.common.supers.Radar;
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
    //TEMP
    public BaseContainer container;
    public ArrayList<RadarTrackingInfo> radarTrackingData;
    public ArrayList<Radar> radars;
    public ArrayList<IDefenceSystem> defenders;
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

        radars = new ArrayList<Radar>();
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
            if (GetDefCount() < defenders.size()) {
                defenders.add((IDefenceSystem)sys);
                return true;
            }
            return false;
        }
        else if (sys instanceof Radar) {
            radars.add((Radar)sys);
            return true;
        }
        return false;
    }

    public void DisconnectDefenceSystem (IBaseConnectable sys) {
        if (sys instanceof IDefenceSystem) {
            defenders.remove((IDefenceSystem)sys);
        }
        else if (sys instanceof Radar) {
            radars.remove((Radar)sys);
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
        for (IBaseConnectable s : radars) {
            s.Refresh();
        }
        for (IBaseConnectable s : defenders) {
            s.Refresh();
        }
    }

    public void UpdateAllRadars () {
        ArrayList<RadarTrackingInfo> result = new ArrayList<RadarTrackingInfo>();
        for (Radar s : radars) {
            for (RadarTrackingInfo info : s.ReportTargetsToBase()) {
                if (!result.contains(info)) {
                    result.add(info);
                }
            }
        }
        radarTrackingData = null;
        radarTrackingData = result;

        //DEBYG
        for (IDefenceSystem def : defenders) {
            if (result.size() > 0) {
                ArrayList<Entity> list = new ArrayList<Entity>();
                list.add(result.get(0).ent);
                def.SetTargetsFromBase(list);
            }
        }
    }

    public void Tick () {
        timeExisted++;
        if (timeExisted % 10 == 0) {
            UpdateAllRadars();
        } 
    }
}
