package com.anet.qtr4tdm.uebki.messages.primitive;

import java.util.ArrayList;

import com.anet.qtr4tdm.uebki.RadarObjectStructure;
import com.anet.qtr4tdm.uebki.RadarsInfo;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class RadarMessage implements IMessage {

    public RadarMessage (BlockPos pos) {
        this.pos = pos;
    }

    public RadarMessage () {

    }

    private BlockPos pos;
    public ArrayList<RadarObjectStructure> objs;

    @Override
    public void fromBytes(ByteBuf buf) {
        objs = new ArrayList<RadarObjectStructure>();
        String received = ByteBufUtils.readUTF8String(buf);
        String[] objects = received.split("«");
        for (String object : objects) {
            if (object != "") { //TODO Можно и без него, переделав цикл выше в не foreach и начиная переборку с 1, но посрать
                String[] splitedObject = object.split("»");
                if (splitedObject.length == 5) {
                    RadarObjectStructure objToList = new RadarObjectStructure(
                        new BlockPos(Integer.parseInt(splitedObject[2]), Integer.parseInt(splitedObject[3]), Integer.parseInt(splitedObject[4])),
                        splitedObject[0],
                        splitedObject[1].equals("da") || splitedObject[1].equals("offline"), !splitedObject[1].equals("offline"));
                    objs.add(objToList);
                }
            }
        }
        RadarInfoHandler.objects = objs;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ArrayList<RadarObjectStructure> objects = null;
        objects = RadarsInfo.instance.GetInfoForPos(pos);
        String prepString = "";
        for (RadarObjectStructure object : objects) {
            prepString += "«";
            prepString += object.DisplayName;
            prepString += "»";
            prepString += object.isRadar ? (object.isActive ? "da" : "offline" ) : "net";
            prepString += "»";
            prepString += object.pos.getX();
            prepString += "»";
            prepString += object.pos.getY();
            prepString += "»";
            prepString += object.pos.getZ();
        }
        ByteBufUtils.writeUTF8String(buf, prepString);
    }
    
}
