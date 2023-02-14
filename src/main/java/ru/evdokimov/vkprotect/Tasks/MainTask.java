package ru.evdokimov.vkprotect.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.evdokimov.vkprotect.Main;

public class MainTask {
    public static void setHelmet(String nickname, Integer VKID){
        Player player = Bukkit.getPlayer(nickname);
        Bukkit.getServer().getScheduler().runTask(Main.getInstance(), (task) -> {
             player.sendMessage(VKID+" - " + player.getName());
        });
    }

    
}
