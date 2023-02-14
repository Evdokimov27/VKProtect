package ru.evdokimov.vkprotect;


import ru.evdokimov.vkprotect.commands.VKConnect;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import ru.evdokimov.vkprotect.events.*;

import java.io.File;
import java.io.IOException;

public final class Main extends JavaPlugin {
    private static Main instance;
    public ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    private Storage data;

    @Override
    public void onEnable() {
        instance = this;
        data = new Storage("config.yml");
        this.registerCommands();
        this.registerEvents();
    }
    public static Storage getData() {
        return instance.data;
    }
//Сохранить конфиг


    public static Main getInstance() {
        return instance;
    }

    private void registerCommands() {
        this.getCommand("vkconnect").setExecutor(new VKConnect());
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new EntityExplodeEvents(), this);
        Bukkit.getPluginManager().registerEvents(new VKMessageEvents(), this);

    }



    @Override
    public void onDisable() {

    }
}
