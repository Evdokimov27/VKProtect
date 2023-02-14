package ru.evdokimov.vkprotect.events;

import com.ubivashka.vk.bukkit.BukkitVkApiPlugin;
import com.ubivashka.vk.bukkit.events.VKMessageEvent;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import ru.evdokimov.vkprotect.Main;

import java.util.Random;

public class VKMessageEvents implements Listener {
    private static final VkApiClient CLIENT = BukkitVkApiPlugin.getPlugin(BukkitVkApiPlugin.class).getVkApiProvider()
            .getVkApiClient();
    private static final GroupActor ACTOR = BukkitVkApiPlugin.getPlugin(BukkitVkApiPlugin.class).getVkApiProvider()
            .getActor();
    private final static Random RANDOM = new Random();
    Plugin plugin = Main.getInstance();
    FileConfiguration config = plugin.getConfig();

    @EventHandler
    public void onMessage(VKMessageEvent e) {

        new BukkitRunnable() {
            @Override
            public void run() {
                String command = "Никнейм:";
                if (!(e.getMessage().getText().startsWith(command)))
                    return;
                if (e.getMessage().getText().length() <= command.length())
                    return;
                String screenName = e.getMessage().getText().substring(command.length()).trim();
                try {
                    CLIENT.messages().send(ACTOR).randomId(RANDOM.nextInt()).peerId(e.getPeer()).message("Скопируйте данный текст и вставьте на сервер\n" +
                            "/vkconnect "+ e.getPeer()).execute() ; // https://vk.com/dev/messages.send

                } catch (ApiException | ClientException ex) {
                    ex.printStackTrace();
                }
            }
        }.runTaskTimer(this.plugin, 20, 20);

    }
}


