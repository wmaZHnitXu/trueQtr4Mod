package com.anet.qtr4tdm.uebki;

import java.util.Arrays;

import com.anet.qtr4tdm.TdmMod;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandTitle;
import net.minecraft.entity.player.EntityPlayer;

public class TitleHandler {

    private static CommandTitle commandTitle;

    public static void SendTitleToPlayer (String title, String subTitle, String color, String playerName) {
        SendTitleBaseMethod(title, subTitle, color, playerName);
    }

    public static void SendTitleForAll (String title, String subTitle, String color) {
        for (EntityPlayer p : TdmMod.currentServer.getPlayerList().getPlayers())
            SendTitleBaseMethod(title, subTitle, color, p.getName());
        
    }

    private static void SendTitleBaseMethod (String title, String subTitle, String color,  String target) {
        if (TdmMod.currentServer == null) return;
        commandTitle = new CommandTitle();
        if (color.equals("")) color = "white";
        String[] arguments = new String[3];
        arguments[0] = target;
        if (subTitle != null && !subTitle.equals("")) {
            arguments[1] = "subtitle";
            arguments[2] = "{\"text\":\"" + subTitle + "\",\"color\":\"white\",\"italic\":false}";
            try {
                commandTitle.execute(TdmMod.currentServer, TdmMod.currentServer, arguments);
                TdmMod.logger.info(Arrays.toString(arguments));
            }
            catch (CommandException e) {
                TdmMod.logger.info(e.getMessage());
                TdmMod.logger.info(Arrays.toString(arguments));
                TdmMod.logger.info("pizda_subtitle");
            }
        }
        arguments[1] = "title";
        arguments[2] = "{\"text\":\"" + title + "\",\"color\":\"" + color + "\",\"bold\":true}";

        //Пиздобратия с трайкэч анхибитед эксептион не оьращай внемания, тьебе нужна 42 строка
        try {
            commandTitle.execute(TdmMod.currentServer, TdmMod.currentServer, arguments);
            TdmMod.logger.info(Arrays.toString(arguments));
        }
        catch (CommandException e) {
            TdmMod.logger.info(e.toString());
            TdmMod.logger.info(Arrays.toString(arguments));
            TdmMod.logger.info("pizda_title");
        }
    }
}
