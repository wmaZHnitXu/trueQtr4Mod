package com.anet.qtr4tdm.client;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.entities.LaserTurretEntity;
import com.anet.qtr4tdm.common.entities.render.RenderLaserTurret;
import com.anet.qtr4tdm.uebki.messages.primitive.TurretRotationMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber(modid = TdmMod.MODID)
public class LaserTurretEntityRenderSupport {

    public final static LaserTurretEntityRenderSupport instance = new LaserTurretEntityRenderSupport();

    public final static HashMap<Integer, TurretHelpInfo> turrets = new HashMap<Integer, TurretHelpInfo>();
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onRenderWorldLastEvent (RenderWorldLastEvent evt) {
        if (turrets.isEmpty()) return;
        ArrayList<TurretHelpInfo> infs = new ArrayList<TurretHelpInfo>(turrets.values());
        for (int i = 0; i < infs.size(); i++) {
            TurretHelpInfo turret = infs.get(0);
            turret.needHelp7(evt.getPartialTicks());
            turret.decreaseLifetime();
            turret.resetRendered();
        }
    }

    public static void sddmitRenderedByItself (LaserTurretEntity entity) {
        int id = entity.getEntityId();
        if (turrets.containsKey(id)) {
            turrets.get(id).submitRendered();
        }
    }

    public static void messageRecieved (TurretRotationMessage message, LaserTurretEntity entity) {
        instance.processMessage(message, entity);
    }

    public void processMessage (TurretRotationMessage message, LaserTurretEntity entity) {
        if (turrets.containsKey(message.entityId)) {
            turrets.get(message.entityId).resetLifetime();
        }
        else {
            turrets.put(message.entityId, new TurretHelpInfo(message, entity));
        }
    }

    class TurretHelpInfo {
        TurretRotationMessage message;
        LaserTurretEntity entity;
        int lifetime;
        boolean renderedByItself;

        TurretHelpInfo (TurretRotationMessage message, LaserTurretEntity entity) {
            this.message = message;
            this.entity = entity;
            lifetime = 60;
            renderedByItself = false;
        }

        public void needHelp7 (float partialTicks) {
            if (entity == null || entity.isDead) {
                turrets.remove(this.message.entityId);
                return;
            }
            //if (!renderedByItself) { хз и так норм
                RenderLaserTurret.doLaserBeamRender(entity, partialTicks);
            //}
        }

        public void decreaseLifetime () {
            lifetime--;
            if (lifetime <= 0) {
                turrets.remove(this.message.entityId);
            }
        }

        public void resetLifetime () {
            lifetime = 60;
        }

        public void submitRendered () {
            renderedByItself = true;
        }

        public void resetRendered () {
            renderedByItself = false;
        }
    }

}
