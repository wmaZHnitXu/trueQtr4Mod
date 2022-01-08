package com.anet.qtr4tdm.common.entities.render;

import java.util.ArrayList;
import com.anet.qtr4tdm.TdmMod;

import org.lwjgl.opengl.GL11;

import akka.actor.Kill;
import io.vavr.collection.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = TdmMod.MODID)
public class Trail
{

    public static final ResourceLocation whiteTexture = new ResourceLocation(TdmMod.MODID, "textures/particle/white.png");

    public int MAX_LENGTH = 10;
    public static final float SPAWN_INTERVAL = 1;

    private Minecraft mc;
    private Entity trackedEntity;
    private TrailNode[] nodes;
    private float spawnCooldown = 0;
    private float thickness;
    private int Lifetime;
    private boolean isFlame;

    private static ArrayList<Trail> trails;
    static {
        trails = new ArrayList<Trail>();
    }

    public Trail(Entity entity, float thickNess, int length, float spawnInterval, boolean isFlame)
    {
        this.mc = Minecraft.getMinecraft();
        this.trackedEntity = entity;
        this.spawnCooldown = spawnInterval;
        this.MAX_LENGTH = length;
        this.nodes = new TrailNode[MAX_LENGTH];
        this.thickness = thickNess;
        this.isFlame = isFlame;
        Lifetime = 200;

        resetNodes();
    }

    public static void TrailOn (Entity entity, float thickNess, int length, float spawnInterval, boolean isFlame) {
        trails.add(new Trail(entity, thickNess, length, spawnInterval, isFlame));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        for (int i = 0; i < trails.size(); i++) {
            Trail t = trails.get(i);
            t.render(0, 0, 0, event.getPartialTicks());
            t.onRenderTick();
            if (!trails.contains(t)) i--;
        }
    }

    public void onRenderTick()
    {
        spawnCooldown += 0.5f;
        if (trackedEntity == null || trackedEntity.isDead) {
            Lifetime--;
        }
        if (Lifetime <= 0) Kill();
    }

    private void Kill () {
        trails.remove(this);
    }

    public void render(double x, double y, double z, float partialTicks)
    {
        if (this.spawnCooldown > 40)
        {
            this.spawnCooldown = 0;
            resetNodes();
        }

        while (this.spawnCooldown >= SPAWN_INTERVAL)
        {
            for (int i = MAX_LENGTH - 1; i > 0; i--)
            {
                nodes[i].moveTo(nodes[i - 1]);
            }
            nodes[0].moveTo(trackedEntity);
            this.spawnCooldown -= SPAWN_INTERVAL;
        }

        renderNodes(partialTicks);
    }

    public void resetNodes()
    {
        for (int i = 0; i < MAX_LENGTH; i++)
            this.nodes[i] = new TrailNode(trackedEntity);
    }

    public void renderNodes(float partialTicks)
    {
        final Entity viewEntity = Minecraft.getMinecraft().getRenderViewEntity();
        Minecraft.getMinecraft().getTextureManager().bindTexture(whiteTexture);
        if (viewEntity == null)
            return;
        Vec3d viewPos = new Vec3d(viewEntity.prevPosX + (viewEntity.posX - viewEntity.prevPosX) * partialTicks,
                viewEntity.prevPosY + (viewEntity.posY - viewEntity.prevPosY) * partialTicks,
                viewEntity.prevPosZ + (viewEntity.posZ - viewEntity.prevPosZ) * partialTicks);
        GlStateManager.pushMatrix();

        float lightMapSaveX = OpenGlHelper.lastBrightnessX;
        float lightMapSaveY = OpenGlHelper.lastBrightnessY;

        if (isFlame) {
            GlStateManager.color(1.0F, 1.0F, 0.7F, 1.0F);
            GlStateManager.disableBlend();
            GlStateManager.disableAlpha();
            GlStateManager.disableLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 0.0F);
        }
        else {
            GlStateManager.color(0.7F, 0.7F, 0.7F, 0.7F);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
        }
        
        
        
