package com.anet.qtr4tdm.uebki.messages;

import java.util.List;


import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.bases.InWorldBasesManager;
import com.anet.qtr4tdm.common.bases.baseInfo;
import com.anet.qtr4tdm.common.entities.Radar1Entity;
import com.anet.qtr4tdm.common.entities.RadarThermal1Entity;
import com.anet.qtr4tdm.common.supers.IHasEnergy;
import com.anet.qtr4tdm.common.supers.Radar;
import com.anet.qtr4tdm.common.tiles.EnergyConsumerTile;
import com.anet.qtr4tdm.uebki.RadarsInfo;
import com.anet.qtr4tdm.uebki.gui.BaseSetupGui;

import ic2.api.energy.prefab.BasicEnergyTe.Sink;

import com.anet.qtr4tdm.common.tiles.BaseTile;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import scala.Int;

public class BasedAnswer implements IMessage {

    public BasedAnswer(BlockPos pos, int reqId, int dim, int playerId) { //REQID RESERVE: 2
        //try {
            switch (reqId) {
                case 0: answer = new int[10]; // запрос по транслятору энергии.
                    EnergyConsumerTile tile = (EnergyConsumerTile)TdmMod.currentServer.worlds[dim].getTileEntity(pos);
                    answer[0] = reqId;
                    answer[1] = pos.getX();
                    answer[2] = pos.getY();
                    answer[3] = pos.getZ();
                    answer[4] = 0;
                    answer[5] = 0;
                    answer[6] = 0;
                    answer[7] = 0;
                    answer[8] = 0;
                    answer[9] = 0;
                break;
                case 1: answer = new int[5];
                    answer[0] = reqId;
                    answer[1] = pos.getX();
                    answer[2] = pos.getY();
                    answer[3] = pos.getZ();
                    answer[4] = RadarsInfo.IsRadarActive(pos) ? 1 : 0;
                break;
                case 3: answer = new int[4];
                    answer[0] = reqId;
                    String name = TdmMod.currentServer.getWorld(dim).getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 100, false).getName(); //TODO PEREPISAT'
                    answer[1] = InWorldBasesManager.PlayerBaseCount(name);
                    answer[2] = InWorldBasesManager.IsPositionClaimed(pos) ? 1 : 0;
                    answer[3] = InWorldBasesManager.GetNewBaseId();
                break;
                case 4: answer = new int[2];
                answer[0] = reqId;
                    baseInfo i = InWorldBasesManager.AddNormalBase(pos, TdmMod.currentServer.getWorld(dim).getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 100, false), dim); //TODO PEREPISAT'
                    if (i != null) {
                        ((BaseTile)TdmMod.currentServer.getWorld(dim).getTileEntity(pos)).InsertDirectInfo(i);;
                        answer[1] = 1;
                    }
                    else {
                        answer[1] = 0;
                    }
                break;
                case 6: answer = new int[5];
                    answer[0] = reqId;
                    answer[1] = pos.getX();
                    answer[2] = pos.getY();
                    answer[3] = pos.getZ();
                    TileEntity te = TdmMod.currentServer.getWorld(dim).getTileEntity(pos.down());
                    if (te instanceof Sink)
                        answer[4] =  Double.valueOf(((Sink)te).getEnergyBuffer().getEnergyStored()).intValue();
                    else answer[4] = 0;
                break;
            }
        //}
        //catch (Exception e) {
        //    TdmMod.logger.info(e.toString());
        //}
    }

    public BasedAnswer () {

    }

    int[] answer;

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            int length = buf.getInt(0);
            answer = new int[length];
            for (int i = 1; i < length + 1; i++) {
                answer[i-1] = buf.getInt(i*4);
            }

            //Vstavlyaem

            switch (answer[0]) {
                case 0:
                    EnergyConsumerTile.InjectDataFromServer(answer[4], answer[5], answer[6], answer[7], answer[8], answer[9]);
                break;

                case 1:
                    BlockPos pos = new BlockPos(answer[1], answer[2], answer[3]);
                    BlockPos a = pos.add(2, 2, 2);
                    BlockPos b = pos.add(-2, -2, -2);
                    List<Radar1Entity> ent = Minecraft.getMinecraft().world.getEntitiesWithinAABB(Radar1Entity.class, new AxisAlignedBB(a, b));
                    if (ent.size() > 0) {
                        ent.get(0).isActive = answer[4] > 0;
                    }
                break;
                case 3:
                    TdmMod.logger.info("answer 3");
                    BaseSetupGui.InsertInfoFromServer(answer[2] == 1, answer[1] < 1, answer[3]);
                break;
                case 4:
                    BaseSetupGui.SetDone(answer[1] == 1);
                break;

                case 6:
                    BlockPos pos1 = new BlockPos(answer[1], answer[2], answer[3]);
                    List<RadarThermal1Entity> ent1 = Minecraft.getMinecraft().world.getEntitiesWithinAABB(RadarThermal1Entity.class, new AxisAlignedBB(pos1));
                    if (ent1.size() != 0) {
                        ent1.get(0).hasEnergy = answer[4] > 0;
                    }
            }
        }
        catch (Exception e) {
            TdmMod.logger.info(e.toString());
            
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(answer.length);
        for (int i = 0; i < answer.length; i++) {
            buf.writeInt(answer[i]);
        }
    }
    
}
