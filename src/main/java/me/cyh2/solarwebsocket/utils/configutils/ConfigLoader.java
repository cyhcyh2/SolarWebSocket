package me.cyh2.solarwebsocket.utils.configutils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import static me.cyh2.solarwebsocket.SolarWebSocket.plugin;

public class ConfigLoader {
    public static YamlConfiguration LOAD (String FileName) {
        return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + "/" + FileName));
    }
}
