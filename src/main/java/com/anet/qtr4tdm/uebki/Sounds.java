package com.anet.qtr4tdm.uebki;

import com.anet.qtr4tdm.TdmMod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;


public class Sounds
{
    //Это наш звук, `test_sound` это название звука указанного в sounds.json
    public static final SoundEvent system_active = reg("system_active");
    public static final SoundEvent pidoras_naiden = reg("pidoras_found");
    public static final SoundEvent beep = reg("beep");
    public static final SoundEvent active_protection = reg("active_protection");
    public static final SoundEvent active_protection_shot = reg("active_protection_shot");

    public static void regSound()
    {
        //Регистрация звука
        TdmMod.logger.info("sound reg");
        ForgeRegistries.SOUND_EVENTS.register(system_active);
        ForgeRegistries.SOUND_EVENTS.register(pidoras_naiden);
        ForgeRegistries.SOUND_EVENTS.register(beep);
        ForgeRegistries.SOUND_EVENTS.register(active_protection);
        ForgeRegistries.SOUND_EVENTS.register(active_protection_shot);
    }

    //Упрощённая регистрация звука
    private static SoundEvent reg(String name)
    {
        ResourceLocation rl = new ResourceLocation(TdmMod.MODID, name);
        return new SoundEvent(rl).setRegistryName(name);
    }
}