        GlStateManager.disableCull();
        GlStateManager.pushMatrix();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        for (int i = 1; i < MAX_LENGTH; i++)
        {
            TrailNode node0 = nodes[i - 1];
            TrailNode node1 = nodes[i];

            Vec3d pos0 = new Vec3d(node0.x - viewPos.x, node0.y - viewPos.y, node0.z - viewPos.z);
            Vec3d pos1 = new Vec3d(node1.x - viewPos.x, node1.y - viewPos.y, node1.z - viewPos.z);
            float scale0 = ((float) (MAX_LENGTH - i)) / MAX_LENGTH * thickness;
            float scale1 = ((float) MAX_LENGTH - i - 1.0f) / MAX_LENGTH * thickness;
            /*if (i == 1)
            {
                scale1 = 0;
            }
            */
            final Vec3f up0 = node0.up;
            final Vec3f right0 = node0.right;
            final Vec3f up1 = node1.up;
            final Vec3f right1 = node1.right;

            vertexbuffer
                    .pos(pos0.x + (-right0.x) * scale0, pos0.y + (-right0.y) * scale0, pos0.z + (-right0.z) * scale0)
                    .tex(0.0D, 1D).endVertex();
            vertexbuffer.pos(pos0.x + (right0.x) * scale0, pos0.y + (right0.y) * scale0, pos0.z + (right0.z) * scale0)
                    .tex(0.0D, 0D).endVertex();
            vertexbuffer.pos(pos1.x + (right1.x) * scale1, pos1.y + (right1.y) * scale1, pos1.z + (right1.z) * scale1)
                    .tex(1.0D, 0D).endVertex();
            vertexbuffer
                    .pos(pos1.x + (-right1.x) * scale1, pos1.y + (-right1.y) * scale1, pos1.z + (-right1.z) * scale1)
                    .tex(1D, 1D).endVertex();

            vertexbuffer.pos(pos0.x + (-up0.x) * scale0, pos0.y + (-up0.y) * scale0, pos0.z + (-up0.z) * scale0)
                    .tex(0.0D, 1D).endVertex();
            vertexbuffer.pos(pos0.x + (up0.x) * scale0, pos0.y + (up0.y) * scale0, pos0.z + (up0.z) * scale0)
                    .tex(1D, 0D).endVertex();
            vertexbuffer.pos(pos1.x + (up1.x) * scale1, pos1.y + (up1.y) * scale1, pos1.z + (up1.z) * scale1)
                    .tex(1D, 1D).endVertex();
            vertexbuffer.pos(pos1.x + (-up1.x) * scale1, pos1.y + (-up1.y) * scale1, pos1.z + (-up1.z) * scale1)
                    .tex(0.0D, 0D).endVertex();
        }
        tessellator.draw();


        
        
        GlStateManager.popMatrix();
        
        if (isFlame) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.enableLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
        }

        GlStateManager.enableCull();

        GlStateManager.popMatrix();
    }

    /**
     * Used for debugging.
     * @param x
     * @param y
     * @param z
     */
    public void renderAxis(double x, double y, double z)
    {
        Vec3d forward = trackedEntity.getForward();
        forward = new Vec3d(-forward.x, -forward.y, forward.z);
        Vec3d up = Vec3d.fromPitchYaw(trackedEntity.rotationPitch + 90.0f, trackedEntity.rotationYaw);
        up = new Vec3d(-up.x, -up.y, up.z);
        Vec3d right = forward.crossProduct(up);

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.depthFunc(GL11.GL_ALWAYS);
        GlStateManager.translate(x, y, z);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        GlStateManager.color(1, 0, 0, 1);
        vertexbuffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.15625D).endVertex();
        vertexbuffer.pos(right.x, right.y, right.z).tex(0.15625D, 0.15625D).endVertex();
        tessellator.draw();
        GlStateManager.color(0, 1, 0, 1);
        vertexbuffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.15625D).endVertex();
        vertexbuffer.pos(up.x, up.y, up.z).tex(0.15625D, 0.15625D).endVertex();
        tessellator.draw();
        GlStateManager.color(0, 0, 1, 1);
        vertexbuffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.15625D).endVertex();
        vertexbuffer.pos(forward.x, forward.y, forward.z).tex(0.15625D, 0.15625D).endVertex();
        tessellator.draw();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    public boolean shouldBeRemoved()
    {
        return mc.world == null || trackedEntity.isDead;
    }

    class TrailNode
    {

        public double x;
        public double y;
        public double z;

        public final Vec3f up;
        public final Vec3f right;

        TrailNode(Entity entity)
        {
            this.up = new Vec3f();
            this.right = new Vec3f();

            this.moveTo(entity);
        }

        public void moveTo(TrailNode trailNode)
        {
            this.x = trailNode.x;
            this.y = trailNode.y;
            this.z = trailNode.z;
            this.up.set(trailNode.up);
            this.right.set(trailNode.right);
        }

        public void moveTo(Entity entity)
        {
            if (entity == null) return;
            this.x = entity.posX;
            this.y = entity.posY;
            this.z = entity.posZ;

            final Vec3d forward = entity.getForward();
            final Vec3d up = Vec3d.fromPitchYaw(entity.rotationPitch + 90F, entity.rotationYaw);

            this.up.set((float) -up.x, (float) -up.y, (float) up.z);

            cross(
                    (float) -forward.x, (float) -forward.y, (float) forward.z,
                    this.up.x, this.up.y, this.up.z, this.right);
        }

        public Vec3f cross(float lx, float ly, float lz, float rx, float ry, float rz, Vec3f dest)
        {
            dest.set(
                    ly * rz - lz * ry,
                    rx * lz - rz * lx,
                    lx * ry - ly * rx
            );

            return dest;
        }
    }

    class Vec3f {
        float x, y, z;

        public float getX () {return x;};
        public float getY () {return y;};
        public float getZ () {return z;};

        public Vec3f () {

        }

        public void set (Vec3f v) {
            x = v.x;
            y = v.y;
            z = v.z;
        }

        public void set (float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
