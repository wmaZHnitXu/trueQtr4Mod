package com.anet.qtr4tdm.uebki.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.bases.baseInfo;
import com.anet.qtr4tdm.common.tiles.BaseTile;
import com.anet.qtr4tdm.uebki.gui.baseGuiMisc.BaseContainer;

import it.unimi.dsi.fastutil.chars.CharIterable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import scala.collection.script.Update;

public class BaseGui extends GuiContainer {

    public final BaseTile te;
    public final InventoryPlayer player;
    private final ResourceLocation background = new ResourceLocation(TdmMod.MODID, "textures/gui/furnace.png");

    private static baseInfo info;
    private static DynamicTexture mapTex;
    private ResourceLocation mapTexLocation;
    private int xOffset;
    private int yOffset;
    private static final HashMap<Block, Integer> colors = new HashMap<>();
    private ArrayList<ChunkPos> selectedChunks; 
    private ChunkPos startchunk;
    
    
    public BaseGui(InventoryPlayer playerInventory, BaseTile baseTe) {
        super(new BaseContainer(playerInventory, baseTe));
        player = playerInventory;
        te = baseTe;
        LoadMap();
        selectedChunks = new ArrayList<ChunkPos>();
        UpdateMap(1);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        mc.renderEngine.bindTexture(mapTexLocation);
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
            drawScaledCustomSizeModalRect(-81, -50, 0, 0, 208, 208, 208, 208, 208, 208);

            //DRAW MARKED CHUNKS
            GlStateManager.pushMatrix();
                if (info != null && info.chunks != null)
                    for (ChunkPos pos : info.chunks) {
                        int localx = pos.x - (startchunk.x - 6);
                        int localy = pos.z - (startchunk.z - 6);
                        drawRect(-81 + localx * 16, -50 + localy * 16, -81 + localx * 16 + 16, -50 + localy * 16 + 16, 0x9999FF99);
                    }

                for (ChunkPos pos : selectedChunks) {
                    int localx = pos.x - (startchunk.x - 6);
                    int localy = pos.z - (startchunk.z - 6);
                    drawRect(-81 + localx * 16, -50 + localy * 16, -81 + localx * 16 + 1, -50 + localy * 16 + 1, 0xFFFFFFFF);
                    drawRect(-81 + localx * 16 + 15, -50 + localy * 16 + 15, -81 + localx * 16 + 16, -50 + localy * 16 + 16,0xFFFFFFFF);
                    drawRect(-81 + localx * 16, -50 + localy * 16 + 15, -81 + localx * 16 + 1, -50 + localy * 16 + 16, 0xFFFFFFFF);
                    drawRect(-81 + localx * 16 + 15, -50 + localy * 16, -81 + localx * 16 + 16, -50 + localy * 16 + 1, 0xFFFFFFFF);
                    drawRect(-81 + localx * 16, -50 + localy * 16, -81 + localx * 16 + 16, -50 + localy * 16 + 16, 0x77777777);
                }
            GlStateManager.popMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.popMatrix();
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        mc.renderEngine.bindTexture(background);
        drawScaledCustomSizeModalRect(guiLeft - 100, guiTop - 70, 0, 0, 365, 329, 365, 329, 512, 512);
        //this.drawTexturedModalRect(,  - 70, 0, 0, 365, 329);
    }
    
    public void LoadMap () {
        mapTex = new DynamicTexture(208, 208);
        final int[] mapTextureData = mapTex.getTextureData();
        for (int i = 0; i < mapTextureData.length; i++)
            mapTextureData[i] = 0x11111111;
        mapTexLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(TdmMod.MODID + "_basemap_texture", mapTex);
        mapTex.updateDynamicTexture();
    }

