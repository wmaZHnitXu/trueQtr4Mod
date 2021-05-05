package com.anet.qtr4tdm.init;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.blocks.EnergyConsumerBlock;
import com.anet.qtr4tdm.common.blocks.MiniSiloBlock;
import com.anet.qtr4tdm.common.blocks.RadarBaseBlock;
import com.anet.qtr4tdm.common.blocks.RadarMasterBlock;
import com.anet.qtr4tdm.common.blocks.RadarSlaveBlock;
import com.anet.qtr4tdm.common.blocks.RadarSlaveCornerBlock;
import com.anet.qtr4tdm.common.blocks.RadarSlaveEdgeBlock;
import com.anet.qtr4tdm.common.blocks.TerminalRadarBlock;
import com.anet.qtr4tdm.common.entities.Radar1Entity;
import com.anet.qtr4tdm.common.entities.Radar2Entity;
import com.anet.qtr4tdm.common.entities.Radar3Entity;
import com.anet.qtr4tdm.common.entities.render.RenderRadar1;
import com.anet.qtr4tdm.common.entities.render.RenderRadar2;
import com.anet.qtr4tdm.common.entities.render.RenderRadar3;
import com.anet.qtr4tdm.common.items.rocketItem;
import com.anet.qtr4tdm.common.tiles.EnergyConsumerTile;
import com.anet.qtr4tdm.common.tiles.RadarBaseTile;
import com.anet.qtr4tdm.common.tiles.RadarWorkerTile;
import com.anet.qtr4tdm.common.tiles.TerminalRadarTile;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;

@Mod.EventBusSubscriber(modid = TdmMod.MODID)
public class BlocksInit {
    public static final Block RADAR = new RadarBaseBlock();
    public static final Block RADARM = new RadarMasterBlock();
    public static final Block RADARS = new RadarSlaveBlock();
    public static final Block RADARC = new RadarSlaveCornerBlock();
    public static final Block RADARE = new RadarSlaveEdgeBlock();
    public static final Block RADART = new TerminalRadarBlock();
    public static final Block ENERGYCONSUMER = new EnergyConsumerBlock();
    public static final Block MINISILO = new MiniSiloBlock();

    public static final Block[] BLOCKS = new Block[] {
    
            RADAR,
            RADARM,
            RADARS,
            RADARC,
            RADARE,
            RADART,
            ENERGYCONSUMER,
            MINISILO

    };

    public static final Item ROCKET = new rocketItem();

    public static final Item[] ITEMS = new Item[] {
        ROCKET
    };

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ITEMS);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {

        event.getRegistry().registerAll(BLOCKS);
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {

        event.getRegistry().registerAll(getItemBlocks(BLOCKS));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {

        setRenderForAll(BLOCKS);
        setRenderForItems(ITEMS);
    }

    private static Item[] getItemBlocks(Block... blocks) {

        Item[] items = new Item[blocks.length];

        for (int i = 0; i < blocks.length; i++) {
    
            items[i] = new ItemBlock(blocks[i]).setRegistryName(blocks[i].getRegistryName());
        }

        return items;
    }

    @SideOnly(Side.CLIENT)
    private static void setRenderForAll(Block... blocks) {

        for (int i = 0; i < blocks.length; i++) {
    
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blocks[i]), 0, new ModelResourceLocation(blocks[i].getRegistryName(), "inventory"));
        }
        RenderingRegistry.registerEntityRenderingHandler(Radar3Entity.class, new IRenderFactory() {
            @Override
            public Render createRenderFor(RenderManager manager) 
            {
                return new RenderRadar3(manager);
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(Radar2Entity.class, new IRenderFactory() {
            @Override
            public Render createRenderFor(RenderManager manager) 
            {
                return new RenderRadar2(manager);
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(Radar1Entity.class, new IRenderFactory() {
            @Override
            public Render createRenderFor(RenderManager manager) 
            {
                return new RenderRadar1(manager);
            }
        });
    }

    @SideOnly(Side.CLIENT)
    public static void setRenderForItems (Item... items) {
        for (Item item : items) {
            final ResourceLocation regName = item.getRegistryName();
            final ModelResourceLocation mrl = new ModelResourceLocation(regName, "inventory");
            ModelBakery.registerItemVariants(item, mrl);
            ModelLoader.setCustomModelResourceLocation(item, 0, mrl);
        }
    }

    public static void RegisterTileEntities () {

        GameRegistry.registerTileEntity(RadarWorkerTile.class, new ResourceLocation(TdmMod.MODID + ":" + "radarworkertile"));
        GameRegistry.registerTileEntity(RadarBaseTile.class, new ResourceLocation(TdmMod.MODID + ":" + "radarbasetile"));
        GameRegistry.registerTileEntity(TerminalRadarTile.class, new ResourceLocation(TdmMod.MODID + ":" + "terminalradartile"));
        GameRegistry.registerTileEntity(EnergyConsumerTile.class, new ResourceLocation(TdmMod.MODID + ":" + "energyconsumertile"));

    }

    public static void RegisterEntities () {
        EntityRegistry.registerModEntity(new ResourceLocation(TdmMod.MODID + ":" + "radar1"), Radar1Entity.class, "Radar1", 1335, TdmMod.instance, 150, 3, false);
        EntityRegistry.registerModEntity(new ResourceLocation(TdmMod.MODID + ":" + "radar2"), Radar2Entity.class, "Radar2", 1336, TdmMod.instance, 150, 3, false);
        EntityRegistry.registerModEntity(new ResourceLocation(TdmMod.MODID + ":" + "radar3"), Radar3Entity.class, "Radar3", 1337, TdmMod.instance, 150, 3, false);
    }
}
