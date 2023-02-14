package ru.evdokimov.vkprotect.utils;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import java.util.HashMap;
import java.util.Map;

public class CooldownManager {

    private  final Map<ProtectedRegion, Integer> cooldowns = new HashMap<ProtectedRegion, Integer>();

    public static final int DEFAULT_COOLDOWN = 10;

    public void setCooldown(ProtectedRegion region, int time){
        if(time < 1) {
            cooldowns.remove(region);
        } else {
            cooldowns.put(region, time);
        }
    }

    public int getCooldown(ProtectedRegion rg){
        return cooldowns.getOrDefault(rg, 0);
    }
}
