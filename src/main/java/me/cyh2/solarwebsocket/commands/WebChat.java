package me.cyh2.solarwebsocket.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static me.cyh2.solarwebsocket.SolarWebSocket.server;
import static me.cyh2.solarwebsocket.websocket.MainWebSocketServer.WSMap;

public class WebChat implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length >= 1) {
            WSMap.keySet().forEach( str -> WSMap.get(str).send(commandSender.getName() + "：" + strings[0]));
            server.broadcastMessage(commandSender.getName() + "：" + strings[0]);
        }
        return true;
    }
}
