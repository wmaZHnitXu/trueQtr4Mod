package com.anet.qtr4tdm.common.entities;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.supers.IHasEnergy;
import com.anet.qtr4tdm.common.supers.Radar;
import com.anet.qtr4tdm.common.tiles.ThermalBaseTile;
import com.anet.qtr4tdm.uebki.messages.BasedRequest;

import ic2.api.energy.prefab.BasicSink;
import ic2.api.energy.prefab.BasicEnergyTe.Sink;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import scala.annotation.elidable;

public class RadarThermal1Entity extends Radar implements IHasEnergy {

    public boolean active;
    public float rot;
    private BasicSink tilebase;
    public boolean hasEnergy;

    @Override
    protected void entityInit() {
        setSize(1, 1);
    }

    public RadarThermal1Entity(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean isInRange(EntityPlayer e) {
        return (e.getPosition().distanceSq(getPosition()) < 1000 && hasEnergy);
    }

    @Override
    public void onUpdate() {
        rot += 0.1f;
        if (!world.isRemote) {
            if (tilebase != null && tilebase.canUseEnergy(1)) {
                tilebase.addEnergy(-1);
                hasEnergy = true;
            }
            else {
                if (tilebase == null) {
                    TileEntity te = world.getTileEntity(getPosition().down());
                    if (te != null)
                    tilebase = ((Sink)te).getEnergyBuffer();
                }
                hasEnergy = false;
            }
        }
        if (ticksExisted % 10 == 0 && world.isRemote) {
            TdmMod.wrapper.sendToServer(new BasedRequest(getPosition(), 6, world.provider.getDimension()));
        }
        super.onUpdate();
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void SetHasEnergy(boolean b) {
        hasEnergy = b;
    }
}
