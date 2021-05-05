package com.anet.qtr4tdm.uebki.messages;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.tiles.EnergyConsumerTile;
import com.anet.qtr4tdm.uebki.EnergyTeams;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class BasedAnswer implements IMessage {

    public BasedAnswer(BlockPos pos, int reqId) {
        switch (reqId) {
            case 0: answer = new int[8]; // запрос по транслятору энергии.
                answer[0] = reqId;
                EnergyConsumerTile tile = (EnergyConsumerTile)TdmMod.currentServer.worlds[0].getTileEntity(pos);
                answer[1] = pos.getX();
                answer[2] = pos.getY();
                answer[3] = pos.getZ();
                answer[4] = (int)tile.getEnergyBuffer().getEnergyStored();
                answer[5] = (int)EnergyTeams.GetEnergyOfTeam(tile.GetTeam());
                answer[6] = (int)EnergyTeams.GetMaxEnergyOfTeam(tile.GetTeam());
                answer[7] = tile.GetTeam().ordinal();
            break;
        }
    }

    public BasedAnswer () {

    }

    int[] answer;

    @Override
    public void fromBytes(ByteBuf buf) {
        TdmMod.logger.info("basedanswerdelivered");
        int length = buf.getInt(0);
        answer = new int[length];
        for (int i = 1; i < length + 1; i++) {
            answer[i-1] = buf.getInt(i*4);
            TdmMod.logger.info(answer[i-1]);
        }

        //Vstavlyaem

        switch (answer[0]) {
            case 0:
                EnergyConsumerTile.InjectDataFromServer(answer[4], answer[5], answer[6], answer[7]);
            break;
        }

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(answer.length);
        for (int i = 0; i < answer.length; i++) {
            buf.writeInt(answer[i]);
        }
        TdmMod.logger.info("basedanswergenerated");
    }
    
}
