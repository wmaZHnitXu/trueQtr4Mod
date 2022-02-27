package com.anet.qtr4tdm;

import java.nio.file.Path;

import com.anet.qtr4tdm.commands.AdminManagementCommands;
import com.anet.qtr4tdm.common.bases.BaseBorderInfoSender;
import com.anet.qtr4tdm.common.bases.InWorldBasesManager;
import com.anet.qtr4tdm.init.BlocksInit;
import com.anet.qtr4tdm.uebki.IDSmanager;
import com.anet.qtr4tdm.uebki.RadarsInfo;
import com.anet.qtr4tdm.uebki.Sounds;
import com.anet.qtr4tdm.uebki.SqlHelper;
import com.anet.qtr4tdm.uebki.gui.GuiHandler;
import com.anet.qtr4tdm.uebki.messages.primitive.AskDefenceDataToPlayer;
import com.anet.qtr4tdm.uebki.messages.primitive.AskForRadarHandler;
import com.anet.qtr4tdm.uebki.messages.primitive.AskForRadarsMessage;
import com.anet.qtr4tdm.uebki.messages.primitive.BaseBorderHandler;
import com.anet.qtr4tdm.uebki.messages.primitive.BaseBorderMessage;
import com.anet.qtr4tdm.uebki.messages.primitive.BaseUpgradeHandler;
import com.anet.qtr4tdm.uebki.messages.primitive.BaseUpgradeMessage;
import com.anet.qtr4tdm.uebki.messages.primitive.BasedAnswer;
import com.anet.qtr4tdm.uebki.messages.primitive.BasedAnswerHandler;
import com.anet.qtr4tdm.uebki.messages.primitive.BasedRequest;
import com.anet.qtr4tdm.uebki.messages.primitive.BasedRequestHandler;
import com.anet.qtr4tdm.uebki.messages.primitive.DefDataHandler;
import com.anet.qtr4tdm.uebki.messages.primitive.DefDataRecHandler;
import com.anet.qtr4tdm.uebki.messages.primitive.DefenceDataToPlayer;
import com.anet.qtr4tdm.uebki.messages.primitive.GetBaseMembersHandler;
import com.anet.qtr4tdm.uebki.messages.primitive.GetBaseMembersMessage;
import com.anet.qtr4tdm.uebki.messages.primitive.RadarInfoHandler;
import com.anet.qtr4tdm.uebki.messages.primitive.RadarMessage;
import com.anet.qtr4tdm.uebki.messages.primitive.SetBaseMember;
import com.anet.qtr4tdm.uebki.messages.primitive.SetBaseMemberHandler;
import com.anet.qtr4tdm.uebki.messages.primitive.TopBarMessage;
import com.anet.qtr4tdm.uebki.messages.primitive.TopBarMessageHandler;
import com.anet.qtr4tdm.uebki.messages.primitive.baseInfoMessage;
import com.anet.qtr4tdm.uebki.messages.primitive.baseInfoMessageHandler;

import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.server.MinecraftServer;


@Mod(modid = TdmMod.MODID, name = TdmMod.NAME, version = TdmMod.VERSION)
@Mod.EventBusSubscriber(modid = TdmMod.MODID)
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

    public static final int GUI_BASE = 0;
    public static final int GUI_KAZ = 1;
    public static final int GUI_COMMONDEF = 2;
    public static final int GUI_DRONEHARVESTER = 3;
    public static final int GUI_TURRETMASTER = 4;

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
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel("myChannel");

        //wrapper.registerMessage(AskForPlayersHandler.class, PlayersAskMessage.class, 1, Side.CLIENT);
        //wrapper.registerMessage(AnswerPlayersHandler.class, PlayersAnswerMessage.class, 2, Side.SERVER);

        wrapper.registerMessage(RadarInfoHandler.class, RadarMessage.class, 3, Side.SERVER);

        wrapper.registerMessage(BasedRequestHandler.class, BasedRequest.class, 4, Side.SERVER);
        wrapper.registerMessage(BasedAnswerHandler.class, BasedAnswer.class, 5, Side.CLIENT);

        wrapper.registerMessage(AskForRadarHandler.class, AskForRadarsMessage.class, 6, Side.SERVER);

        wrapper.registerMessage(baseInfoMessageHandler.class, baseInfoMessage.class, 7, Side.CLIENT);

        wrapper.registerMessage(BaseUpgradeHandler.class, BaseUpgradeMessage.class, 8, Side.SERVER);
        
        wrapper.registerMessage(SetBaseMemberHandler.class, SetBaseMember.class, 9, Side.SERVER);

        wrapper.registerMessage(GetBaseMembersHandler.class, GetBaseMembersMessage.class, 10, Side.CLIENT);
        wrapper.registerMessage(TopBarMessageHandler.class, TopBarMessage.class, 11, Side.CLIENT);

        wrapper.registerMessage(DefDataHandler.class, AskDefenceDataToPlayer.class, 12, Side.SERVER);
        wrapper.registerMessage(DefDataRecHandler.class, DefenceDataToPlayer.class, 13, Side.CLIENT);

        wrapper.registerMessage(BaseBorderHandler.class, BaseBorderMessage.class, 14, Side.CLIENT);
    }

    @EventHandler
    public void initServerStart(FMLServerAboutToStartEvent event)
    {
        logger.info("initalise FMLServerStartingEvent :" + NAME);
        currentServer = event.getServer();
        new IDSmanager();
        new SqlHelper(cfgPath + "/baseinfo.cfg");
    }


    @EventHandler
    public void ServerStarting(FMLServerStartingEvent event)
    {
        logger.info("Server Starting");
        event.registerServerCommand(new AdminManagementCommands()); //ИНИЦИАЛИЗИРОВАТЬ ВСЕ МАНАГЕРЫ ХЕЛПЕРЫ ХАНДЛЕРЫ
        new BaseBorderInfoSender();
    }

    @SubscribeEvent
    public void WorldLoad (WorldEvent.Load event) {
        if (event.getWorld().provider.getDimension() == 0) {
            System.out.println("Main init"); //НЕ работает ничего, что способно выводить текст
            new InWorldBasesManager(); //да вообще не работает, пизда, этот блок кода не выполняется
            new PrivatesHandler(); //worldbasemanager тупо в basetile инициируется, когда первый тайл этого класса грузится
            new RadarsInfo(); //в приват хандлер инстанс не нужен и радарсинфо сам тоже не нужен, поэтому не крашится

        }
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
