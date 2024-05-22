package me.cyh2.bungee.solarwebsocket.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.TextComponent;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static me.cyh2.bungee.solarwebsocket.SolarWebSocketBungee.*;
import static me.cyh2.bungee.solarwebsocket.utils.textutils.ColorUtils.ReColor;


public class MainWebSocket extends WebSocketServer{
    @Deprecated
    public static Map<String, WebSocket> WSMap = new HashMap<>();
    public static Map<String, Map<String, WebSocket>> WSMapS = new HashMap<>();
    public int Clients = 0;

    public MainWebSocket(String hostName, int port) {
        super(new InetSocketAddress(hostName, port));
        proxyServer.getServers().keySet().forEach( str -> WSMapS.put(proxyServer.getServers().get(str).getName(), new HashMap<>()));
    }
    @Override
    public void onClose(WebSocket ws, int arg1, String arg2, boolean arg3) {
        AtomicReference<String> remove = new AtomicReference<>("");
        AtomicReference<String> rserver = new AtomicReference<>("");
        if(ws != null) {
            Clients --;
            WSMapS.keySet().forEach( str -> WSMapS.get(str).keySet().forEach(str1 -> {
                if (WSMapS.get(str).get(str1).equals(ws)) {
                    remove.set(str1);
                    rserver.set(str);
                }
            }));
            if (!rserver.get().equals("") && !remove.get().equals("")) {
                WSMapS.get(rserver.get()).remove(remove.get());
                logger.info(ReColor("&b[Web Socket] &a编号为 " + remove.get() + " 的连接已断开，其连接的子服务器为 " + rserver.get()));
            } else {
                logger.info(ReColor("&b[Web Socket] &a一个未登录的非法客户端连接已断开！"));
            }
        }
    }
    @Override
    public void onError(WebSocket ws, Exception e) {
        logger.severe("WebSocket服务器发生异常！请检查您的配置文件中的配置端口是否已被占用！");
        AtomicReference<String> remove = new AtomicReference<>("");
        AtomicReference<String> rserver = new AtomicReference<>("");
        if(ws != null) {
            Clients --;
            WSMapS.keySet().forEach( str -> WSMapS.get(str).keySet().forEach(str1 -> {
                if (WSMapS.get(str).get(str1).equals(ws)) {
                    remove.set(str1);
                    rserver.set(str);
                }
            }));
            if (!rserver.get().equals("") && !remove.get().equals("")) {
                WSMapS.get(rserver.get()).remove(remove.get());
                logger.info(ReColor("&b[Web Socket] &a编号为 " + remove.get() + " 的连接已断开，其连接的子服务器为 " + rserver.get()));
            } else {
                logger.info(ReColor("&b[Web Socket] &a一个未登录的非法客户端连接已断开！"));
            }
        }
        e.printStackTrace();
        logger.severe(Arrays.toString(e.getStackTrace()));
    }
    @Override
    public void onMessage(WebSocket ws, String msg) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(msg, JsonObject.class);
        if (jsonObject.get("msg") != null) {
            WSMapS.keySet().forEach( str -> WSMapS.get(str).keySet().forEach( str1 ->{
                if (WSMapS.get(str).get(str1).equals(ws)) {
                    logger.info(ReColor("&b[WEB SOCKET CHAT]&r " + jsonObject.get("user").getAsString() + "：" + jsonObject.get("msg").getAsString()));
                    WSMapS.get(str).keySet().forEach( str2 -> WSMapS.get(str).get(str2).send(jsonObject.get("user").getAsString() + "：" + jsonObject.get("msg").getAsString()));
                    proxyServer.getServerInfo(str).getPlayers().forEach(p -> p.sendMessage(new TextComponent(ReColor("[ Web Chat ] " + jsonObject.get("user").getAsString() + "：" + jsonObject.get("msg").getAsString()))));
                }
            }));
            //WSMap.keySet().forEach(str -> WSMap.get(str).send(jsonObject.get("user").getAsString() + "：" + jsonObject.get("msg").getAsString()));
        } else if (jsonObject.get("login_user") != null && jsonObject.get("is_anonymous") != null && jsonObject.get("server") != null) {
            boolean anonymous = jsonObject.get("is_anonymous").getAsBoolean();
            String user = jsonObject.get("login_user").getAsString();
            String server = jsonObject.get("server").getAsString();
            if (WSMapS.get(server) != null) {
                AtomicReference<String> remove = new AtomicReference<>("");
                AtomicReference<String> rserver = new AtomicReference<>("");
                WSMapS.keySet().forEach( str -> WSMapS.get(str).keySet().forEach( str1 -> {
                    if (WSMapS.get(str).get(str1).equals(ws)) {
                        rserver.set(str);
                        remove.set(str1);
                    }
                }));
                if (!remove.get().equals("") && !rserver.get().equals("")) {
                    WSMapS.get(rserver.get()).remove(remove.get());
                    WSMapS.get(rserver.get()).keySet().forEach( str -> WSMapS.get(rserver.get()).get(str).send("用户" + remove.get() + "离开了这个聊天通道！"));
                    logger.info(ReColor("&a用户&r" + remove.get() + "&a离开了这个聊天通道！"));
                }
                WSMapS.get(server).put(user, ws);
            }
            String s = "{\"login_user\": \"{{ user }}\", \"is_anonymous\": \"false\", \"server\": \"" + server + "\"}";
        }
    }
    @Override
    public void onOpen(WebSocket ws, ClientHandshake shake) {
        logger.info(ReColor("&b[Web Socket 连接事件]&a\n"+ws.isOpen()+"--"+ws.getReadyState()+"--"+ws.getAttachment()));
        for(Iterator<String> it=shake.iterateHttpFields();it.hasNext();) {
            String key = it.next();
            logger.info(ReColor(key+":"+shake.getFieldValue(key)));
        }
        ws.send(msgConfig.getString("WebSocketWelcomeMessage"));
        Clients ++;
        //WSMap.put("WS" + Clients, ws);
    }
    @Override
    public void onStart() {
        logger.info(ReColor("&a&lSolar&rWebSocket&r服务器已启动！"));
    }
}