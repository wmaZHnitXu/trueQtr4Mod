package com.anet.qtr4tdm.uebki;

import com.anet.qtr4tdm.TdmMod;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import com.anet.qtr4tdm.uebki.Teams;

public class Commands extends CommandBase {

  @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {

        TdmMod.logger.info("execute called");

        if (params != null) {
            if (params.length == 1) {
                teamState team = teamState.specs;
                TdmMod.logger.info(params[0]);
                for (teamState state : teamState.values()) {
                    TdmMod.logger.info(state.toString());
                    if (params[0].equals(state.toString()))
                        team = state;
                }
                String message;
                if (params[0].equals("load")) {
                    Stages.instance.Load();
                    message = "Mod activen";
                }
                else
                if (params[0].equals("start")) {
                    Stages.instance.Start();
                    message = "startoval";
                }
                else {
                    boolean success = Teams.TryChangeTeam((EntityPlayer)sender.getCommandSenderEntity(), team);
                    message = success ? "Вы теперь в команде " + team.toString() : "Не удалось сменить команду.";
                }
                TextComponentString text = new TextComponentString(message);
                text.getStyle().setColor(TextFormatting.RED);
                sender.sendMessage(text);
            }
            else if (params.length == 2) {
                if (params[0].equals("skip")) {
                    Stages.instance.Skip(Integer.parseInt(params[1]));
                }
            }
        }
    }

    @Override
    public String getName() {
        return "team";
    }

    @Override
    public String getUsage(ICommandSender arg0) {
        return "command.qtr4tdm.usage";
    }
}
