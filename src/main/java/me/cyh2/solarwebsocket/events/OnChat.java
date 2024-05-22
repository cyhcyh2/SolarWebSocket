package me.cyh2.solarwebsocket.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import static me.cyh2.solarwebsocket.websocket.MainWebSocketServer.WSMap;

public class OnChat implements Listener {
    @EventHandler
    public void onChat (PlayerChatEvent e) {
        WSMap.keySet().forEach( str -> WSMap.get(str).send("[ 来自服务器 ] " + e.getPlayer().getName() + "：" + e.getMessage()));
    }
}