package me.cyh2.bungee.solarwebsocket;

import me.cyh2.bungee.solarwebsocket.events.OnPlayerChat;
import me.cyh2.bungee.solarwebsocket.events.OnPlayerJoin;
import me.cyh2.bungee.solarwebsocket.events.OnPlayerQuit;
import me.cyh2.bungee.solarwebsocket.httpserver.NettyHttpServer;
import me.cyh2.bungee.solarwebsocket.websocket.MainWebSocket;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

import static me.cyh2.bungee.solarwebsocket.utils.textutils.ColorUtils.ReColor;
import static me.cyh2.bungee.solarwebsocket.utils.textutils.getServers.Servers;
import static me.cyh2.html.HtmlReader.LoadHtmlFile;

public final class SolarWebSocketBungee extends Plugin {
    public static Logger logger;
    public static ProxyServer proxyServer;
    public static Plugin plugin;
    public static Configuration msgConfig;
    public static Configuration config;
    public static StringBuilder index_html;
    public static StringBuilder chat_html;
    public static String ServerSelect;
    public static MainWebSocket WSServer;
    public static NettyHttpServer nettyHttpServer;
    @Override
    public void onEnable() {
        logger = getLogger();
        proxyServer = getProxy();
        plugin = this;
        saveResource("message.yml", false);
        saveResource("config.yml", false);
        saveResource("index.html", false);
        saveResource("chat.html", false);
        ServerSelect = Servers(proxyServer);
        msgConfig = loadConfig("message.yml");
        config = loadConfig("config.yml");
        index_html = LoadHtmlFile(getDataFolder() + "/index.html");
        chat_html = LoadHtmlFile(getDataFolder() + "/chat.html");
        WSServer = new MainWebSocket(config.getString("host"), config.getInt("port"));
        WSServer.start();
        nettyHttpServer = new NettyHttpServer(config.getString("http-host"), config.getInt("http-port"));
        nettyHttpServer.run();
        proxyServer.getPluginManager().registerListener(plugin, new OnPlayerChat());
        proxyServer.getPluginManager().registerListener(plugin, new OnPlayerJoin());
        proxyServer.getPluginManager().registerListener(plugin, new OnPlayerQuit());
        logger.info(ReColor("&aSolar&rWebSocketBungee启动成功了哦~"));
    }
    @Override
    public void onDisable() {
        try {
            WSServer.stop();
            nettyHttpServer.stop();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        logger.info(ReColor("&aSolar&rWebSocketBungee关闭成功了哦~"));
    }
    private void saveResource(String resourcePath, boolean replace) {
        File resourceFile = new File(getDataFolder(), resourcePath);
        if (!resourceFile.exists() || replace) {
            try {
                File dataFolder = new File(getDataFolder().getPath());
                if (!dataFolder.exists()) {
                    dataFolder.mkdir();
                }

                InputStream inputStream = getResourceAsStream(resourcePath);
                OutputStream outputStream = Files.newOutputStream(resourceFile.toPath());
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private Configuration loadConfig(String fileName) {
        File configFile = new File(getDataFolder(), fileName);
        Configuration configuration = null;

        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return configuration;
    }
}
