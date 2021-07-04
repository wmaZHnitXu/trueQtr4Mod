package com.anet.qtr4tdm.uebki;

import com.anet.qtr4tdm.TdmMod;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import com.anet.qtr4tdm.uebki.Teams;

public class TeamChat extends CommandBase {

  @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
        teamState team = Teams.GetPlayerByName(sender.getName()).team;
        String message = "("+ team.toString() + "§r-Terrorist) " + sender.getDisplayName().getFormattedText() + " : " ;
        for (String s : params) {
            message += s + " ";
        }
        for (player p : Teams.GetPlayersOfTeam(team)) {
            if (p.playerEntity != null) {
                p.playerEntity.sendMessage(new TextComponentString(message));
            }
        }
    }

    @Override
    public String getName() {
        return "t";
    }

    @Override
    public String getUsage(ICommandSender arg0) {
        return "command.qtr4tdm.usage";
    }


    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }
}
