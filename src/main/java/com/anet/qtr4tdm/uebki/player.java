package com.anet.qtr4tdm.uebki;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.uebki.messages.PlayersAnswerMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class player {
    public teamState team;
    public EntityPlayer playerEntity;
    public String lastName;
    public boolean dead;

    public player (String name, teamState team) {
        this.lastName = name;
        this.team = team;
        try {
            if (name.equals(Minecraft.getMinecraft().player.getName())) {
                Minecraft.getMinecraft().player.refreshDisplayName();
            }
            else {
                TdmMod.logger.info(Minecraft.getMinecraft().player.getName() + " " + name);
                NetworkPlayerInfo info;
                if ((info = Minecraft.getMinecraft().getConnection().getPlayerInfo(name)) != null) {
                    info.setDisplayName(new TextComponentString(Teams.GetTeamColorSymbols(team) + name));
                }
            }
        }
        catch (Exception e){
            TdmMod.logger.info(e.getMessage());
        }
        this.dead = false;
    }

    public player () {
        this.team = teamState.specs;
        this.dead = false;
    }

    public player (Entity playerEntity) {
        this();
        this.playerEntity = (EntityPlayer)playerEntity;
        this.lastName = playerEntity.getName();
    }
    public void AddFormatName (String Suffix) {
        System.out.println("formatted");
    }

    public void ChangeTeam (teamState newTeam) {
        this.team = newTeam;
        this.playerEntity.refreshDisplayName();
        TdmMod.wrapper.sendToAll(new PlayersAnswerMessage());
    }
}
