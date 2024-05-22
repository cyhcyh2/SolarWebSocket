package me.cyh2.solarwebsocket;

import me.cyh2.solarwebsocket.commands.WebChat;
import me.cyh2.solarwebsocket.events.OnChat;
import me.cyh2.solarwebsocket.events.OnPlayerJoin;
import me.cyh2.solarwebsocket.events.OnPlayerQuit;
import me.cyh2.solarwebsocket.utils.textutils.ColorLogger;
import me.cyh2.solarwebsocket.websocket.MainWebSocketServer;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;

import static me.cyh2.solarwebsocket.utils.configutils.ConfigLoader.LOAD;
import static me.cyh2.solarwebsocket.utils.textutils.ColorUtils.ReColor;

public final class SolarWebSocket extends JavaPlugin {
    public static Server server;
    public static Logger logger;
    public static Plugin plugin;
    public static String PluginName;
    public static ColorLogger clogger;
    public static YamlConfiguration msgConfig;
    public static YamlConfiguration WSServerConfig;
    public static MainWebSocketServer WSServer;
    @Override
    public void onEnable() {
        server = getServer();
        logger = getLogger();
        plugin = getPlugin(getClass());
        PluginName = ReColor("&a&l「 Solar&rWebSocket &a&l」");
        clogger = new ColorLogger(PluginName);
        saveResource("message.yml", false);
        saveResource("config.yml", false);
        msgConfig = LOAD("message.yml");
        WSServerConfig = LOAD("config.yml");
        WSServer =  new MainWebSocketServer(WSServerConfig.getString("host"), WSServerConfig.getInt("port"));
        WSServer.start();
        server.getPluginManager().registerEvents(new OnChat(), plugin);
        server.getPluginManager().registerEvents(new OnPlayerJoin(), plugin);
        server.getPluginManager().registerEvents(new OnPlayerQuit(), plugin);
        getCommand("webchat").setExecutor(new WebChat());
        clogger.info("SolarWebSocket启动成功了哦~");
    }

    @Override
    public void onDisable() {
        try {
            WSServer.stop();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        clogger.info("SolarWebSocket关闭成功了哦~");
    }
}
