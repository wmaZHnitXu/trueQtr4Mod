package com.anet.qtr4tdm.uebki;

import java.util.ArrayList;
import java.util.List;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.uebki.Stages.GameStage;
import com.anet.qtr4tdm.uebki.messages.PlayersAnswerMessage;
import com.anet.qtr4tdm.uebki.messages.PlayersAskMessage;
import com.mojang.realmsclient.dto.PlayerInfo;

import ibxm.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.ServerStatusResponse.Players;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import scala.reflect.internal.Trees.This;

@Mod.EventBusSubscriber(modid = TdmMod.MODID)
    
public class Teams {
    public static Teams instance;

    public Teams () {
        instance = this;
        players = new ArrayList<player>();
    }

    private ArrayList<player> players;

    public ArrayList<player> GetPlayers () {
        return players;
    }
    @SubscribeEvent
    public static void OnConnectedToServer (FMLNetworkEvent.ClientConnectedToServerEvent event) {
        TdmMod.logger.info("initalise ClientConnectedToServerEvent : sexxx");
        new Teams();
        new Stages();
        TdmMod.wrapper.sendToServer(new PlayersAskMessage());
    }

    @SubscribeEvent
    public static void PlayerJoinWorld(PlayerLoggedInEvent e) {
        if (e.player != null && e.player instanceof EntityPlayer && !e.player.world.isRemote) {
            TdmMod.wrapper.sendTo(new PlayersAnswerMessage(),(EntityPlayerMP) e.player);
            boolean exist = false;
            for (player p : instance.players) {
                if (e.player.getName().equals(p.lastName)) {
                    p.playerEntity = e.player;
                    exist = true;
                    return;
                }
            }
            //Осторожно, выше return
            instance.players.add(new player(e.player));
            if (Stages.getStage() == GameStage.waitForPlayers) {
                    AddTeamBlocksToPlayer((EntityPlayer)e.player);
            }
        }
    }

    private static void AddTeamBlocksToPlayer (EntityPlayer player) {
        TdmMod.logger.info("Blocks added");
        player.inventory.clear();
        for (int i = 0; i < Stages.GetTeamcount(); i++) {
            teamState team = teamState.values()[i];
            ItemStack itemStack = new ItemStack(Item.getByNameOrId("minecraft:concrete"), 1, GetTeamColor(team).ordinal());
            itemStack.setStackDisplayName(GetTeamColorSymbols(team) + team.toString());
            player.addItemStackToInventory(itemStack);
        }
        teamState team = teamState.specs;
        ItemStack itemStack = new ItemStack(Item.getByNameOrId("minecraft:concrete"), 1, GetTeamColor(team).ordinal());
        itemStack.setStackDisplayName(GetTeamColorSymbols(team) + team.toString());
        player.addItemStackToInventory(itemStack);
    }

    @SubscribeEvent
    public static void PlayerBlockomTyk(PlayerInteractEvent event) {
        if (event.getWorld().isRemote) return;
        Item item = event.getItemStack().getItem();
        if (Stages.getStage().ordinal() < 2 && item.getUnlocalizedName().equals("tile.concrete")) {
            TryChangeTeam(event.getEntityPlayer(), GetTeamFromMetadata(event.getItemStack().getItem().getMetadata(event.getItemStack())));
        }
        else if (item.getRegistryName().equals(new ResourceLocation("minecraft:paper"))) {
            Stages.ShowEventsInfo(event.getEntityPlayer());
        }
    }

    public static boolean TryChangeTeam (EntityPlayer playerEntity, teamState teamToChange) {
        if (instance == null) return false;
        for (player p : instance.players) {
            if (p.playerEntity == playerEntity) {
                p.ChangeTeam(teamToChange);
                if (!playerEntity.getEntityWorld().isRemote) {
                    TitleHandler.SendTitleToPlayer("Вы теперь в команде " 
                    + GetTeamColorSymbols(teamToChange) 
                    + teamToChange.toString(), " ", "", p.playerEntity.getName());
                    TdmMod.wrapper.sendToAll(new PlayersAnswerMessage());
                }
                return true;
            }
        }
        if (!playerEntity.getEntityWorld().isRemote)
            TitleHandler.SendTitleToPlayer("Не удалось сменить команду", "Баланс.", "red", playerEntity.getName());
        return false;
    }    

    public static teamState GetTeamOfPlayer (EntityPlayer Player) {
        teamState result = teamState.specs;
        for (player p : instance.players) {
            if (Player.getName().equals(p.lastName)) result = p.team;
        }
        return result;
    }

    public static player[] GetPlayersOfTeam (teamState team) {
        ArrayList<player> preresult = new ArrayList<player>();
        for (player Player : instance.players) {
            if (Player.team == team) preresult.add(Player);
        }
        return preresult.toArray(new player[preresult.size()]);
    }

    public static EnumDyeColor GetTeamColor (teamState team) {
        switch (team) {
            case red: return EnumDyeColor.RED;
            case blue: return EnumDyeColor.BLUE;
            case green: return EnumDyeColor.LIME;
            case yellow: return EnumDyeColor.YELLOW;
            case white: return EnumDyeColor.WHITE;
            case black: return EnumDyeColor.BLACK;
            default: return EnumDyeColor.GRAY;
        }
    }

    public static teamState GetTeamFromMetadata (int metadata) {
        switch (metadata) {
            case 0: return teamState.white;
            case 4: return teamState.yellow;
            case 5: return teamState.green;
            case 11: return teamState.blue;
            case 14: return teamState.red;
            case 15: return teamState.black;

            default: return teamState.specs;
        }
    }
    public static String GetTeamColorSymbols (teamState team) {
        if (team == null) return "§7";
        switch (team) {
            case red: return "§c";
            case blue: return "§9";
            case green: return "§a";
            case yellow: return "§e";
            case white: return "§f";
            case black: return "§0";
            default: return "§7";
        }
    }
    public static String GetTeamColorSymbols (EntityPlayer player) {
        return GetTeamColorSymbols(GetTeamOfPlayer(player));
    }

    public static player GetClosestPlayer (BlockPos pos) {
        player result = null;
        double distance = 10000000;
        for (player p : instance.players) {
            BlockPos playerPos = p.playerEntity.getPosition();
            double newDist = pos.getDistance(playerPos.getX(), playerPos.getY(), playerPos.getZ());
            if (newDist < distance) {
                distance = newDist;
                TdmMod.logger.info(distance + " " + newDist + " " + String.valueOf(newDist < distance));
                result = p;
            }
        }
        return result;
    }

    public static void InsertPlayersFromServer (ArrayList<player> players) {
        instance.players = players;
        List<EntityPlayer> playerss = Minecraft.getMinecraft().world.playerEntities;
        for (int i = 0; i < playerss.size(); i++) {
            playerss.get(i).refreshDisplayName();
        }
    }
}

