package me.cyh2.solarwebsocket.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static me.cyh2.solarwebsocket.websocket.MainWebSocketServer.WSMap;

public class OnPlayerQuit implements Listener {
    @EventHandler
    public void onPlayerQuit (PlayerQuitEvent e) {
        WSMap.keySet().forEach( str -> WSMap.get(str).send("玩家" + e.getPlayer().getName() + "离开了服务器（"));
    }
}
