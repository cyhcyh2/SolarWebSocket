package me.cyh2.bungee.solarwebsocket.events;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import static me.cyh2.bungee.solarwebsocket.websocket.MainWebSocket.WSMap;

public class OnPlayerQuit implements Listener {
    @EventHandler
    public void onPlayerQuit (PlayerDisconnectEvent event) {
        WSMap.keySet().forEach( str -> WSMap.get(str).send("玩家 " + event.getPlayer().getDisplayName() + " 离开了服务器（"));
    }
}
