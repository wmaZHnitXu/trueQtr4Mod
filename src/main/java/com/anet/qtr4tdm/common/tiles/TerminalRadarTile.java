package com.anet.qtr4tdm.common.tiles;

import java.util.ArrayList;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.uebki.RadarObjectStructure;
import com.anet.qtr4tdm.uebki.RadarsInfo;
import com.anet.qtr4tdm.uebki.Sounds;
import com.anet.qtr4tdm.uebki.messages.primitive.RadarInfoHandler;
import com.ibm.icu.util.BytesTrie.Result;

import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

public class TerminalRadarTile extends TileEntity implements ITickable {

    private int playersFound;
    private int counter = 0;

    @Override
    public void onLoad() {
        world.playSound(null, pos, Sounds.system_active, SoundCategory.BLOCKS, 1.0F, 1.0F);
        super.onLoad();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    public void update() {
        if (world.isRemote) return;
        if (counter >= 80) {
            counter = 0;
            MakeSound();
        }
        else counter++;  
    }

    private void MakeSound () {
        int plrCount = 0;
        if (plrCount > playersFound) {
            world.playSound(null, pos, Sounds.pidoras_naiden, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        else {
            if (plrCount > 0)
                world.playSound(null, pos, Sounds.beep, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        playersFound = plrCount;
    }
}

