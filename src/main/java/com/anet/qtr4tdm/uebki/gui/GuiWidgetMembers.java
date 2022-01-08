package com.anet.qtr4tdm.uebki.gui;

import java.util.ArrayList;

import javax.lang.model.util.ElementScanner6;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.uebki.messages.primitive.BasedRequest;
import com.anet.qtr4tdm.uebki.messages.primitive.GetBaseMembersMessage;
import com.anet.qtr4tdm.uebki.messages.primitive.MemberData;
import com.anet.qtr4tdm.uebki.messages.primitive.SetBaseMember;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiWidgetMembers extends GuiWidget implements IScrollest, IChildHasScrolls {

    private InputFieldDownScroll inp;
    private ITextFieldFocus master;
    private ArrayList<GuiButton> buttonList;
    private static ArrayList<MemberData> members;
    private static Scroll membersScroll;
    private static boolean permission;
    private static int minePermLevel = -1;
    private String shownName;
    private String shownRank;

    public GuiWidgetMembers(boolean rightSide, int x, int y, int dwidth, int dheight, Minecraft mc,
            ResourceLocation icon, ITextFieldFocus master) {
        super(rightSide, x, y, dwidth, dheight, mc, icon);
        this.master = master;
        inp = new InputFieldPlayers(0, mc.fontRenderer, xPos + 20, yPos + 20, 60, 15, master);
        buttonList = new ArrayList<GuiButton>();
        membersScroll = new Scroll(xPos + 115, yPos, 85, new String[0], this, mc, true, 150);
        membersScroll.enabled = true;
        buttonList.add(new SimpleButton(0, xPos + 85, yPos + 20, 15, 15, "+"));
        buttonList.add(new SimpleButton(1, xPos + 20, yPos + 100, 105, 20, "Исключить игрока"));
        buttonList.get(0).enabled = false;
        buttonList.get(1).visible = false;
        shownName = "";
        shownRank = "";
    }
    
    @Override
    public void drawContent(int mouseX, int mouseY) {
        inp.drawTextBox();
        for (GuiButton b : buttonList) {
            b.drawButton(mc, mouseX, mouseY, 1);
        }
        drawString(mc.fontRenderer, shownName, xPos + 20, yPos + 60, 0xFFFFFFFF);
        drawString(mc.fontRenderer, shownRank, xPos + 20, yPos + 80, 0xFFFFFFFF);
        membersScroll.draw(mouseX, mouseY);
        inp.scroll.draw(mouseX, mouseY);
    }

    @Override
    public void Update() {
        inp.updateCursorCounter();
        super.Update();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        inp.textboxKeyTyped(typedChar, keyCode);
        inp.setText(inp.getText().trim());
        super.keyTyped(typedChar, keyCode);
        buttonList.get(0).enabled = canSetPlayer(inp.getText()) && permission;
        boolean n = false;
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).name.toLowerCase().equals(inp.getText().toLowerCase())) {
                ScrollClick(i);
                buttonList.get(0).enabled = false;
                n = true;
            }
        }
        if (!n) {
            ResetShown();
        }
    }

    private boolean canSetPlayer (String query) {
        if (query.length() == 0 || query.contains(" ")) return false;
        return true;
    }

    public static void InserMembers (GetBaseMembersMessage message) {
        members = message.members;
        String[] scrl = new String[members.size()];
        for (int i = 0; i < scrl.length; i++) {
            scrl[i] = members.get(i).name;
            if (scrl[i].equals(Minecraft.getMinecraft().player.getName())) minePermLevel = members.get(i).level;
        }
        membersScroll.sections = scrl;
        permission = message.permission;
    }

    @Override
    public void MouseClick(int x, int y) {
        inp.mouseClicked(x, y, 0);
        membersScroll.Click(x, y);
        for (GuiButton b : buttonList) {
            boolean pr = b.mousePressed(mc, x, y);
            if (pr) {
                switch (b.id) {
                    case 0:
                        if (canSetPlayer(inp.getText())) {
                            if (buttonList.get(1).visible) {
                                ResetShown();
                            }
                            else {
                                TdmMod.wrapper.sendToServer(new SetBaseMember(inp.getText(), 0, master.GetBlockPos()));
                                inp.setText("");
                                buttonList.get(0).enabled = false;
                            }
                        }
                    break;
                    case 1:
                        if (canSetPlayer(inp.getText())) {
                                TdmMod.wrapper.sendToServer(new SetBaseMember(inp.getText(), -1, master.GetBlockPos()));
                                inp.setText("");
                                ResetShown();
                        }
                    break;
                }
            }
        }
        super.MouseClick(x, y);
    }

    @Override
    public void Deploy() {
        TdmMod.wrapper.sendToServer(new BasedRequest(master.GetBlockPos(), 5, mc.world.provider.getDimension()));
        ResetShown();
        super.Deploy();
    }

    @Override
    public void ScrollClick(int id) {
        inp.setText(membersScroll.sections[id]);
        inp.setCursorPositionEnd();
        shownName = members.get(id).name;
        shownRank = members.get(id).level == 0 ? "Участник" : "§aВладелец";
        boolean isme = mc.player.getName() == shownName;
        buttonList.get(1).visible = (true && permission) || isme;
        buttonList.get(1).enabled = (isme && minePermLevel != 3) || (minePermLevel > members.get(id).level);
        buttonList.get(1).displayString = isme ? "Выйти" : "Исключить игрока";
    }

    public void ResetShown () {
        shownName = "";
        shownRank = "";
        buttonList.get(1).visible = false;
        membersScroll.selectedIndex = -1;
    }

    @Override
    public void SetPosition(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        buttonList.get(0).y = yPos + 20;  
        buttonList.get(1).y = yPos + 100;
        membersScroll.yPos = yPos;
        inp.y = yPos + 20;   
    }

    @Override
    public void mouseWheelMove(int mouseX, int mouseY, int wheelDelta) {
        membersScroll.handleMouseWheel(mouseX, mouseY, wheelDelta * 10);
    }
}
