package com.anet.qtr4tdm.commands;

import com.anet.qtr4tdm.common.bases.InWorldBasesManager;
import com.anet.qtr4tdm.uebki.SqlHelper;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class AdminManagementCommands extends CommandBase {

    @Override
    public String getName() {
        return "ltadmin";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "dadaya";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        switch (args.length) {
            case 2:
                if (args[0].equals("removeallbases")) {
                    if (args[1].equals(String.valueOf((SqlHelper.instance.GetPlayerId(sender.getName()))))) {
                        sender.sendMessage(new TextComponentString(InWorldBasesManager.DeleteAllBases() ? "§l§4Информация о базах удалена!!!" : "§l§4Невозможно удалить данные баз.")); 
                    }
                }
        }        
    }
    
}
