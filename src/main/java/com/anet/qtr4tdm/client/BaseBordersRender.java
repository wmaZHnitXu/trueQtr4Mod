package com.anet.qtr4tdm.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.bases.baseInfo;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = TdmMod.MODID)
public class BaseBordersRender {

    public static final ResourceLocation whiteTexture = new ResourceLocation(TdmMod.MODID, "textures/particle/white.png");

    private static HashMap<Integer, baseInfo> borders;
    private static EntityPlayerSP player;
    private static HashMap<ChunkPos, int[]> heightmaps;
    private static HashMap<ChunkPos, boolean[]> sides;
    private static float motionOffset;
    //private static baseInfo lastRendered; Сомнительно, а чё если их несколько рендерится одновременно (ес чё это надо чтобы обновлялись высоты.)

    static {
        borders = new HashMap<Integer, baseInfo>();
        heightmaps = new HashMap<ChunkPos, int[]>();
        sides = new HashMap<ChunkPos, boolean[]>();
    }
    
    public static void InsertBorderData (baseInfo shadow) {

        if (!borders.containsKey(shadow.id) || borders.get(shadow.id).chunks.length != shadow.chunks.length) {
            CheckBaseChunkSides(shadow);
        }

        //updateHeightMapOfBase(shadow);

        borders.put(shadow.id, shadow);

        for (ChunkPos pos : shadow.chunks) {
            if (!heightmaps.containsKey(pos)) {
                heightmaps.put(pos, Minecraft.getMinecraft().world.getChunkFromChunkCoords(pos.x, pos.z).getHeightMap());
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private static void updateHeightMapOfBase (baseInfo shadow) {

        World world = Minecraft.getMinecraft().world;

        for (ChunkPos pos : shadow.chunks) {
            if (isChunkSided(pos))
                heightmaps.put(pos, world.getChunkFromChunkCoords(pos.x, pos.z).getHeightMap());
        }

    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void RenderBaseBorders (RenderWorldLastEvent evt) {
        if (borders != null) {
            motionOffset += evt.getPartialTicks() * 0.04d;
            if (motionOffset > 2) while (motionOffset > 2) motionOffset-=2;
            player = Minecraft.getMinecraft().player;
            for (baseInfo border : borders.values()) {
                ChunkPos playerPos = new ChunkPos(player.getPosition());
                if (Vector2Distance(playerPos, border.chunks[0]) < 5) {
                    RenderBorder(border, evt.getPartialTicks());
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private static void RenderBorder (baseInfo border, float partialTicks) {
        for (ChunkPos pos : border.chunks) {
            if (isChunkSided(pos)) {
                boolean[] bools = sides.get(pos);
                RenderChunk(pos, bools[0], bools[1], bools[2], bools[3], partialTicks);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private static int getHeightFromHeightMap (int x, int z, int[] heightmap, ChunkPos pos) { //coords in chunk
        x -= pos.getXStart();
        z -= pos.getZStart();
        return heightmap[x + z * 16];
    }

    @SideOnly(Side.CLIENT)
    private static boolean isChunkSided (ChunkPos pos) {
        boolean[] bools = sides.get(pos);
        return bools[0] || bools[1] || bools[2] || bools[3];
    }

    @SideOnly(Side.CLIENT)
    private static void CheckBaseChunkSides (baseInfo border) {

        for (ChunkPos pos : border.chunks) {
            boolean x_plus = true; boolean x_minus = true; boolean z_plus = true; boolean z_minus = true; //true = render, false = не render
            ChunkPos xpluschunk = new ChunkPos(pos.x + 1, pos.z);
            for (int i = 0; i < border.chunks.length; i++) {
                if (border.chunks[i].equals(xpluschunk)) {
                    x_plus = false; break;
                }
            }
            ChunkPos xminuschunk = new ChunkPos(pos.x - 1, pos.z);
            for (int i = 0; i < border.chunks.length; i++) {
                if (border.chunks[i].equals(xminuschunk)) {
                    x_minus = false; break;
                }
            }
            ChunkPos zpluschunk = new ChunkPos(pos.x, pos.z + 1);
            for (int i = 0; i < border.chunks.length; i++) {
                if (border.chunks[i].equals(zpluschunk)) {
                    z_plus = false; break;
                }
            }
            ChunkPos zminuschunk = new ChunkPos(pos.x, pos.z - 1);
            for (int i = 0; i < border.chunks.length; i++) {
                if (border.chunks[i].equals(zminuschunk)) {
                    z_minus = false; break;
                }
            }
            sides.put(pos, new boolean[]{x_plus, x_minus, z_minus, z_plus});
        }
    }

    @SideOnly(Side.CLIENT)
    private static void RenderChunk (ChunkPos pos, boolean x_plus, boolean x_minus, boolean z_plus, boolean z_minus, float partialTicks) {

        final Entity viewEntity = Minecraft.getMinecraft().getRenderViewEntity();
        Minecraft.getMinecraft().getTextureManager().bindTexture(whiteTexture);
        if (viewEntity == null)
            return;
        Vec3d viewPos = new Vec3d(viewEntity.prevPosX + (viewEntity.posX - viewEntity.prevPosX) * partialTicks,
                viewEntity.prevPosY + (viewEntity.posY - viewEntity.prevPosY) * partialTicks,
                viewEntity.prevPosZ + (viewEntity.posZ - viewEntity.prevPosZ) * partialTicks);


        float lightMapSaveX = OpenGlHelper.lastBrightnessX;
        float lightMapSaveY = OpenGlHelper.lastBrightnessY;
        
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 0.0F);
        GlStateManager.color(1, 1, 1, 1f);

        GlStateManager.pushMatrix();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();

        vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        if (x_plus) {

            int x = pos.getXEnd();
            for (Double z = pos.getZStart() + Float.valueOf(motionOffset).doubleValue(); z <= pos.getZEnd() + motionOffset; z+=2) {
                boolean last = false;

                float width = 0.2f;
                float depth = 0.2f;

                if (z > pos.getZEnd() + 2 - depth) {z -= 16; last = true;}

                //int height = getHeightFromHeightMap(x, z, heightmaps.get(pos), pos);
                
                double y = Minecraft.getMinecraft().world.getHeight(x, Double.valueOf(z + depth * 0.5f).intValue()) + 0.1d;

                Vec3d pos0 = new Vec3d(x - viewPos.x, y - viewPos.y, z - viewPos.z);
                Vec3d pos1 = new Vec3d(x - viewPos.x, y - viewPos.y, z - viewPos.z);
                
                vertexbuffer.pos(pos0.x + 1, pos0.y, pos0.z + depth)
                        .tex(0.0D, 1D).endVertex();
                vertexbuffer.pos(pos0.x + 1, pos0.y, pos0.z)
                        .tex(0.0D, 0D).endVertex();
                vertexbuffer.pos(pos1.x + 1 - width, pos1.y, pos1.z)
                        .tex(1.0D, 0D).endVertex();
                vertexbuffer.pos(pos1.x + 1 - width, pos1.y, pos1.z + depth)
                        .tex(1D, 1D).endVertex();

                if (last) break;

            }

        }
        if (x_minus) {

            int x = pos.getXStart();
            for (Double z = pos.getZStart() - Float.valueOf(motionOffset).doubleValue(); z <= pos.getZEnd() + motionOffset; z+=2) {
                boolean last = false;

                float width = 0.2f;
                float depth = 0.2f;

                if (z < pos.getZStart() - 2 - depth) {z += 16; last = true;}

                //int height = getHeightFromHeightMap(x, z, heightmaps.get(pos), pos);
                
                double y = Minecraft.getMinecraft().world.getHeight(x, Double.valueOf(z + depth * 0.5f).intValue()) + 0.1d;

                Vec3d pos0 = new Vec3d(x - viewPos.x, y - viewPos.y, z - viewPos.z);
                Vec3d pos1 = new Vec3d(x - viewPos.x, y - viewPos.y, z - viewPos.z);
                
                vertexbuffer.pos(pos0.x, pos0.y, pos0.z + depth)
                        .tex(0.0D, 1D).endVertex();
                vertexbuffer.pos(pos0.x, pos0.y, pos0.z)
                        .tex(0.0D, 0D).endVertex();
                vertexbuffer.pos(pos1.x + width, pos1.y, pos1.z)
                        .tex(1.0D, 0D).endVertex();
                vertexbuffer.pos(pos1.x + width, pos1.y, pos1.z + depth)
                        .tex(1D, 1D).endVertex();

                if (last) z = pos.getZStart() - Float.valueOf(motionOffset).doubleValue();
            }

        }

        if (z_plus) {

            int z = pos.getZStart();
            for (Double x = pos.getXStart() - Float.valueOf(motionOffset).doubleValue(); x <= pos.getXEnd() + motionOffset; x+=2) {
                boolean last = false;

                float width = 0.2f;
                float depth = 0.2f;

                if (x > pos.getXEnd() + 2 + depth) {x -= 16; last = true;}

                //int height = getHeightFromHeightMap(x, z, heightmaps.get(pos), pos);
                
                double y = Minecraft.getMinecraft().world.getHeight(Double.valueOf(x + depth * 0.5f).intValue(), z) + 0.1d;

                Vec3d pos0 = new Vec3d(x - viewPos.x, y - viewPos.y, z - viewPos.z);
                Vec3d pos1 = new Vec3d(x - viewPos.x, y - viewPos.y, z - viewPos.z);
                
                vertexbuffer.pos(pos0.x + 1, pos0.y, pos0.z + depth)
                        .tex(0.0D, 1D).endVertex();
                vertexbuffer.pos(pos0.x + 1, pos0.y, pos0.z)
                        .tex(0.0D, 0D).endVertex();
                vertexbuffer.pos(pos1.x + 1 - width, pos1.y, pos1.z)
                        .tex(1.0D, 0D).endVertex();
                vertexbuffer.pos(pos1.x + 1 - width, pos1.y, pos1.z + depth)
                        .tex(1D, 1D).endVertex();

                if (last) break;
            }

        }
        if (z_minus) {

            int z = pos.getZEnd();
            for (Double x = pos.getXStart() - Float.valueOf(motionOffset).doubleValue() + 1; x <= pos.getXEnd() - motionOffset + 1; x+=2) {
                boolean last = false;

                float width = 0.2f;
                float depth = 0.2f;

                if (x > pos.getXEnd() + 2 - depth) {x -= 16; last = true;}

                //int height = getHeightFromHeightMap(x, z, heightmaps.get(pos), pos);
                
                double y = Minecraft.getMinecraft().world.getHeight(Double.valueOf(x + depth * 0.5f).intValue(), z) + 0.1d;

                Vec3d pos0 = new Vec3d(x - viewPos.x, y - viewPos.y, z - viewPos.z);
                Vec3d pos1 = new Vec3d(x - viewPos.x, y - viewPos.y, z - viewPos.z);
                
                vertexbuffer.pos(pos0.x + 1, pos0.y, pos0.z + 1 - depth)
                        .tex(0.0D, 1D).endVertex();
                vertexbuffer.pos(pos0.x + 1, pos0.y, pos0.z + 1)
                        .tex(0.0D, 0D).endVertex();
                vertexbuffer.pos(pos1.x + 1 - width, pos1.y, pos1.z + 1)
                        .tex(1.0D, 0D).endVertex();
                vertexbuffer.pos(pos1.x + 1 - width, pos1.y, pos1.z + 1 - depth)
                        .tex(1D, 1D).endVertex();

                if (last) x = pos.getXStart() - Float.valueOf(motionOffset).doubleValue();
            }
        }

        tessellator.draw();

        GlStateManager.popMatrix();

        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    private static double Vector2Distance (ChunkPos pos1, ChunkPos pos2) {
        return Math.sqrt((pos2.x - pos1.x) * (pos2.x - pos1.x) + (pos2.z - pos1.z) * (pos2.z - pos1.z));
    }

}
