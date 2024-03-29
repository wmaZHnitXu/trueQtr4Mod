package com.anet.qtr4tdm.uebki.gui;

import java.io.IOException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;


import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.bases.baseInfo;
import com.anet.qtr4tdm.common.items.BaseExpandItem;
import com.anet.qtr4tdm.common.tiles.BaseTile;
import com.anet.qtr4tdm.init.BlocksInit;
import com.anet.qtr4tdm.uebki.gui.baseGuiMisc.BaseContainer;
import com.anet.qtr4tdm.uebki.messages.primitive.AskDefenceDataToPlayer;
import com.anet.qtr4tdm.uebki.messages.primitive.BaseUpgradeMessage;
import com.anet.qtr4tdm.uebki.messages.primitive.DefenceDataToPlayer;
import com.anet.qtr4tdm.uebki.messages.primitive.DefenceDataToPlayer.DefDataStruct;

import org.jline.reader.Widget;
import org.lwjgl.input.Mouse;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import scala.tools.ant.sabbus.Break;

public class BaseGui extends GuiContainer implements ITextFieldFocus {

    public final BaseTile te;
    public final InventoryPlayer player;
    private final ResourceLocation background = new ResourceLocation(TdmMod.MODID, "textures/gui/furnace.png");

    private static baseInfo info;
    private static DynamicTexture mapTex;
    private ResourceLocation mapTexLocation;
    private int xOffset;
    private int yOffset;
    private static final HashMap<Block, Integer> colors = new HashMap<>();
    private ChunkPos selectedChunk; 
    private ChunkPos startchunk;
    private static int upgradeCountNeeded;
    private static int upgradeMeta;
    private static ItemStack neededStack;
    private String[] rightBar;
    private boolean baseContains = true;
    private boolean nearBase = false;
    private ArrayList<GuiWidget> widgets;
    public boolean InputFieldFocused;
    private static ArrayList<DefDataStruct> defenceData;
    private static ArrayList<DefDataStruct> dead;
    private boolean blink;
    private static GuiWidgetModule widgetModule;
    private static DefDataStruct selectedDef;


    static {
        dead = new ArrayList<DefDataStruct>();
        defenceData = new ArrayList<DefDataStruct>();
    }

    private int counter;
    private static boolean sortedDead;
    
    public BaseGui(InventoryPlayer playerInventory, BaseTile baseTe) {
        super(new BaseContainer(playerInventory, baseTe));
        player = playerInventory;
        te = baseTe;
        widgetModule = null;
        LoadMap();
        UpdateMap(1);
        UpdatePrice();
        widgets = new ArrayList<GuiWidget>();
        rightBar = new String[5];
    }

