package com.anet.qtr4tdm.uebki.messages.primitive;

import java.util.ArrayList;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.bases.InWorldBasesManager;
import com.anet.qtr4tdm.common.supers.IDefenceSystem;
import com.anet.qtr4tdm.common.supers.IHasAmmo;
import com.anet.qtr4tdm.common.supers.IHasEnergy;
import com.anet.qtr4tdm.uebki.gui.BaseGui;
import com.anet.qtr4tdm.uebki.messages.basic.IBasicMessage;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

public class DefenceDataToPlayer implements IBasicMessage {

    public class DefDataStruct {
        public BlockPos pos;
        public int ammoCount;
        public int chargeStatus;
        private String name;

        public DefDataStruct (BlockPos pos, int ammoCount, int chargeStatus) {
            
            this.pos = pos;
            this.ammoCount = ammoCount;
            this.chargeStatus = chargeStatus;
            name = "";

        }

        public String getName () {
            if (name.equals("")) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc != null && mc.world != null && mc.world.getBlockState(pos).getBlock() != null) {
                    name = mc.world.getBlockState(pos).getBlock().getLocalizedName();
                }
                else name = "error";
            }
            return name;
        }
    }

    BlockPos basePos;

    public ArrayList<DefDataStruct> DefDataList;

    public DefenceDataToPlayer () {
        TdmMod.logger.info("loaded clientside");
    }

    public DefenceDataToPlayer (BlockPos pos, int playerEntityId) {
        basePos = pos;
        
        DefDataList = new ArrayList<DefDataStruct>();
        for (IDefenceSystem sys : InWorldBasesManager.GetInfo(basePos).defenders) {

            int ammoCount = 0, chargeStatus = 3;
            if (sys instanceof IHasEnergy) {
                IHasEnergy enrg = (IHasEnergy)sys;
                chargeStatus = (enrg.getEnergy() > enrg.getConsumptionPerTick()) ? 1 : 0;
            }
            if (sys instanceof IHasAmmo) {
                IHasAmmo amm = (IHasAmmo)sys;
                ammoCount = amm.getAmmo();
            }

            BlockPos defPos = sys.getPosForBase();

            DefDataList.add(new DefDataStruct(defPos, ammoCount, chargeStatus));
        }

        TdmMod.logger.info("serversidedone");
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int count = buf.readInt();
        DefDataList = new ArrayList<DefDataStruct>();
        for (int i = 0; i < count; i++ ) {
            DefDataList.add(ReadStructFromByteBuf(buf));
        }

        BaseGui.InsertDefenceData(DefDataList);
        TdmMod.logger.info("loaded clientside2");
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeInt(DefDataList.size());
        for (DefDataStruct struct : DefDataList) {
            WriteStructToByteBuf(buf, struct);
            TdmMod.logger.info(struct.pos);
        }
    }

    private void WriteStructToByteBuf (ByteBuf buf, DefDataStruct struct) {

        buf.writeInt(struct.pos.getX());
        buf.writeInt(struct.pos.getY());
        buf.writeInt(struct.pos.getZ());
        buf.writeInt(struct.ammoCount);
        buf.writeInt(struct.chargeStatus);

    }

    private DefDataStruct ReadStructFromByteBuf (ByteBuf buf) {

        BlockPos pos = new BlockPos(buf.readInt(),buf.readInt(),buf.readInt());
        int ammoCount = buf.readInt();
        int chargeStatus = buf.readInt();

        TdmMod.logger.info(pos);
        return new DefDataStruct(pos, ammoCount, chargeStatus);
    }

    @Override
    public IBasicMessage getResponse() {
        return null;
    }
    
}
