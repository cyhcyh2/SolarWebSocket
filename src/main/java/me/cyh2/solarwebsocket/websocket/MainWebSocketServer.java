package me.cyh2.solarwebsocket.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static me.cyh2.solarwebsocket.SolarWebSocket.*;
import static me.cyh2.solarwebsocket.utils.textutils.ColorUtils.ReColor;


public class MainWebSocketServer extends WebSocketServer{

    public static Map<String, WebSocket> WSMap = new HashMap<>();
    public int Clients = 0;

    public MainWebSocketServer(String hostName, int port) {
        super(new InetSocketAddress(hostName, port));
    }

    @Override
    public void onClose(WebSocket ws, int arg1, String arg2, boolean arg3) {
        AtomicReference<String> remove = new AtomicReference<>("");
        Clients --;
        if(ws != null) {
            WSMap.keySet().forEach( str -> {
                if (WSMap.get(str).equals(ws)) {
                    remove.set(str);
                }
            });
            WSMap.remove(remove.get());
        }
        clogger.info("&b[Web Socket] &a编号为 " + remove.get() + " 的连接已断开");
    }
    @Override
    public void onError(WebSocket ws, Exception e) {
        logger.severe("WebSocket服务器发生异常！请检查您的配置文件中的配置端口是否已被占用！");
        AtomicReference<String> remove = new AtomicReference<>("");
        if(ws != null) {
            WSMap.keySet().forEach( str -> {
                if (WSMap.get(str).equals(ws)) {
                    remove.set(str);
                }
            });
            WSMap.remove(remove.get());
        }
        e.printStackTrace();
        logger.severe(Arrays.toString(e.getStackTrace()));
    }
    @Override
    public void onMessage(WebSocket ws, String msg) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(msg, JsonObject.class);
        if (jsonObject.get("msg").getAsString() != null) {
            clogger.info("&b[WEB SOCKET CHAT]&r " + jsonObject.get("user").getAsString() + "：" + jsonObject.get("msg").getAsString());
            WSMap.keySet().forEach( str -> WSMap.get(str).send(jsonObject.get("user").getAsString() + "：" + jsonObject.get("msg").getAsString()));
            Bukkit.getOnlinePlayers().forEach( p -> p.sendMessage(ReColor("[ Web Chat ] " + jsonObject.get("user").getAsString() + "：" + jsonObject.get("msg").getAsString())));
        }
    }
    @Override
    public void onOpen(WebSocket ws, ClientHandshake shake) {
        clogger.info("&b[Web Socket 连接事件]&a\n"+ws.isOpen()+"--"+ws.getReadyState()+"--"+ws.getAttachment());
        for(Iterator<String> it=shake.iterateHttpFields();it.hasNext();) {
            String key = it.next();
            clogger.info(key+":"+shake.getFieldValue(key));
        }
        ws.send(msgConfig.getString("WebSocketWelcomeMessage"));
        Clients ++;
        WSMap.put("WS" + Clients, ws);
    }
    @Override
    public void onStart() {
        clogger.info("&a&lSolar&rWebSocket&r服务器已启动！");
    }
}