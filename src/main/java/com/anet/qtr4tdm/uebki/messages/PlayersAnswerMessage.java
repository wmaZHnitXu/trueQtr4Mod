package com.anet.qtr4tdm.uebki.messages;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.uebki.Teams;
import com.anet.qtr4tdm.uebki.teamState;
import com.anet.qtr4tdm.uebki.player;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.ServerStatusResponse.Players;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import scala.actors.threadpool.Arrays;

public class PlayersAnswerMessage implements IMessage {

    public PlayersAnswerMessage() {

    }

    ArrayList<player> players;

    @Override
    public void fromBytes(ByteBuf buf) {
        players = new ArrayList<player>();
        String readenString = ByteBufUtils.readUTF8String(buf);
        TdmMod.logger.info("AnswerPlayersReaden: " + readenString);
        String[] splitedString = readenString.split(" ");
        for (String str : splitedString) {
            TdmMod.logger.info("AnswerPlayers : " + str);
            if (str.length() > 1) {
                players.add(new player(str.substring(0, str.length()-1), teamState.values()[Integer.parseInt(String.valueOf(str.charAt(str.length()-1)))]));
            }
        }
        Teams.InsertPlayersFromServer(players);
    }

    //БАЙТЧЕЙН
    @Override
    public void toBytes(ByteBuf buf) {
        players = Teams.instance.GetPlayers();
        String finalString = "";
        for (player p : players) {
            finalString += p.lastName + p.team.ordinal() + " ";
        }
        ByteBufUtils.writeUTF8String(buf, finalString);
        TdmMod.logger.debug("AnswerPlayersFinal : " + finalString);
    }
    
    public static byte[] GetbyteArrFromByteArr (Byte[] Byti) { //Вы там в джаве совсем долбаебы, че это за хуйня?
        byte[] byti = new byte[Byti.length];
        for (int i = 0; i < byti.length; i++) {
            byti[i] = Byti[i];
        }
        return byti;
    }
}
