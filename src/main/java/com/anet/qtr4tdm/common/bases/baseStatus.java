package com.anet.qtr4tdm.common.bases;

import net.minecraft.entity.monster.EntityZombie;

public enum baseStatus {
    Peace,
    Threat,
    Emergency;

    @Override
    public String toString () {
        switch (this) {
            case Peace:
                return "§aПокой";
            case Threat:
                return "§6Угроза";
            case Emergency:
                return "§cТревога";
            default: return "unknown";
        }
    }
}
