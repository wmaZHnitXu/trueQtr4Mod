package com.anet.qtr4tdm.uebki.gui;

import java.util.ArrayList;
import java.util.List;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.bases.baseInfo;
import com.anet.qtr4tdm.common.supers.DroneSmallEntity;
import com.anet.qtr4tdm.common.supers.TEDefenceInvEnrg;
import com.anet.qtr4tdm.common.tiles.BaseTile;
import com.anet.qtr4tdm.common.tiles.DroneBaseTile;
import com.anet.qtr4tdm.common.tiles.Kaz1Tile;
import com.anet.qtr4tdm.common.tiles.TurretMasterTe;
import com.anet.qtr4tdm.uebki.IDSmanager;
import com.anet.qtr4tdm.uebki.gui.KAZGuiMisc.kazContainer;
import com.anet.qtr4tdm.uebki.gui.TEDefInvEnrgGuiMisc.TEDefContainer;
import com.anet.qtr4tdm.uebki.gui.baseGuiMisc.BaseContainer;
import com.anet.qtr4tdm.uebki.messages.primitive.baseInfoMessage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    public class Session { baseInfo base; int playerId; public Session (baseInfo b, int p) {base = b; playerId = p;}}

    public static ArrayList<Session> sessions;

    public GuiHandler () {
        super();
        sessions = new ArrayList<Session>();
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case TdmMod.GUI_BASE:
                BaseTile tile = (BaseTile)world.getTileEntity(new BlockPos(x,y,z));
                TdmMod.wrapper.sendTo(new baseInfoMessage(tile.GetDirectInfo()), (EntityPlayerMP)player);
                sessions.add(new Session(tile.GetDirectInfo(), IDSmanager.GetPlayerId(player)));
                tile.GetDirectInfo().container = new BaseContainer(player.inventory, tile);
                return tile.GetDirectInfo().container;
            case TdmMod.GUI_KAZ:
                Kaz1Tile kaz = (Kaz1Tile)world.getTileEntity(new BlockPos(x,y,z));
                kaz.container = new kazContainer(player.inventory, kaz);
                return kaz.container;
            case TdmMod.GUI_COMMONDEF:
                TEDefenceInvEnrg def = (TEDefenceInvEnrg)world.getTileEntity(new BlockPos(x,y,z));
                def.container = new TEDefContainer(player.inventory, def);
                return def.container;
            case TdmMod.GUI_DRONEHARVESTER:
                DroneBaseTile droneBaseTile = (DroneBaseTile)world.getTileEntity(new BlockPos(x,y,z));
                return droneBaseTile.container = new DroneContainer(player.inventory, droneBaseTile.GetDrone(), droneBaseTile);
            case TdmMod.GUI_TURRETMASTER:
                TurretMasterTe turretBaseTile = (TurretMasterTe)world.getTileEntity(new BlockPos(x,y,z));
                return turretBaseTile.conatainer = new TurretConatainer(player.inventory, turretBaseTile);
        }
        return null;
    }

    public static void UpdateBaseGuis () {
        for (Session session : sessions) {
            EntityPlayer player = IDSmanager.GetPlayer(session.playerId);
            baseInfo base = session.base;
            if (player != null && base != null) {
                TdmMod.wrapper.sendTo(new baseInfoMessage(base), (EntityPlayerMP)player);
            }
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case TdmMod.GUI_BASE:
                return new BaseGui(player.inventory, (BaseTile)world.getTileEntity(new BlockPos(x,y,z)));
            case TdmMod.GUI_KAZ:
                return new KazGui(player.inventory, (Kaz1Tile)world.getTileEntity(new BlockPos(x,y,z)));
            case TdmMod.GUI_COMMONDEF:
                return new TEDefInvEnrgGui(player.inventory, (TEDefenceInvEnrg)world.getTileEntity(new BlockPos(x,y,z)));
            case TdmMod.GUI_DRONEHARVESTER:
                DroneBaseTile tile = (DroneBaseTile)world.getTileEntity(new BlockPos(x,y,z));
                List<DroneSmallEntity> drones = tile.getWorld()
                .getEntitiesWithinAABB(DroneSmallEntity.class, new AxisAlignedBB(tile.getPos().west().south(), tile.getPos().up(2).east().north()));
                DroneSmallEntity drone = null;
                if (drones.size() > 0) drone = drones.get(0);
                return new DroneBaseGui(player.inventory, drone, tile);
            case TdmMod.GUI_TURRETMASTER:
            return new TurretGui(player.inventory, (TurretMasterTe)world.getTileEntity(new BlockPos(x,y,z)));
        }
        return null;
    }
    
}
