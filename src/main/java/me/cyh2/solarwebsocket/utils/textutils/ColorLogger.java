package me.cyh2.solarwebsocket.utils.textutils;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import static me.cyh2.solarwebsocket.utils.textutils.ColorUtils.ReColor;

public class ColorLogger {
    String pluginName;
    ConsoleCommandSender commandSender = Bukkit.getConsoleSender();
    public ColorLogger (String PluginName) {
        this.pluginName = PluginName;
    }
    public ConsoleCommandSender getCommandSender () {
        return this.commandSender;
    }
    public void info (String msg) {
        this.commandSender.sendMessage(ReColor(pluginName + "&7[ &b·信息·&7 ]&r" + msg));
    }
    public void warning (String msg) {
        this.commandSender.sendMessage(ReColor(pluginName + "&7[ &e-警告-&7 ]&r" + msg));
    }
    public void error (String msg) {
        this.commandSender.sendMessage(ReColor(pluginName + "&7[ &b！错误！&7 ]&r" + msg));
    }
}
