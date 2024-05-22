package me.cyh2.bungee.solarwebsocket.events;

import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import static me.cyh2.bungee.solarwebsocket.websocket.MainWebSocket.WSMap;

public class OnPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin (PostLoginEvent event) {
        WSMap.keySet().forEach( str -> WSMap.get(str).send("玩家 " + event.getPlayer().getDisplayName() + " 加入了服务器！"));
    }
}
