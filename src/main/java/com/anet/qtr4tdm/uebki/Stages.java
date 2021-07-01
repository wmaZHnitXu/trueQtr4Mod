package com.anet.qtr4tdm.uebki;

import java.sql.Time;
import java.util.ArrayList;

import javax.swing.text.AbstractDocument.BranchElement;

import com.anet.qtr4tdm.uebki.Teams;
import com.anet.qtr4tdm.TdmMod;
import com.ibm.icu.util.BytesTrie.Result;

import org.apache.commons.io.output.NullOutputStream;

import ibxm.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.command.CommandTitle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import scala.remote;


@Mod.EventBusSubscriber(modid = TdmMod.MODID)

public class Stages {

    private GameStage stage;
    public static Stages instance;
    private int teamCount;
    public static int GetTeamcount() {
        return instance.teamCount;
    }
    public static GameStage getStage() {return instance.stage;}
    private ArrayList<player> players;
    private Bounds[] boundsPerTeam; //Сохранять порядок как у teamState
    private int counter;
    private boolean[] teamsStarted;
    private boolean[] teamsDead;
    private BlockPos startSpawnZagon;
    private Bounds dangerZone;
    float dangerMultipler;
    boolean isSomebodyInsideDangerZone = false;
    private boolean dgZoneOpen;
    private ArrayList<player> playersInsideDangerZone;
    private GameEvent[] events;
    private BlockPos[] bedPositions;
    private int time;
    private int instanceCount;


    public Stages() {
        instanceCount++;
        counter = 0;
        instance = this;
        teamCount = 4;
        stage = GameStage.waitForCommand;
        boundsPerTeam = new Bounds[6];
        /* MAP OSTRAU
        boundsPerTeam[0] = new Bounds(-1280,-1280, 0, -70);
        boundsPerTeam[1] = new Bounds(-1280, 1280, 0, -70);
        boundsPerTeam[2] = new Bounds(1280, -1280, 0, -70);
        boundsPerTeam[3] = new Bounds(1280, 1280, 0, -70);
        */
        // MAP MEDIUM
        boundsPerTeam[0] = new Bounds(-630,-630, 0, -0);
        boundsPerTeam[1] = new Bounds(-630, 630, 0, -0);
        boundsPerTeam[2] = new Bounds(630, -630, 0, -0);
        boundsPerTeam[3] = new Bounds(630, 630, 0, -0);
        
        teamsStarted = new boolean[7];
        bedPositions = new BlockPos[7];
        teamsDead = new boolean[7];
        /* MAP OSTRAU
        dangerZone = new Bounds(new BlockPos(-380, 0, -500), new BlockPos(440, 255, 320));
        */

        // MAP MEDIUM 
        dangerZone = new Bounds(new BlockPos(-74, 0, -74), new BlockPos(74, 255, 74));

        playersInsideDangerZone = new ArrayList<player>();

        events = new GameEvent[8];
        float timescale = 1;

        events[0] = new GameEvent((int)(3600 * timescale), "Открытие границ", "Границы открыты.", () -> {stage = GameStage.borderRemove;});
        events[1] = new GameEvent((int)(4200 * timescale), "Первое открытие центра (на 2 минуты)", "Центр открыт.", "На 2 минуты.", () -> OpenCenter(true));
        events[2] = new GameEvent((int)(4320 * timescale), "Закрытие центра", "Центр закрыт.", () -> OpenCenter(false));
        events[3] = new GameEvent((int)(5000 * timescale), "Открытие центра (на 3 минуты)", "Центр открыт.", "На 3 минуты.", () -> OpenCenter(true));
        events[4] = new GameEvent((int)(5180 * timescale), "Закрытие центра", "Центр закрыт.", () -> OpenCenter(false));
        events[5] = new GameEvent((int)(6000 * timescale), "Открытие центра (на 4 минуты)", "Центр открыт.", "На 4 минуты.", () -> OpenCenter(true));
        events[6] = new GameEvent((int)(6240 * timescale), "Закрытие центра", "Центр закрыт.", () -> OpenCenter(false));
        events[7] = new GameEvent((int)(7000 * timescale), "Финал", "Копать можно везде.", () -> {stage = GameStage.finale;});

    }

