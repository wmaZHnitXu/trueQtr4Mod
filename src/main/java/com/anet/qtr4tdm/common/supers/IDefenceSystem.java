package com.anet.qtr4tdm.common.supers;

import java.util.ArrayList;

import net.minecraft.entity.Entity;

public interface IDefenceSystem extends IBaseConnectable {

    public void SetTargetsFromBase (ArrayList<Entity> targets);

    public int getPoints ();

    public int getRange ();

}
