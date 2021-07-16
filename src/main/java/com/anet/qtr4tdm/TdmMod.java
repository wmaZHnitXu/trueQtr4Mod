package com.anet.qtr4tdm;

import java.nio.file.Path;

import com.anet.qtr4tdm.commands.AdminManagementCommands;
import com.anet.qtr4tdm.common.bases.InWorldBasesManager;
import com.anet.qtr4tdm.init.BlocksInit;
import com.anet.qtr4tdm.uebki.IDSmanager;
import com.anet.qtr4tdm.uebki.RadarsInfo;
import com.anet.qtr4tdm.uebki.Sounds;
import com.anet.qtr4tdm.uebki.SqlHelper;
import com.anet.qtr4tdm.uebki.messages.AskForRadarHandler;
import com.anet.qtr4tdm.uebki.messages.AskForRadarsMessage;
import com.anet.qtr4tdm.uebki.messages.BasedAnswer;
import com.anet.qtr4tdm.uebki.messages.BasedAnswerHandler;
import com.anet.qtr4tdm.uebki.messages.BasedRequest;
import com.anet.qtr4tdm.uebki.messages.BasedRequestHandler;
import com.anet.qtr4tdm.uebki.messages.RadarInfoHandler;
import com.anet.qtr4tdm.uebki.messages.RadarMessage;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;


@Mod(modid = TdmMod.MODID, name = TdmMod.NAME, version = TdmMod.VERSION)
public class TdmMod
{
    public static final String MODID = "qtr4tdm";
    public static final String NAME = "Qtr4 tdm extension";
    public static final String VERSION = "1.0";
    public static TdmMod instance;
    //Не нашел статического поля с сервером => создал
    public static MinecraftServer currentServer;
    public static Logger logger;
    public static SimpleNetworkWrapper wrapper;
    public static String cfgPath;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        instance = this;
        logger = event.getModLog();
        cfgPath = event.getModConfigurationDirectory().getAbsolutePath();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        BlocksInit.RegisterTileEntities();
        BlocksInit.RegisterEntities();
        Sounds.regSound();
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel("myChannel");

        //wrapper.registerMessage(AskForPlayersHandler.class, PlayersAskMessage.class, 1, Side.CLIENT);
        //wrapper.registerMessage(AnswerPlayersHandler.class, PlayersAnswerMessage.class, 2, Side.SERVER);

        wrapper.registerMessage(RadarInfoHandler.class, RadarMessage.class, 3, Side.SERVER);

        wrapper.registerMessage(BasedRequestHandler.class, BasedRequest.class, 4, Side.SERVER);
        wrapper.registerMessage(BasedAnswerHandler.class, BasedAnswer.class, 5, Side.CLIENT);

        wrapper.registerMessage(AskForRadarHandler.class, AskForRadarsMessage.class, 6, Side.SERVER);
    }

    @EventHandler
    public void initServerStart(FMLServerStartingEvent event)
    {
        logger.info("initalise FMLServerStartingEvent :" + NAME);
        currentServer = event.getServer();
        event.registerServerCommand(new AdminManagementCommands());
        new RadarsInfo();
        new IDSmanager();
        new InWorldBasesManager();
        new SqlHelper(cfgPath + "/baseinfo.cfg");
    }

    @EventHandler
    public void Stopping (FMLServerStoppingEvent event) {
        SqlHelper.CloseConnection();
    }
    
    public static final CreativeTabs qtr4 = new CreativeTabs("qtr4") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Item.getByNameOrId("qtr4tdm:terminal_radar"));
        }
    };
}