    public void Load () {
        stage = GameStage.waitForPlayers;
        players = Teams.instance.GetPlayers();
        TitleHandler.SendTitleForAll("Ожидание игроков.", "", "grey");

        //MAP MEDIUM
        startSpawnZagon = new BlockPos(5, 76, 505);
    }
    public void Start () {
        if (stage == GameStage.waitForPlayers) {
            stage = GameStage.prepare;
            /* MAP OSTRAU
            TransportPlayersAndGiveOneBed(teamState.blue, new BlockPos(-730, 119, -862));
            TransportPlayersAndGiveOneBed(teamState.red, new BlockPos(-788, 77, 804));
            TransportPlayersAndGiveOneBed(teamState.green, new BlockPos(800, 87, -880));
            TransportPlayersAndGiveOneBed(teamState.yellow, new BlockPos(830, 64, 683));
            */

            //MAP MEDIUM
            TransportPlayersAndGiveOneBed(teamState.blue, new BlockPos(-325, 75, -298));
            TransportPlayersAndGiveOneBed(teamState.red, new BlockPos(-292, 67, 289));
            TransportPlayersAndGiveOneBed(teamState.green, new BlockPos(293, 69, -319));
            TransportPlayersAndGiveOneBed(teamState.yellow, new BlockPos(388, 70, 302));

            TitleHandler.SendTitleForAll("Игра началась.", "установите кровать", "white");
            TdmMod.logger.info("AAAAPAMAGI " + instanceCount);
        }
    }
    private void TransportPlayersAndGiveOneBed (teamState team, BlockPos position) {
        player[] teamPlayers = Teams.GetPlayersOfTeam(team);
        if (teamPlayers.length == 0) return;

        for (player Player : teamPlayers) {
            EntityPlayer entity = Player.playerEntity;
            entity.setPositionAndUpdate(position.getX(), position.getY(), position.getZ());
            entity.inventory.clear();
        }

        //Кровать
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagList tag = new NBTTagList();
        tag.appendTag(new NBTTagString("minecraft:grass"));
        tag.appendTag(new NBTTagString("minecraft:dirt"));
        tag.appendTag(new NBTTagString("minecraft:stone"));
        tag.appendTag(new NBTTagString("minecraft:cobblestone"));
        compound.setTag("CanPlaceOn", tag);
        ItemStack bedStack = new ItemStack(Item.getByNameOrId("minecraft:bed"));
        bedStack.setTagCompound(compound);
        teamPlayers[0].playerEntity.addItemStackToInventory(bedStack);
        //Конец кровати
    }

    public static int getTime () {
        if (instance == null) return -1;
        return instance.time;
    }

    public static boolean getTeamStarted (teamState team) {
        if (instance == null) return false;
        return instance.teamsStarted[team.ordinal()];
    }

    public BlockPos getBedPos (teamState team) {
        if (bedPositions == null || team.ordinal() >= bedPositions.length) return null;
        else {
            return bedPositions[team.ordinal()];
        }
    }

    @SubscribeEvent 
    public static void onWorldTick (WorldTickEvent event) {
        if (event.world.provider.getDimension() != 0 || event.side == Side.CLIENT || getStage().ordinal() < 2) return;
        if (instance.counter < 40) instance.counter++;
        else {
            instance.CheckPlayersBounds();
            if (instance.stage != GameStage.finale)
                instance.CheckDangerZone();
            instance.CheckBedTeam(event.world);
            //Logging events
            int i = 0;
            while (i < instance.events.length && instance.time > instance.events[i].time) i++;
             GameEvent nextEvent = instance.events[i];
                int timeLeft = nextEvent.time - instance.time;
                if (timeLeft == 300 || (timeLeft <= 5 && timeLeft != 0) || timeLeft == 900 || timeLeft == 1800 || timeLeft == 3600 || timeLeft == 30 || timeLeft == 10) {
                    TdmMod.currentServer.getPlayerList().sendMessage(new TextComponentString("§b" + nextEvent.desc + " через §a" + instance.GetFormattedTime(nextEvent.time - instance.time)));
                }
                if (timeLeft == 60) {
                    TitleHandler.SendTitleForAll(nextEvent.desc, "через §a 1 минуту", "aqua");
                }
                if (timeLeft == 0) {
                    TitleHandler.SendTitleForAll(nextEvent.result, nextEvent.subtext, "aqua");
                    if (nextEvent.onEvent != null) nextEvent.onEvent.Do();
                }
            //EndLogging
            instance.time++;
            instance.counter = 0;
        }
    }
        

