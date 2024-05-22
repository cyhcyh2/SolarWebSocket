package me.cyh2.solarwebsocket.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static me.cyh2.solarwebsocket.websocket.MainWebSocketServer.WSMap;

public class OnPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e) {
        WSMap.keySet().forEach( str -> WSMap.get(str).send("玩家" + e.getPlayer().getName() + "进入了服务器！"));
    }
}
