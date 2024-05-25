package me.cyh2.bungee.solarwebsocket.utils.textutils;

import net.md_5.bungee.api.ProxyServer;

public class getServers {
    public static String Servers (ProxyServer server) {
        StringBuilder list_servers = new StringBuilder();
        server.getServers().keySet().forEach( str -> list_servers.append("<option value=\"").append(server.getServers().get(str).getName()).append("\">").append(server.getServers().get(str).getName()).append("服务器").append("</option>\n"));
        return list_servers.toString();
    }
}