    @SubscribeEvent
    public static void BlockPlaced (BlockEvent.PlaceEvent event) {
        if (instance.stage.ordinal() < 2 || event.getPlayer() == null || event.getWorld().provider.isNether())return;
        if (event.getPlayer().capabilities.allowEdit == false && instance.teamsStarted[Teams.GetTeamOfPlayer(event.getPlayer()).ordinal()]) {event.setCanceled(true); return;}
        //event.getEntity().sendMessage(new TextComponentString(String.valueOf(event.getItemInHand().getTagCompound().getTag("CanPlaceOn").getTagTypeName(0))));
        if (event.getPlacedBlock().getBlock().getUnlocalizedName().equals("tile.bed") 
        && event.getEntity() instanceof EntityPlayer
        && Teams.GetTeamOfPlayer(event.getPlayer()) != teamState.specs
        && instance.stage == GameStage.prepare
        && !instance.teamsStarted[Teams.GetTeamOfPlayer((EntityPlayer)(event.getEntity())).ordinal()])
            instance.StartTeam(event);
    }

    @SubscribeEvent
    public static void BlockDestroyed (BlockEvent.BreakEvent event) {
        if (instance.stage.ordinal() < 2 || event.getWorld().provider.isNether()) return;
        if (event.getPlayer().capabilities.allowEdit == false) {event.setCanceled(true); return;}
        if (event.getState().getBlock().getUnlocalizedName().equals("tile.bed")) {
            instance.TeamBedBroken(event);
            TdmMod.logger.info("teamBedDestroyed");
        }
    }

    @SubscribeEvent
    public static void LivingDead (LivingDeathEvent e) {
        if (e.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer plr = (EntityPlayer)e.getEntityLiving();
            player p = Teams.GetPlayer(plr);
            if (!instance.teamsStarted[p.team.ordinal()]) {
                p.dead = true;
            }
            boolean b = true;
            player[] teamPlayers = Teams.GetPlayersOfTeam(p.team);
            for (player tp : teamPlayers) {
                if (!tp.dead) b = false;
            }
            if (b) {
                for (player tp : teamPlayers) if (tp.playerEntity != null) {
                    tp.playerEntity.setGameType(GameType.SPECTATOR);
                    tp.playerEntity.sendMessage(new TextComponentString("§lВаша команда потерпела поражение."));
                }
                instance.teamsDead[p.team.ordinal()] = false;
                int aliveCount = 0;
                int alive = 0;
                for (int i = 0; i < instance.teamCount; i++) {
                    if (instance.teamsDead[i] == false) {aliveCount++; alive = i;}
                }
                if (aliveCount > 1)
                    TitleHandler.SendTitleForAll("Команда " + Teams.GetTeamColorSymbols(p.team) + p.team.toString() + " уничтожена.",
                    "Осталось " + aliveCount + "команд" + instance.correctEnd(aliveCount), "white");
                else {
                    TitleHandler.SendTitleForAll("Команда " + Teams.GetTeamColorSymbols(teamState.values()[alive]) + teamState.values()[alive].toString() + " победила!",
                    "ПОБЕДАААА УООООООООООРЯЯЯЯЯЯ", "white");
                }
            }
        }
    }

    //НЕ ПОДПИСЫВАТЬСЯ (вызов из Teams)
    public void PlayerRespawnPosCorrect (PlayerRespawnEvent event, player p) {
        if (p.team == teamState.specs || stage.ordinal() <= 1) return;
        if (teamsStarted[p.team.ordinal()]) {
            BlockPos pos = bedPositions[p.team.ordinal()];
            TileEntity te;
            
            BlockPos dir = ((te = p.playerEntity.world.getTileEntity(pos)) instanceof TileEntityBed) ? new BlockPos(te.getWorld().getBlockState(pos).getValue(net.minecraft.block.BlockHorizontal.FACING).getDirectionVec()) : new BlockPos(0,0,0);
            pos = pos.subtract(dir);
            p.playerEntity.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
        }
        else {
            p.playerEntity.setPositionAndUpdate(startSpawnZagon.getX(), startSpawnZagon.getY(), startSpawnZagon.getZ());
            boolean b = true;
            player[] teamPlayers = Teams.GetPlayersOfTeam(p.team);
            for (player tp : teamPlayers) {
                if (!tp.dead) b = false;
            }
            if (b) {
                for (player tp : teamPlayers) if (tp.playerEntity != null) {
                    tp.playerEntity.setGameType(GameType.SPECTATOR);
                }
            }
        }
    }
    
