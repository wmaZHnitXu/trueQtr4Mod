package com.anet.qtr4tdm.common.tiles;

import java.util.ArrayList;
import java.util.List;

import com.anet.qtr4tdm.common.blocks.RadarBaseBlock;
import com.anet.qtr4tdm.common.blocks.RadarMasterBlock;
import com.anet.qtr4tdm.common.blocks.RadarSlaveBlock;
import com.anet.qtr4tdm.common.entities.Radar1Entity;
import com.anet.qtr4tdm.common.entities.Radar2Entity;
import com.anet.qtr4tdm.common.entities.Radar3Entity;
import com.anet.qtr4tdm.common.entities.render.RenderRadar3;
import com.anet.qtr4tdm.init.BlocksInit;
import com.anet.qtr4tdm.uebki.RadarsInfo;
import com.anet.qtr4tdm.uebki.RadarInfoStruct;
import com.anet.qtr4tdm.uebki.Teams;
import com.anet.qtr4tdm.uebki.teamState;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class RadarWorkerTile extends TileEntity{
    private boolean isMaster;
    private boolean transformed;
    private int level; //1 - 1, 2 - 2, 3 - 3...
    private RadarWorkerTile master;
    private int team;
    private ArrayList<RadarWorkerTile> slaves;
    private int crutilcaId;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setBoolean("isMaster", isMaster);
        tagCompound.setBoolean("transformed", this.transformed);
        tagCompound.setInteger("level", this.level);
        tagCompound.setInteger("crutilka", crutilcaId);
        tagCompound.setInteger("team", team);
        return super.writeToNBT(tagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        this.isMaster = tagCompound.getBoolean("isMaster");
        if (isMaster) master = this;
        this.transformed = tagCompound.getBoolean("transformed");
        this.level =  tagCompound.getInteger("level");
        this.crutilcaId = tagCompound.getInteger("crutilka");
        this.team = tagCompound.getInteger("team");
        super.readFromNBT(tagCompound);
    }
    
    public void ReadSlaves () {
        int lvl = this.level;
        slaves = new ArrayList<RadarWorkerTile>();
        switch (lvl) {
            case 1:
                ((RadarWorkerTile)world.getTileEntity(pos.west())).GetAndSetMasterFromAware(this);
                ((RadarWorkerTile)world.getTileEntity(pos.east())).GetAndSetMasterFromAware(this);
                ((RadarWorkerTile)world.getTileEntity(pos.south())).GetAndSetMasterFromAware(this);
                ((RadarWorkerTile)world.getTileEntity(pos.north())).GetAndSetMasterFromAware(this);
            break;
            case 2:
            case 3:
            for (int x = pos.getX()-(lvl-1); x < pos.getX() + lvl; x++) {
                for (int z = pos.getZ()-(lvl-1); z < pos.getZ() + lvl; z++) {
                    BlockPos positionToChange = new BlockPos(x, pos.getY(), z);
                    if (world.getBlockState(positionToChange).getBlock().getClass() == RadarSlaveBlock.class)
                        ((RadarWorkerTile)world.getTileEntity(positionToChange)).GetAndSetMasterFromAware(this);
                }
            }
            break;
        }
    }

    private int CheckLevel () {
        int result = 0;
        if (world.getBlockState(pos.north()).getBlock().getClass() == RadarBaseBlock.class) result++;
        if (world.getBlockState(pos.north().east()).getBlock().getClass() == RadarBaseBlock.class) result++;
        if (world.getBlockState(pos.north().north()).getBlock().getClass() == RadarBaseBlock.class) result++;
        return result;
    }

    public void firstCheck () {
        if (world.getBlockState(pos).getBlock().getClass() == RadarMasterBlock.class) {
            MakeMeMaster();
        }
        markDirty();
    }
    private void MakeMeMaster () {
        isMaster = true;
        master = this;
        if (!transformed) {
            level = CheckLevel();
            Transformation();
        }
        RadarsInfo.AddRadar(new RadarInfoStruct(teamState.values()[this.team], this.pos, level));
    }

    private void Transformation () {
        slaves = new ArrayList<RadarWorkerTile>();
        IBlockState center = BlocksInit.RADARS.getDefaultState();
        IBlockState[] corner = {};
        IBlockState[] edge = {};
        Entity e = null;
        switch (level) {
            case 1:
            InitTileAndSetBlock(pos.north(), center);
            InitTileAndSetBlock(pos.south(), center);
            InitTileAndSetBlock(pos.west(), center);
            InitTileAndSetBlock(pos.east(), center);
            e = new Radar1Entity(world);
            break;
            case 2:
            for (int i = pos.getX()-1; i < pos.getX() + 2; i++) {
                InitTileAndSetBlock(new BlockPos(i, pos.getY(), pos.getZ()+1),center);
                InitTileAndSetBlock(new BlockPos(i, pos.getY(), pos.getZ()-1),center);
            }
            InitTileAndSetBlock(pos.east(),center);
            InitTileAndSetBlock(pos.west(),center);
            e = new Radar2Entity(world);
            break;
            case 3:
            for (int x = pos.getX()-2; x < pos.getX() + 3; x++) {
                for (int z = pos.getZ()-2; z < pos.getZ() + 3; z++) {
                    BlockPos positionToChange = new BlockPos(x, pos.getY(), z);
                    if (world.getBlockState(positionToChange).getBlock().getClass() == RadarBaseBlock.class)
                        InitTileAndSetBlock(positionToChange, center);
                }
            }
            //Entity
            e = new Radar3Entity(world);
            break;
        }
        BlockPos position = new BlockPos(pos.getX() + 0.5f, pos.up().getY(), pos.getZ() + 0.5f);
        e.setPosition(position.getX() , position.getY(), position.getZ());
        world.spawnEntity(e);
        teamState tm = Teams.GetClosestPlayer(this.pos).team;
        this.team = tm.ordinal();
        markDirty();
        transformed = true;
    }
    private void InitTileAndSetBlock (BlockPos position, IBlockState state) {
        world.setBlockState(position, state);
        ((RadarWorkerTile)world.getTileEntity(position)).GetAndSetMasterFromAware(this);;
    }

    public RadarWorkerTile GetMaster () {
        RadarWorkerTile result = this.master;
        if (result == null) 
            for (int x = pos.getX()-2; x < pos.getX() + 3; x++) {
                for (int z = pos.getZ()-2; z < pos.getZ() + 3; z++) {
                    BlockPos positionToChange = new BlockPos(x, pos.getY(), z);
                    if (world.getBlockState(positionToChange).getBlock().getClass() == RadarMasterBlock.class) {
                        result = ((RadarWorkerTile)world.getTileEntity(positionToChange));
                        result.ReadSlaves();
                        this.master = result;
                    }
                }
            }
        return this.master;
    }
    private void GetAndSetMasterFromAware(RadarWorkerTile masterFinder) {
        master = masterFinder.GetMaster();
        master.AddSlave(this);
    }
    public void AddSlave(RadarWorkerTile slave) {
        slaves.add(slave);
    }
    public void RemoveSlave(RadarWorkerTile slave) {
        slaves.remove(slave);
        Decomposition();
        if (this != slave) world.setBlockState(this.pos, BlocksInit.RADAR.getDefaultState());
    }
    public void Decomposition () {
        if (slaves == null) ReadSlaves();
        IBlockState rbaseState = BlocksInit.RADAR.getDefaultState();
        for (int i = 0; i < slaves.size(); i++) {
            if (slaves.get(i) != this)
                world.setBlockState(slaves.get(i).pos, rbaseState);
        }
        List<Entity> e1 = world.getEntitiesWithinAABB((level == 1 ? Radar1Entity.class : level == 2 ? Radar2Entity.class : Radar3Entity.class) , new AxisAlignedBB(pos.down(2).north(2).east(2), pos.up(2).south(2).west(2)));
        if (e1.size() > 0) e1.get(0).setDead();
        RadarsInfo.RemoveRadar(pos);
    }
}
