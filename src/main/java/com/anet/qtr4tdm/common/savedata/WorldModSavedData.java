package com.anet.qtr4tdm.common.savedata;

import java.util.ArrayList;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.bases.baseInfo;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class WorldModSavedData extends WorldSavedData{

    ArrayList<baseInfo> bases;

    public WorldModSavedData () {
        super(TdmMod.MODID);
    }

    public WorldModSavedData(String name) {
        super(name);
    }

    public static WorldModSavedData get (World world) {
        MapStorage storage = world.getMapStorage();
        return (WorldModSavedData) storage.getOrLoadData(WorldModSavedData.class, TdmMod.MODID);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        // TODO Auto-generated method stub
        return null;
    }

    public void SetData (ArrayList<baseInfo> data) {

    }

    public ArrayList<baseInfo> GetData () {
        return bases;
    }
    
}