    public void UpdateMap (int size) {
        if (size == 0) size = 1;
        final int[] mapTextureData = mapTex.getTextureData();
        World world = te.getWorld();
        startchunk = new ChunkPos(te.getPos());
        int startx = startchunk.x - 6;
        int startz = startchunk.z - 6;

        HashMap<ChunkPos, int[]> cachedChunks = new HashMap<>();

        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                Chunk workChunk = world.getChunkFromChunkCoords(startx + i, startz + j);
                int[] hmap = workChunk.getHeightMap();
                for (int h = 0; h < hmap.length; h++) {

                    IBlockState state = workChunk.getBlockState(h % 16, hmap[h]-1, h / 16);
                    Block block = state.getBlock();

                    int color = 0xFF3D8B4D;
                    if (colors.containsKey(block)) color = colors.get(block);
                    int xW = h % 16;
                    int zN = h / 16;
                    BlockPos normalBlockCoords = workChunk.getPos().getBlock(xW, 0, zN);
                    int northHeight = world.getHeight(normalBlockCoords.north()).getY()-1;
                    int westHeight = world.getHeight(normalBlockCoords.west()).getY()-1;
                    int southHeight = world.getHeight(normalBlockCoords.south()).getY()-1;
                    int eastHeight = world.getHeight(normalBlockCoords.east()).getY()-1;
                    int light = 255 - Math.max(((northHeight + westHeight + southHeight + eastHeight) / 2 - hmap[h]-1) * 40, 0);
                    color = color - 0xFF000000 + light * 16777216;

                    mapTextureData[j * 208 * 16 + i * 16 + h % 16 + (h / 16 * 208)] = color;
                }
                cachedChunks.put(workChunk.getPos(), hmap);
            }
        }
        mapTex.updateDynamicTexture();

        /* 
        int j = 0;
        for (int i = 0; i < 16384; ++i) {
            if (i % 128 == 0 && i != 0) j++;

            int color = 0;
            World world = te.getWorld();
            chariot = startpos.add(new BlockPos(i % 128, 0, j));
            chariot = world.getHeight(chariot).down();
            int blockHeight = chariot.getY();
            IBlockState state = world.getBlockState(chariot);
            Block block = state.getBlock();
            //if (colors.containsKey(block)) color = colors.get(block);
            if (block.equals(Blocks.WATER)) color = 0xFF000000;
            else { if (colors.containsKey(block)) color = 0xFF555555;
                if (block.equals(Blocks.REDSTONE_TORCH)) {
                    color = 0xFFFF0000;
                }
            }
            int westHeight = world.getHeight(chariot.west()).getY();
            int northHeight = world.getHeight(chariot.north()).getY();
            int light = 255 - Math.max(((northHeight + westHeight) / 2 - blockHeight) * 40, 0);
            color = color - 0xFF000000 + light * 16777216;
            
            mapTextureData[i] = color;
            mapTex.updateDynamicTexture();
        }
        */
    }

    @Override
    public void initGui() {
        xOffset = width / 2 - xSize / 2;
        yOffset = height / 2 - ySize / 2;
        super.initGui();
    }

    @Override
    public int getXSize() {
        return 700;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseX > guiLeft - 81 && mouseX < guiLeft - 81 + 208 && mouseY > guiTop - 50 && mouseY < guiTop - 50 + 208) {
            int onMapX = mouseX - (guiLeft - 81);
            int onmapY = mouseY - (guiTop - 50);
            ChunkPos pos = new ChunkPos(startchunk.x - 6 + onMapX / 16, startchunk.z - 6 + onmapY / 16);
            if (selectedChunks.contains(pos)) selectedChunks.remove(pos);
            else {
                selectedChunks.add(pos);
            }
        } 
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public static void InsertInfo (baseInfo infos) {
        info = infos;
    }

    enum DyeColor {
        WHITE(0, "white", 16383998, 15790320, 16777215),
        ORANGE(1, "orange", 16351261, 15435844, 16738335),
        MAGENTA(2, "magenta", 13061821, 12801229, 16711935),
        LIGHT_BLUE(3, "light_blue", 3847130, 6719955, 10141901),
        YELLOW(4, "yellow", 16701501, 14602026, 16776960),
        LIME(5, "lime", 8439583, 4312372, 12582656),
        PINK(6, "pink", 15961002, 14188952, 16738740),
        GRAY(7, "gray", 4673362, 4408131, 8421504),
        LIGHT_GRAY(8, "light_gray", 10329495, 11250603, 13882323),
        CYAN(9, "cyan", 1481884, 2651799, 65535),
        PURPLE(10, "purple", 8991416, 8073150, 10494192),
        BLUE(11, "blue", 3949738, 2437522, 255),
        BROWN(12, "brown", 8606770, 5320730, 9127187),
        GREEN(13, "green", 6192150, 3887386, 65280),
        RED(14, "red", 11546150, 11743532, 16711680),
        BLACK(15, "black", 1908001, 1973019, 0);

        private final int id;
        private final String translationKey;
        private final int colorValue;
        private final int swappedColorValue;
        private final float[] colorComponentValues;
        private final int fireworkColor;
        private final int textColor;

        private DyeColor(int idIn, String translationKeyIn, int colorValueIn, int fireworkColorIn, int textColorIn) {
            this.id = idIn;
            this.translationKey = translationKeyIn;
            this.colorValue = colorValueIn + 0xFF000000;
            this.textColor = textColorIn;
            int i = (colorValueIn & 16711680) >> 16;
            int j = (colorValueIn & '\uff00') >> 8;
            int k = (colorValueIn & 255) >> 0;
            this.swappedColorValue = k << 16 | j << 8 | i << 0;
            this.colorComponentValues = new float[]{(float)i / 255.0F, (float)j / 255.0F, (float)k / 255.0F};
            this.fireworkColor = fireworkColorIn;
         }
    }

    static
  {
    addColor(Blocks.LAVA, 0xffff8800);
    addColor(Blocks.END_ROD, DyeColor.WHITE);
    addColor(Blocks.TORCH, DyeColor.YELLOW);
    addColor(Blocks.REDSTONE_TORCH, DyeColor.RED);
    addColor(Blocks.LEVER, DyeColor.GRAY);
    addColor(Blocks.REDSTONE_WIRE, DyeColor.RED);
    addColor(Blocks.RAIL, DyeColor.GRAY);
    addColor(Blocks.ACTIVATOR_RAIL, DyeColor.GRAY);
    addColor(Blocks.DETECTOR_RAIL, DyeColor.GRAY);
    addColor(Blocks.CAKE, DyeColor.WHITE);
    addColor(Blocks.BROWN_MUSHROOM, DyeColor.BROWN);
    addColor(Blocks.RED_MUSHROOM, DyeColor.RED);
    addColor(Blocks.NETHER_WART, DyeColor.RED);
    addColor(Blocks.FLOWER_POT, DyeColor.BROWN);
    addColor(Blocks.GLASS, 0xffDEDEDE);
    addColor(Blocks.GLASS_PANE, 0xffDEDEDE);
    addColor(Blocks.STONE, 0x88888888);
    addColor(Blocks.GRASS, 0xFF7FCA69);
    addColor(Blocks.DIRT, 0x99AA8888);
    addColor(Blocks.WATER, 0x99599393);
    addColor(Blocks.LEAVES, 0xFF1FC514);
    addColor(Blocks.LEAVES2, 0xFF1FC514);
    addColor(Blocks.SAND, 0xFFEBE76F);
    addColor(Blocks.SANDSTONE, 0xDDEBE76F);
    addColor(Blocks.PLANKS, 0xFFD9C36F);
    addColor(Blocks.GRAVEL, 0xAAAAAAAA);
  }

  static void addColor(Block b, DyeColor c) {
      addColor(b, c.colorValue);
  }
  static void addColor(Block b, int i) {
      colors.put(b, i);
  }

}
