package com.anet.qtr4tdm.uebki.messages;

import java.util.List;

import javax.swing.text.AbstractDocument.BranchElement;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.entities.Radar1Entity;
import com.anet.qtr4tdm.common.tiles.EnergyConsumerTile;
import com.anet.qtr4tdm.uebki.RadarsInfo;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class BasedAnswer implements IMessage {

    public BasedAnswer(BlockPos pos, int reqId) {
        try {
            switch (reqId) {
                case 0: answer = new int[10]; // запрос по транслятору энергии.
                    answer[0] = reqId;
                    EnergyConsumerTile tile = (EnergyConsumerTile)TdmMod.currentServer.worlds[0].getTileEntity(pos);
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
            }
        }
        catch (Exception e) {
            TdmMod.logger.info(e.toString());
        }
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
