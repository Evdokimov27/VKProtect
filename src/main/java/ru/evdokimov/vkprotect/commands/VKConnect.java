package ru.evdokimov.vkprotect.commands;

import com.ubivashka.vk.bukkit.BukkitVkApiPlugin;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.evdokimov.vkprotect.Main;

import java.util.Random;

public class VKConnect  implements CommandExecutor {

    private final Plugin plugin = Main.getPlugin(Main.class);
    private static final VkApiClient CLIENT = BukkitVkApiPlugin.getPlugin(BukkitVkApiPlugin.class).getVkApiProvider()
            .getVkApiClient();
    private static final GroupActor ACTOR = BukkitVkApiPlugin.getPlugin(BukkitVkApiPlugin.class).getVkApiProvider()
            .getActor();
    private final static Random RANDOM = new Random();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        plugin.reloadConfig();
        Player p = (Player)sender;
        if(args.length == 1)
        {
            plugin.getConfig().set(p.getUniqueId()+".vk_id", Integer.parseInt(args[0]));
            plugin.saveConfig();
           try {
               CLIENT.messages().send(ACTOR).randomId(RANDOM.nextInt()).peerId(plugin.getConfig().getInt(p.getUniqueId()+".vk_id")).message("Ваш ВК успешно привязан!").execute() ; // https://vk.com/dev/messages.send

           } catch (ApiException | ClientException ex) {
               ex.printStackTrace();
           }
        }
        else p.sendMessage("Неверный аргумент");
        return true;
    }
}
