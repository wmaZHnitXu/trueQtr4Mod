package com.anet.qtr4tdm.common.supers;

import com.anet.qtr4tdm.common.bases.baseInfo;

import net.minecraft.util.math.BlockPos;

public interface IBaseConnectable {
    
    public void Refresh ();

    public boolean ConnectToBase ();

    public void DisconnectFromBase ();

    public baseInfo GetBase ();

    public BlockPos getPosForBase ();
    
}
