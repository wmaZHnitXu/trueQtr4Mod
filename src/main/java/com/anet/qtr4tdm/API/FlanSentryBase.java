package com.anet.qtr4tdm.API;

import java.util.ArrayList;

import com.anet.qtr4tdm.common.bases.InWorldBasesManager;
import com.anet.qtr4tdm.common.supers.EntityBaseConnectable;
import com.anet.qtr4tdm.common.supers.IDefenceSystem;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class FlanSentryBase extends EntityBaseConnectable implements IDefenceSystem {


    class atkInfo {
        EntityPlayer p;
        boolean can;
        public atkInfo(EntityPlayer p, boolean can) {this.p = p; this.can = can;}
    }

    private Entity priorityTarget;

    public ArrayList<atkInfo> cachedAttackInfo;

    public FlanSentryBase(World worldIn) {
        super(worldIn);
        cachedAttackInfo = new ArrayList<atkInfo>();
    }

    public boolean canAttack (EntityPlayer player) {
        if (base == null) return false;
        for (atkInfo i : cachedAttackInfo) {
            if (i.p == player) return i.can;
        }
        boolean result = InWorldBasesManager.GetPlayerRelationsWithBase(player, base) < -1;
        cachedAttackInfo.add(new atkInfo(player, result));
        return result;
    }

    @Override
    public void SetTargetsFromBase(ArrayList<Entity> targets) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void Refresh () {
        priorityTarget = null;
        cachedAttackInfo.clear();
    }
}
