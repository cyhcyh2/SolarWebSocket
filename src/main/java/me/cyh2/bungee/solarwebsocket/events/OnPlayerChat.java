package me.cyh2.bungee.solarwebsocket.events;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.java_websocket.WebSocket;

import java.util.Map;

import static me.cyh2.bungee.solarwebsocket.websocket.MainWebSocket.WSMapS;

public class OnPlayerChat implements Listener {
    @EventHandler
    public void onPlayerChat (ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        //WSMap.keySet().forEach( str -> WSMap.get(str).send("[来自服务器] " + player.getDisplayName() + "：" + event.getMessage()));
        Map<String, WebSocket> WS = WSMapS.get(player.getServer().getInfo().getName());
        WS.keySet().forEach( str -> WS.get(str).send("[来自服务器] " + player.getDisplayName() + "：" + event.getMessage()));
    }
}