    private void UpdateData () {
        TdmMod.wrapper.sendToServer(new AskDefenceDataToPlayer(te.getPos(), Minecraft.getMinecraft().player.getEntityId()));
        counter = 100;
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

            if (!widgetModule.deployed) {

                if (info != null && info.chunks != null)
                    for (ChunkPos pos : info.chunks) {
                        int localx = pos.x - (startchunk.x - 6);
                        int localy = pos.z - (startchunk.z - 6);
                        drawRect(-81 + localx * 16, -50 + localy * 16, -81 + localx * 16 + 16, -50 + localy * 16 + 16, 0x5599FF99);
                    }

                if (mouseX > guiLeft - 81 && mouseX < guiLeft - 81 + 208 && mouseY > guiTop - 50 && mouseY < guiTop - 50 + 208) {
                    int onMapX = mouseX - (guiLeft - 81);
                    int onmapY = mouseY - (guiTop - 50);
                    ChunkPos pos = new ChunkPos(startchunk.x - 6 + onMapX / 16, startchunk.z - 6 + onmapY / 16);
                    boolean shouldHighlight = false;
                    for (ChunkPos poss : info.chunks) {
                        if ((Math.abs(poss.x - pos.x) == 1 && Math.abs(poss.z - pos.z) == 0) || (Math.abs(poss.z - pos.z) == 1 && Math.abs(poss.x - pos.x) == 0)) {shouldHighlight = true; continue;}
                    }
                    if (shouldHighlight) {
                        int localx = pos.x - (startchunk.x - 6);
                        int localy = pos.z - (startchunk.z - 6);
                        drawRect(-81 + localx * 16, -50 + localy * 16, -81 + localx * 16 + 16, -50 + localy * 16 + 16, 0xFFFFFFFF);
                    }
                }

                if (selectedChunk != null) {
                    int localx = selectedChunk.x - (startchunk.x - 6);
                    int localy = selectedChunk.z - (startchunk.z - 6);
                    drawRect(-81 + localx * 16, -50 + localy * 16, -81 + localx * 16 + 16, -50 + localy * 16 + 16, 0x5500FFFF);
                    drawRect(-81 + localx * 16, -50 + localy * 16, -81 + localx * 16 + 1, -50 + localy * 16 + 1, 0xFFFFFFFF);
                    drawRect(-81 + localx * 16 + 15, -50 + localy * 16 + 15, -81 + localx * 16 + 16, -50 + localy * 16 + 16,0xFFFFFFFF);
                    drawRect(-81 + localx * 16, -50 + localy * 16 + 15, -81 + localx * 16 + 1, -50 + localy * 16 + 16, 0xFFFFFFFF);
                    drawRect(-81 + localx * 16 + 15, -50 + localy * 16, -81 + localx * 16 + 16, -50 + localy * 16 + 1, 0xFFFFFFFF);
                }
            }
                drawRect(mouseX, mouseY, mouseX+1, mouseY+1, 0xFFFFFFF);
                if (defenceData != null && defenceData.size() > 0) {
                    for (int i = 0; i < defenceData.size(); i++) {
                        DefDataStruct def = defenceData.get(i);
                        int relativeX = def.pos.getX() - (startchunk.x - 6) * 16;
                        int relativeZ = def.pos.getZ() - (startchunk.z - 6) * 16;
                        int finalX = -81 + relativeX;
                        int finalZ = -50 + relativeZ;

                        boolean highlighted = mouseX - (guiLeft-100) - 19 == relativeX  && mouseY - (guiTop-70) - 20 == relativeZ;

                        int color = 0;
                        switch (def.chargeStatus) {
                            case 0 : color = 0xFF111188; break;
                            case 1 : color = 0xFF0077FF; break;
                            case 3 : color = 0xFFFFFF00; break;
                            case -1 : color = 0xFFFF0000; break;
                        }
                        drawRect(finalX, finalZ, finalX + 1, finalZ + 1, highlighted || def == selectedDef ? 0xFFFFFFFF : color);
                    }
                }

                if (!blink) {
                    for (int i = 0; i < dead.size(); i++) {
                        DefDataStruct def = dead.get(i);
                        int relativeX = def.pos.getX() - (startchunk.x - 6) * 16;
                        int relativeZ = def.pos.getZ() - (startchunk.z - 6) * 16;

                        int color = 0xFFFF0000;
                        drawRect(-81 + relativeX, -50 + relativeZ, -81 + relativeX + 1, -50 + relativeZ + 1, color);
                       
                    }
                }

            GlStateManager.popMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.popMatrix();
        mc.getRenderItem().renderItemAndEffectIntoGUI(neededStack, 256 - 100, 133 - 70);
        drawString(fontRenderer, "x" + upgradeCountNeeded, 275 - 100, 133 - 70, 0xFFFFFFFF);

        rightBar[0] = "Статус: " + info.status.toString();
        rightBar[1] = "Уровень: " + info.level;
        rightBar[2] = "Орудия: " + info.defcount + " / " +  info.GetDefCount();
        for (int i = 0; i < rightBar.length; i++) {
            drawString(fontRenderer, rightBar[i], 230 - 100, 26 + i * 15 - 70, 0xFFFFFFFF);
        }
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        mc.renderEngine.bindTexture(background);
        drawScaledCustomSizeModalRect(guiLeft - 100, guiTop - 70, 0, 0, 365, 329, 365, 329, 512, 512);
        drawWidgets(mouseX, mouseY);
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
                    int light = 255 - Math.max(((northHeight + westHeight) - hmap[h]-1) * 10, 0);
                    color = color - 0xFF000000 + light * 16777216;

                    mapTextureData[j * 208 * 16 + i * 16 + h % 16 + (h / 16 * 208)] = color;
                }
            }
        }

        mapTex.updateDynamicTexture();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    public void updateScreen() {
        UpdateButton();
        for (GuiWidget widget : widgets) {
            widget.Update();
        }
        counter--;
        if (counter % 5 == 0) blink = !blink;
        if (counter <= 0) UpdateData();
        super.updateScreen();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        for (GuiWidget widget : widgets) {
            if (widget.deployProgress == 1)
                widget.keyTyped(typedChar, keyCode);
        }
        if (!InputFieldFocused) {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        
        super.handleMouseInput();

        if (widgets == null) return;

        int i = Mouse.getEventDWheel();
        int x = Mouse.getX();
        int y = Mouse.getY();

        i = Integer.compare(i, 0);
        for (GuiWidget widget : widgets) {
            if (widget instanceof IChildHasScrolls) {
                ((IChildHasScrolls)widget).mouseWheelMove(x, y, i);
            }
        } 
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                SendExpand(selectedChunk);
                baseContains = true;
                nearBase = false;
                selectedChunk = null;
                buttonList.get(button.id).enabled = false;
            break;
        }
        super.actionPerformed(button);
    }

    public void UpdateButton () {
        ItemStack inSlot = inventorySlots.getSlot(0).getStack();
        if (inSlot != null && inSlot.getItem() instanceof BaseExpandItem) {
            if (inSlot.getItemDamage() == upgradeMeta && inSlot.getCount() >= upgradeCountNeeded) {
                buttonList.get(0).enabled = !baseContains && nearBase; return;
            }
        }
        buttonList.get(0).enabled = false;
    }


    public static void UpdatePrice () {
        int chunkCount = info.chunks.length;
        if (chunkCount < 32) {
            upgradeCountNeeded = (chunkCount) % 8 + 1;
            upgradeMeta = chunkCount / 8;
        }
        neededStack = new ItemStack(BlocksInit.BASEEXPANDER);
        neededStack.setItemDamage(upgradeMeta);
    }

    @Override
    public void initGui() {
        xOffset = width / 2 - xSize / 2;
        yOffset = height / 2 - ySize / 2;
        super.initGui();
        buttonList.clear();
        buttonList.add(new UpgradeButton(0, 132 + guiLeft, 130 + guiTop, 112, 24, "Расширить базу"));
        buttonList.get(0).enabled = false;
        widgets.clear();
        widgetModule = new GuiWidgetModule(true, guiLeft - 100 + 367, guiTop - 70, 200, 165, mc, new ResourceLocation(TdmMod.MODID, "textures/gui/members.png"), this);
        widgets.add(widgetModule);
        widgets.add(new GuiWidgetMembers(true, guiLeft - 100 + 367, guiTop - 30, 200, 150, mc, new ResourceLocation(TdmMod.MODID, "textures/gui/members.png"), this));
       
    }

    @Override
    public int getXSize() {
        return 700;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        //MAP

        if (mouseX > guiLeft - 81 && mouseX < guiLeft - 81 + 208 && mouseY > guiTop - 50 && mouseY < guiTop - 50 + 208) {
            int onMapX = mouseX - (guiLeft - 81);
            int onmapY = mouseY - (guiTop - 50);
            nearBase = false;
            if (widgetModule.deployed == false) {
                ChunkPos pos = new ChunkPos(startchunk.x - 6 + onMapX / 16, startchunk.z - 6 + onmapY / 16);
                if (selectedChunk != null && selectedChunk.equals(pos)) {
                    selectedChunk = null;
                    rightBar[3] = "";
                    rightBar[4] = "";
                }
                else {
                    rightBar[3] = "Чанк: x:" + pos.x + " z:" + pos.z;
                    baseContains = false;
                    for (ChunkPos poss : info.chunks) {
                        if (poss.equals(pos)) {baseContains = true; continue;}
                    }
                    rightBar[3] = (baseContains ? "§a" : "") + rightBar[3];
                    for (ChunkPos poss : info.chunks) {
                        if ((Math.abs(poss.x - pos.x) == 1 && Math.abs(poss.z - pos.z) == 0) || (Math.abs(poss.z - pos.z) == 1 && Math.abs(poss.x - pos.x) == 0))
                        {nearBase = true; continue;}
                    }
                    rightBar[4] = (nearBase ? baseContains ? "Уже запривачен." : "Можно заприватить." : "Нельзя заприватить.");
                    selectedChunk = pos;
                }
            }
            else {
                for (DefDataStruct struct : defenceData) {
                    int relativeX = struct.pos.getX() - (startchunk.x - 6) * 16;
                    int relativeZ = struct.pos.getZ() - (startchunk.z - 6) * 16;
                    if (mouseX - (guiLeft-100) - 19 == relativeX  && mouseY - (guiTop-70) - 20 == relativeZ) {
                        widgetModule.insertModule(struct);
                        selectedDef = struct;
                    }
                }
            }
        }

        //WIDGET

        for (GuiWidget widget : widgets) {
            widget.MouseClick(mouseX, mouseY);
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public static void InsertInfo (baseInfo infos) {
        info = infos;
        UpdatePrice();
    }

    public static void InsertDefenceData (ArrayList<DefDataStruct> data) {

        dead.clear();

        if (defenceData != null && defenceData.size() > data.size()) {
            for (int i = 0; i < defenceData.size(); i++) {
                boolean contains = false;
                for (int j = 0; j < data.size(); j++) {
                    if (defenceData.get(i).pos.equals(data.get(j).pos)) {
                        contains = true; break;
                    }
                }
                if (!contains) dead.add(defenceData.get(i));
            }
        }

        if (selectedDef != null && widgetModule != null) {
            boolean contains = false;
            for (DefDataStruct s : data) {
                if (s.pos.equals(selectedDef.pos)) {
                    widgetModule.insertModule(s);
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                widgetModule.insertModule(null);
            }
        }

        defenceData = data;
        sortedDead = false;

        if (widgetModule != null) {
            widgetModule.InsertModules(defenceData);
        }
    }

    public void SendExpand (ChunkPos pos) {
        TdmMod.wrapper.sendToServer(new BaseUpgradeMessage(info.id, selectedChunk));

    }

    public void SetSelectedDef (DefDataStruct def) {
        selectedDef = def;
    }

    public void drawWidgets (int mouseX, int mouseY) {
        int rightOffset = 0;
        int leftOffset = 0;
        for (int i = 0; i < widgets.size(); i++) {
            GuiWidget widget = widgets.get(i);
            widget.SetPosition(widget.xPos, widget.yBasic + (widget.rightSide ? rightOffset : leftOffset));
            if (widget.deployProgress > 0) {
                if (widget.rightSide) rightOffset+=Double.valueOf(Double.valueOf(widget.DeployedHeight - widget.iconHeight) * widget.deployProgress).intValue();
                else leftOffset += Double.valueOf(Double.valueOf(widget.DeployedHeight - widget.iconHeight) * widget.deployProgress).intValue();
            }
            widget.draw(mouseX, mouseY);
        }
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

        private final int colorValue;


        private DyeColor(int idIn, String translationKeyIn, int colorValueIn, int fireworkColorIn, int textColorIn) {
            this.colorValue = colorValueIn + 0xFF000000;
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

    @Override
    public void SetFocused(boolean b) {
        InputFieldFocused = b;
    }

    @Override
    public BlockPos GetBlockPos() {
        return te.getPos();
    }

  class UpgradeButton extends GuiButton {

    int buttonWidth, buttonHeight;

    public UpgradeButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        buttonHeight = 24;
        buttonWidth = 124;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible)
            {
                FontRenderer fontrenderer = mc.fontRenderer;
                mc.getTextureManager().bindTexture(background);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                int stateTexY = 0;
                int texX = 367;
                if (this.hovered) stateTexY = 25;
                if (!this.enabled) stateTexY = 50;
                drawModalRectWithCustomSizedTexture(this.x, this.y, texX, stateTexY, buttonWidth, buttonHeight, 512, 512);
                this.mouseDragged(mc, mouseX, mouseY);
                int j = 0xAAAAAAAA;

                if (packedFGColour != 0)
                {
                    j = 0xAAAAAAAA;
                }
                else
                if (!this.enabled)
                {
                    j = 0xacacac;
                }
                else if (this.hovered)
                {
                    j = 0xFFFFFFFF;
                }

                fontrenderer.drawString( this.displayString, this.x + this.width / 2 - fontrenderer.getStringWidth(this.displayString) / 2, this.y + (this.height - 8) / 2, j, false);
            }
        }
    }
}
