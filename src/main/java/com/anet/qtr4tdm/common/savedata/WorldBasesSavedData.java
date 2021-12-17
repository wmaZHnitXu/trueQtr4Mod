package com.anet.qtr4tdm.common.savedata;

import java.util.ArrayList;
import java.util.jar.Attributes.Name;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.bases.baseInfo;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class WorldBasesSavedData extends WorldSavedData {

    public ArrayList<baseInfo> bases;
    public ArrayList<Integer> freeIds;

    public WorldBasesSavedData () {
        super(TdmMod.MODID);
    }

    public WorldBasesSavedData(String name) {
        super(name);
    }

    public static WorldBasesSavedData get (World world) {
        MapStorage storage = world.getMapStorage();
        WorldBasesSavedData instance = (WorldBasesSavedData) storage.getOrLoadData(WorldBasesSavedData.class, TdmMod.MODID);

        if (instance == null) {
            instance = new WorldBasesSavedData();
            storage.setData(TdmMod.MODID, instance);
        }
        return instance;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        int length = nbt.getInteger("length");
        bases = new ArrayList<baseInfo>();
        freeIds = new ArrayList<Integer>();
        for (int i = 0; i < length; i++) {
            BlockPos pos;
                int[] arr = nbt.getIntArray("pos" + i);
                pos = new BlockPos(arr[0], arr[1], arr[2]);
            int OwnerId = nbt.getInteger("owner" + i);
            int id = nbt.getInteger("id" + i);
            int level = nbt.getInteger("level" + i);
            int dimension = nbt.getInteger("dim" + i);
            ChunkPos[] chunks;
                int[] arrX, arrZ;
                arrX = nbt.getIntArray("chunkX" + i);
                arrZ = nbt.getIntArray("chunkZ" + i);
                chunks = new ChunkPos[arrX.length];
                for (int j = 0; j < arrX.length; j++) {
                    chunks[j] = new ChunkPos(arrX[j], arrZ[j]);
                }
            int[] members = nbt.getIntArray("members" + i);
            String name = nbt.getString("name");
            bases.add(new baseInfo(pos, OwnerId, id, level, chunks, dimension, name, members)); //TODO
        }

        //FREEIDS
        int[] arr = nbt.getIntArray("freeids");
        freeIds = new ArrayList<Integer>(arr.length);
        for (int i = 0; i < arr.length; i++) {
            freeIds.set(i, arr[i]);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("length", bases.size());
        for (int i = 0; i < bases.size(); i++) {
            baseInfo base = bases.get(i);
            int id = base.id;
            compound.setIntArray("pos" + id, new int[] {base.pos.getX(), base.pos.getY(), base.pos.getZ()}); //BlockPos
            compound.setInteger("owner" + id, base.OwnerId); //XOZYAIN
            compound.setInteger("id" + id, id); //ID базы
            compound.setInteger("dim" + id, base.dimenision); //измерение
            compound.setInteger("level" + id, base.level);
            //Коорды чанков в два массива
            int[] x = new int[base.chunks.length];
            int[] z = new int[base.chunks.length];
            for (int j = 0; j < base.chunks.length; j++) {
                x[j] = base.chunks[j].x;
                z[j] = base.chunks[j].z;
            }
            compound.setIntArray("chunkX" + id, x);
            compound.setIntArray("chunkZ" + id, z);
            compound.setIntArray("members" + id, base.members);
            compound.setString("name", base.name);
        }

        //FREEIDS
        int[] arr = new int[freeIds.size()];
        for (int i = 0; i < arr.length; i++)
        {
            arr[i] = freeIds.get(i);
        }
        compound.setIntArray("freeids", arr);
        return compound;
    }

    public void SetData (ArrayList<baseInfo> data, ArrayList<Integer> freeIdsdata) {
        bases = data;
        freeIds = freeIdsdata;
        markDirty();
    }
}
