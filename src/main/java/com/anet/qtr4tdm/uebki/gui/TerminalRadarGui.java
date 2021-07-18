package com.anet.qtr4tdm.uebki.gui;


import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.tiles.TerminalRadarTile;
import com.anet.qtr4tdm.uebki.RadarInfoStruct;
import com.anet.qtr4tdm.uebki.RadarObjectStructure;
import com.anet.qtr4tdm.uebki.messages.RadarInfoHandler;
import com.anet.qtr4tdm.uebki.messages.BasedRequest;

import org.lwjgl.opengl.GL11;


import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class TerminalRadarGui extends GuiScreen {

    private static ResourceLocation background_texture;
    private static int screenWidth;
    private static int screenHeight;
    private static int offsetLeft;
    private static int offsetTop;
    private static ResourceLocation radar_texture;
    private static ResourceLocation enemy_texture;
    private TerminalRadarTile terminalTile;
    private int counter;

    public TerminalRadarGui (TerminalRadarTile tile) {
        terminalTile = tile;
        background_texture = new ResourceLocation(TdmMod.MODID+":textures/gui/terminal_background.png");
        radar_texture = new ResourceLocation(TdmMod.MODID+":textures/gui/terminal_radar.png");
        //teammate_texture = new ResourceLocation(TdmMod.MODID+":textures/gui/terminal_teammate.png");
        enemy_texture = new ResourceLocation(TdmMod.MODID+":textures/gui/terminal_enemy.png");
    }

    @Override
    public void drawScreen (int parWidth, int parHeight, float p_73863_3_) {
        super.drawScreen(parWidth, parHeight, p_73863_3_);
        GL11.glColor4f(1.0F, 1.0f, 1.0f, 1.0F);
        mc.getTextureManager().bindTexture(background_texture);
        screenWidth = 256;
        screenHeight = screenWidth;
        offsetLeft = (int)(width * 0.5 - screenWidth * 0.5f);
        offsetTop = 15 * height / 480;
        drawTexturedModalRect(offsetLeft, offsetTop, 0, 0, screenWidth, screenHeight);
        if (RadarInfoHandler.objects == null) return;
        //else
            for (RadarObjectStructure obj : RadarInfoHandler.objects) {
                GL11.glColor4f(1.0F, 1.0f, 1.0f, 1.0F);
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                mc.getTextureManager().bindTexture(obj.isRadar ? radar_texture : enemy_texture);
                int size = obj.isRadar ? 4 : 4;
                //ЗДЕСЬ РАНЬШЕ БЫЛА КОРРЕКЦИЯ ЦВЕТА В ЗАВИСИОМСТИ ОТ КОМАНДЫ ОБЪЕКТА
                int[] c = Coords(obj);
                float xCoord = offsetLeft + screenWidth * 0.5f + c[0];
                float yCoord = offsetTop + screenHeight * 0.5f + c[1];
                drawTexturedModalRect(xCoord - size * 0.5f, yCoord - size * 0.5f, 0, 0, size, size);

                if (!(Math.abs(parWidth - xCoord) < size * 0.75f && Math.abs(parHeight - yCoord) < size * 0.75f)) {
                    //Draw radar range
                    if (obj.isRadar) {
                        int range = RadarInfoStruct.getRange(Character.getNumericValue(obj.DisplayName.charAt(obj.DisplayName.length()-1)));
                        float relMult = (float)(screenWidth) / 1000f;
                        int sqrCount = (int)(range * relMult * Math.PI * 1f);
                        float red = 0, green = 0, blue = 0;
                        //ЗДЕСЬ ТОЖЕ
                        for (int i = 0; i <= sqrCount; i++) {
                            Double sin = Math.sin(i);
                            Double cos = Math.cos(i);
                            float relX = cos.floatValue() * range * relMult;
                            float relY = sin.floatValue() * range * relMult;
                            Float xCoord2 = relX + xCoord;
                            Float yCoord2 = relY + yCoord;
                            if ((xCoord2 < offsetLeft + screenWidth - 5 && xCoord2 > offsetLeft + 5 ) && (yCoord2 < offsetTop + screenHeight - 5 && yCoord2 > offsetTop + 5)) {
                                GL11.glColor4f(red, green, blue, 1.0F);
                                drawRect(xCoord2.intValue(), yCoord2.intValue() , xCoord2.intValue() + 1, yCoord2.intValue() - 1, 0);
                            }
                        }
                    }
                }
                else {
                    fontRenderer.drawStringWithShadow(obj.DisplayName, (int)xCoord, (int)yCoord, 0);
                }
                GL11.glColor4f(1.0F, 1.0f, 1.0f, 1.0F);
            }
    }

    @Override
    public void updateScreen() {
        if (counter <= 0) {
            try {
                TdmMod.wrapper.sendToServer(new BasedRequest(terminalTile.getPos(), 2, mc.world.provider.getDimension()));
            }
            catch (Exception e) {
                TdmMod.logger.info("network error");
                TdmMod.logger.info(e.toString());
            }
            counter = 30;
        }
        else {
            counter--;
        }
        super.updateScreen();
    }
    
    public int[] Coords (RadarObjectStructure obj) {
        int[] result = new int[4];
        float relX = (float)(obj.pos.getX() - terminalTile.getPos().getX()) / 1000f * screenWidth;
        float relY = (float)(obj.pos.getZ() - terminalTile.getPos().getZ()) / 1000f * screenHeight;
        result[2] = (int)relX;
        result[3] = (int)relY;
        relX = Math.max(relX, -(screenWidth - 5) / 2f); relX = Math.min(relX, (screenWidth - 5) / 2f);
        relY = Math.max(relY, -(screenHeight - 5) / 2f); relY = Math.min(relY, (screenHeight - 5) / 2f);
        result[0] = (int) (relX);
        result[1] = (int) (relY);
        return result;
    }
}
