package ru.evdokimov.vkprotect.events;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.PlayerDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.ubivashka.vk.bukkit.BukkitVkApiPlugin;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;
import ru.evdokimov.vkprotect.Main;
import ru.evdokimov.vkprotect.utils.CooldownManager;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.Bukkit.getServer;

public class EntityExplodeEvents implements Listener {
    Plugin plugin = Main.getPlugin(Main.class);
    FileConfiguration config = plugin.getConfig();
    private final CooldownManager cooldownManager = new CooldownManager();

    private static final VkApiClient CLIENT = BukkitVkApiPlugin.getPlugin(BukkitVkApiPlugin.class).getVkApiProvider()
            .getVkApiClient();
    private static final GroupActor ACTOR = BukkitVkApiPlugin.getPlugin(BukkitVkApiPlugin.class).getVkApiProvider()
            .getActor();
    private final static Random RANDOM = new Random();
    WorldGuardPlugin wg = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");

    @EventHandler
    public void blockBreak(EntityExplodeEvent event) throws InterruptedException {

        Location location = event.getLocation();
        ApplicableRegionSet set = Objects.requireNonNull(WorldGuard.getInstance().getPlatform().getRegionContainer().get(new BukkitWorld(location.getWorld()))).getApplicableRegions(BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ()));


        if (set.size() > 0) {
            for (ProtectedRegion rg : set.getRegions()) {
                String str = rg.getId();
                Pattern p = Pattern.compile(".*prot(?=ect_).*");
                Matcher m = p.matcher(rg.getId());
                if (m.matches()) {
                } else
                {
                    for (UUID player : rg.getOwners().getPlayerDomain().getUniqueIds()) {
                        Integer peerID = plugin.getConfig().getInt(player + ".vk_id");
                        if (peerID != null) {

                            int timeLeft = cooldownManager.getCooldown(rg);
                            if (timeLeft == 0) {
                                try {
                                    cooldownManager.setCooldown(rg, CooldownManager.DEFAULT_COOLDOWN);
                                    CLIENT.messages().send(ACTOR).randomId(RANDOM.nextInt()).peerId(peerID).message("Ваша база атакуется!").execute(); // https://vk.com/dev/messages.send
                                } catch (ApiException | ClientException ex) {
                                    ex.printStackTrace();
                                }
                                {

                                }
                            }
                            else {
                                cooldownManager.setCooldown(rg, --timeLeft);
                            }
                        }
                    }
                }
            }
        }
    }
}

