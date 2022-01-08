package com.anet.qtr4tdm.uebki.messages.primitive;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TopBarMessage implements IMessage {

    public String title;
    public String Subtitle;
    public int iconId;
    public int time;

    public TopBarMessage () {}

    public TopBarMessage (String title, String Subtitle, int iconId, int time) {
        this.title = title;
        this.Subtitle = Subtitle;
        this.iconId = iconId;
        this.time = time;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        String[] splitedString = ByteBufUtils.readUTF8String(buf).split("~");
        title = splitedString[0];
        Subtitle = splitedString[1];
        iconId = Integer.parseInt(splitedString[2]);
        time = Integer.parseInt(splitedString[3]);
        TopBarSet();
    }

    @SideOnly(Side.CLIENT)
    private void TopBarSet () {
        com.anet.qtr4tdm.uebki.gui.OverlayGui.SetTopBar(title, Subtitle, com.anet.qtr4tdm.uebki.gui.OverlayGui.TopBarIcon.getIconFromId(iconId), time);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, title + "~" + Subtitle + "~" + iconId + "~" + time);
    }
    
}
