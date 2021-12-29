package com.anet.qtr4tdm.common.tiles;

import java.util.ArrayList;
import com.anet.qtr4tdm.common.blocks.Mrk1Block;
import com.anet.qtr4tdm.common.entities.MrkAmmoEntity;
import com.anet.qtr4tdm.common.supers.TEDefenceInvEnrg;
import com.anet.qtr4tdm.init.BlocksInit;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;

public class Mrk1Tile extends TEDefenceInvEnrg {

    private int cooldown;
    private ArrayList<Entity> targets;
    private Entity target;
    private final double range = 100;

    @Override
    public void onLoad() {
        maxAmmo = 7;
        maxEnergy = 1000;
        super.onLoad();
    }

    @Override
    public void SetTargetsFromBase(ArrayList<Entity> targets) {
        this.targets = targets;
    }

    @Override
    public void Refresh() {
        
    }

    @Override
    public void update() {
        super.update();
        if (!world.isRemote) {
            if (!isEmpty()) {
                if (targets != null) {
                    double optimalRange = range;
                    double rangenew;
                    for (Entity e : targets) {
                        if ((rangenew = e.getPosition().getDistance(pos.getX(), pos.getY(), pos.getZ())) <= optimalRange) {
                            optimalRange = rangenew;
                            target = e;
                        } 
                    }
                }

                if (target != null) {
                    Launch();
                }
            }
            connected = base != null;
            powered = energy > 0;
        }
    }

    @Override
    public Item GetAmmoType() {
        return BlocksInit.MRKAMMO;
    }

    @Override
    protected void doAmmoUpdate() {
        Mrk1Block.SetArmed(world, pos, !ammo.get(0).isEmpty());
    }

    private void Launch () {
        MrkAmmoEntity ammoEntity = new MrkAmmoEntity(world);
        ammoEntity.setPosition(pos.getX() + 0.5d, pos.getY(), pos.getZ() + 0.5d);
        ammoEntity.SetTargetAndTargetPos(target);
        world.spawnEntity(ammoEntity);
        ammo.get(0).setCount(ammo.get(0).getCount()-1);
        target = null;
        targets = null;
    }

    @Override
    public int getPoints() {
        return 30;
    }

    @Override
    public int getRange() {
        return Double.valueOf(range).intValue();
    }
    
    @Override
    public int getConsumptionPerTick() {
        return 0;
    }
}
