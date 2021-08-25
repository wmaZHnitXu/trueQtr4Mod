package com.anet.qtr4tdm.common.supers;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public interface IDefenceSystem extends IBaseConnectable {

    public void SetTargetsFromBase (ArrayList<Entity> targets);

}