    //НЕ ПОДПИСЫВАТЬСЯ
    private void TeamBedBroken (BlockEvent.BreakEvent event) {
        boolean remote = event.getWorld().isRemote;
        teamState teamDeadBed = Teams.GetTeamFromMetadata(((TileEntityBed)(event.getWorld().getTileEntity(event.getPos()))).getColor().ordinal());
        if (teamDeadBed == teamState.specs || !teamsStarted[teamDeadBed.ordinal()] )return;
        if (!remote) {
            String cause = "не человек";
            String colyr = "";
            if (event.getPlayer() != null) {
                cause = Teams.GetTeamColorSymbols(event.getPlayer()) + event.getPlayer().getName();
                colyr = teamDeadBed.forTitleColor();
            }

            TitleHandler.SendTitleForAll("Кровать команды " 
            + teamDeadBed.toString()
            + " уничтожена.", "Её поломал " + cause, colyr);

            TitleHandler.SendTitleForTeam("Ваша кровать уничтожена.", "Её поломал "
            + cause, colyr, teamDeadBed);

            for (player Player : Teams.GetPlayersOfTeam(teamDeadBed)) {
                try {
                    Player.playerEntity.setSpawnPoint(instance.startSpawnZagon, true);
                }
                catch (Exception e) {
                    TdmMod.logger.info(e);
                }
            }
            teamsStarted[teamDeadBed.ordinal()] = false;
        }
        else
            Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(2, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), 1, 1, 1);
    }

    private void CheckBedTeam (World w) {
        for (int i = 0; i < teamsStarted.length; i++) {
            if (teamsStarted[i]) {
                TileEntity bed = w.getTileEntity(bedPositions[i]);
                if (!(bed instanceof TileEntityBed)) {
                    teamsStarted[i] = false;
                    teamState teamDeadBed = teamState.values()[i];
                    String colyr = teamDeadBed.forTitleColor();
                    TitleHandler.SendTitleForAll("Кровать команды " 
                    + teamDeadBed.toString()
                    + " уничтожена.", "Взрывом или зомбем", colyr);
                }
            }
        }
    }

    private void StartTeam (BlockEvent.PlaceEvent event) {
        boolean remote = event.getWorld().isRemote;
        teamState team = Teams.GetTeamOfPlayer(event.getPlayer());
        if (teamsStarted[team.ordinal()]) return;
        BlockPos spawnPos = event.getPos();
        bedPositions[team.ordinal()] = spawnPos;
        ParticleManager serega = null;
        for (player p : Teams.GetPlayersOfTeam(team)) {
            if (p.dead == true) {
                p.playerEntity.setGameType(GameType.SURVIVAL);
                p.playerEntity.setPositionAndUpdate(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                p.dead = false;
            }
        }
        if (remote) {
            serega = Minecraft.getMinecraft().effectRenderer;
        }

        for (player Player : Teams.GetPlayersOfTeam(team)) {
            if (!remote) {
                EntityPlayer entity = Player.playerEntity;
                entity.setGameType(GameType.SURVIVAL);
                entity.setSpawnPoint(spawnPos, true);
            }
        }

        teamsStarted[team.ordinal()] = true;
        BlockPos dir = spawnPos.offset(event.getWorld().getBlockState(event.getPos()).getBlock().getBedDirection(event.getWorld().getBlockState(event.getPos()), event.getWorld(), event.getPos()));
        if (remote)
            serega.spawnEffectParticle(2, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 1, 1, 1);
        if (!remote) {
            TileEntityBed bedEntity = (TileEntityBed)event.getWorld().getTileEntity(spawnPos);
            EnumDyeColor teamColor = Teams.GetTeamColor(team);
            bedEntity.setColor(teamColor);
            bedEntity = (TileEntityBed)event.getWorld().getTileEntity(dir);
            bedEntity.setColor(teamColor);
        }
        if (remote)
            serega.spawnEffectParticle(2, dir.getX(), dir.getY(), dir.getZ(), 1, 1, 1);
        if (!remote)
            TitleHandler.SendTitleForTeam("Кровать установлена.", "можно играть", Teams.GetTeamColor(team).toString(), team);
        
    }

    private void CheckDangerZone () {
        for (player Player : players) {
            boolean contains = playersInsideDangerZone.contains(Player);
            if (!Player.playerEntity.world.provider.isNether() && dangerZone.insideMe(Player.playerEntity.getPosition())) {
                if (!contains && !Player.playerEntity.capabilities.isCreativeMode && !Player.playerEntity.capabilities.isFlying) {
                    playersInsideDangerZone.add(Player);
                    Player.playerEntity.capabilities.allowEdit = false;
                    Player.playerEntity.setGameType(GameType.ADVENTURE);
                    Player.playerEntity.sendMessage(new TextComponentString("Вы входите центральную зону"));
                }
            }
            else if (contains) {
                playersInsideDangerZone.remove(Player);
                Player.playerEntity.capabilities.allowEdit = true;
                Player.playerEntity.setGameType(GameType.SURVIVAL);
                Player.playerEntity.sendMessage(new TextComponentString("Вы покидаете центральную зону"));
            }
        }
        dangerMultipler = playersInsideDangerZone.size() > 0 ? dangerMultipler * 1.5f : 1;
        if (!dgZoneOpen) {
            for (player Player : playersInsideDangerZone) {
                Player.playerEntity.attackEntityFrom(DamageSource.OUT_OF_WORLD, dangerMultipler);
                Player.playerEntity.sendMessage(new TextComponentString("§l§4Зона закрыта для посещения."));
            }
        }
    }

    private void OpenCenter (boolean open) {
        dgZoneOpen = open;
        if (open) TitleHandler.SendTitleForAll("Центр открыт.", "можно играть", "aqua");
        else TitleHandler.SendTitleForAll("Центр закрыт.", "можно играть", "aqua");
    }

    private void CheckPlayersBounds () {
        for (player Player : players) {
            if (Player.playerEntity.world.provider.isNether()) continue;
            if (!IsPlayerInBounds(Player)) {
                String zone = "далеко далеко";
                teamState[] teams = teamState.values();
                for (int i = 0; i < boundsPerTeam.length; i++) {
                    if (boundsPerTeam[i] != null)
                        zone = boundsPerTeam[i].insideMe(Player.playerEntity.getPosition()) ? teams[i].toString() : zone;
                }
                BlockPos destination = boundsPerTeam[Player.team.ordinal()].GetClosestInBounds(Player.playerEntity.getPosition());
                if (stage == GameStage.prepare && !Player.dead || zone.equals("далеко далеко")) {
                    if (Player.playerEntity.isRiding()) {
                        Player.playerEntity.getRidingEntity().setPositionAndUpdate(destination.getX(), destination.getY(), destination.getZ());
                    }
                    else
                        Player.playerEntity.setPositionAndUpdate(destination.getX(), destination.getY(), destination.getZ());
                    Player.playerEntity.sendMessage(new TextComponentString("Зона " + zone + " в данный момент закрыта."));
                }
                else {
                    if (Player.playerEntity.capabilities.allowEdit == true && stage != GameStage.finale) {
                        Player.playerEntity.capabilities.allowEdit = false;
                        Player.playerEntity.setGameType(GameType.ADVENTURE);
                        Player.playerEntity.sendMessage(new TextComponentString("Вы находитесь в зоне " + zone + ", копание забрано."));
                    }
                }
                if (stage == GameStage.finale && Player.playerEntity.capabilities.allowEdit == false) {
                    Player.playerEntity.capabilities.allowEdit = true;
                    Player.playerEntity.setGameType(GameType.SURVIVAL);
                    Player.playerEntity.sendMessage(new TextComponentString("Финал, копать можно везде."));
                }
            }
            else {
                if (Player.playerEntity.capabilities.allowEdit == false 
                && (!dangerZone.insideMe(Player.playerEntity.getPosition()) || stage == GameStage.finale) 
                && teamsStarted[Player.team.ordinal()]) {
                    Player.playerEntity.capabilities.allowEdit = true;
                    Player.playerEntity.setGameType(GameType.SURVIVAL);
                    Player.playerEntity.sendMessage(new TextComponentString("Вы вернулись на свою территорию, копание возвращено."));
                }
            }
        }
    }

    public void Skip (int skipAmount) {
        instance.time += skipAmount;
        TdmMod.logger.info(instance.time);
    }

    public static void ShowEventsInfo (EntityPlayer player) {
        String info;
        int i = 0;
        while (i < instance.events.length && instance.time > instance.events[i].time) i++;
        GameEvent nextEvent = instance.events[i];
        info = "Следующее событие §b" + nextEvent.desc;
        player.sendMessage(new TextComponentString(info));
        info = "через §a" + instance.GetFormattedTime(nextEvent.time - instance.time);
        player.sendMessage(new TextComponentString(info));
    }

    private String GetFormattedTime (int time) {
        String result;
        int hours = time / 3600;
        int minutes = time % 3600 / 60;
        int seconds =  time % 60;
        result = "";
        if (hours > 0)
            result = "час";
        if (minutes > 0)
            result += minutes + " минут" + correctEnd(minutes) + " ";
        if (seconds > 0)
            result += seconds + " секунд" + correctEnd(seconds);
        return result;

    }

    private String correctEnd (int num) {
        String stringged = String.valueOf(num);
        if (stringged.length() == 2 && stringged.charAt(0) == '1') return "";
        switch (stringged.charAt(stringged.length()-1)) {
            case '1':
                return "а";
            case '2':
                return "ы";
            case '3':
                return "ы";
            case '4':
                return "ы";
            default:
                return "";
        }
    }

    private boolean IsPlayerInBounds (player p) {
        if (p.team.ordinal() < boundsPerTeam.length)
            return boundsPerTeam[p.team.ordinal()].insideMe(p.playerEntity.getPosition());
        else return true;
    }

    public enum GameStage {
        waitForCommand,
        waitForPlayers,
        prepare,
        borderRemove,
        secondDgZoneOpen,
        thirdDgZoneOpen,
        finale
    }

    public class Bounds {
        BlockPos min;
        BlockPos max;

        public Bounds (int x1,int z1, int x2, int z2) {
            this(new BlockPos(x1, 0, z1), new BlockPos(x2, 255, z2));
        }

        public Bounds (BlockPos p1, BlockPos p2) {
            int[] xG = maxMinGrouped(p1.getX(), p2.getX());
            int[] yG = maxMinGrouped(p1.getY(), p2.getY());
            int[] zG = maxMinGrouped(p1.getZ(), p2.getZ());
            min = new BlockPos(xG[0],yG[0], zG[0]);
            max = new BlockPos(xG[1],yG[1], zG[1]);
        }

        public boolean insideMe (BlockPos pos) {
            boolean result = true;
            if (pos.getX() > max.getX() || pos.getX() < min.getX()) result = false;
            //if (pos.getY() > max.getY() || pos.getY() < min.getY()) result = false; NE NADOBNO
            if (pos.getZ() > max.getZ() || pos.getZ() < min.getZ()) result = false;
            return result;
        }

        private int[] maxMinGrouped (int i1, int i2) {
            int[] result = new int[2]; // 0 - min 1 - max
            result[0] = i1 > i2 ? i2 : i1;
            result[1] = i1 > i2 ? i1 : i2;
            return result;
        }

        public BlockPos GetClosestInBounds (BlockPos pos) {
            int x = pos.getX();
            int z = pos.getZ();
            if (x < min.getX()) x = min.getX() + 1;
            if (x > max.getX()) x = max.getX() - 1;
            if (z < min.getZ()) z = min.getZ() + 1;
            if (z > max.getZ()) z = max.getZ() - 1;
            return new BlockPos(x, pos.getY(), z);
        }
    }

    public class GameEvent {
        public int time;
        public String desc;
        public String result;
        Action onEvent;
        String subtext;

        public GameEvent(int time, String desc, String result, String subtext) {
            this.time = time;
            this.desc = desc;
            this.result = result;
            this.subtext = subtext;
            
        }

        public GameEvent(int time, String desc, String result) {
            this.time = time;
            this.desc = desc;
            this.result = result;
            
        }

        public GameEvent (int time, String desc, String result, String subtext, Action onEvent) {
            this(time, desc, result, subtext);
            this.onEvent = onEvent;
        }

        public GameEvent (int time, String desc, String result, Action onEvent) {
            this(time, desc, result);
            this.onEvent = onEvent;
        }
    }

    public interface Action{
        void Do();
    }
}

