package me.cyh2.bungee.solarwebsocket.httpserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import static me.cyh2.bungee.solarwebsocket.SolarWebSocketBungee.chat_html;
import static me.cyh2.bungee.solarwebsocket.SolarWebSocketBungee.index_html;


/**
 * NettyHttpServer By CYH2 - CYson Hab
 */
public class NettyHttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        try {
            ByteBuf content = request.content();
            byte[] bts = new byte[content.readableBytes()];
            content.readBytes(bts);
            String result;
            if(request.getMethod() == HttpMethod.GET) {
                String url = request.getUri();
                result = url.substring(url.indexOf("?")+1);
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                response.headers().set("content-Type","text/html;charset=ANSI");
                StringBuilder sb = new StringBuilder();
                if (result.startsWith("/chat/")) {
                    String user_name = result.substring(6);
                    sb.append(chat_html.toString().replace("{{ user }}", user_name));
                } else if (result.length() <= 1) {
                    sb.append(index_html);
                } else {
                    response.setStatus(HttpResponseStatus.NOT_FOUND);
                    sb.append("<h1>404！这个页面消失了！</h1>");
                }
                ByteBuf responseBuf = Unpooled.copiedBuffer(sb, CharsetUtil.UTF_8);
                response.content().writeBytes(responseBuf);
                responseBuf.release();
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            } else if(request.getMethod() == HttpMethod.POST) {
                result = "post method and parameters is "+ new String(bts);
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                response.headers().set("content-Type","text/html;charset=UTF-8");
                StringBuilder sb = new StringBuilder();
                sb.append("<html>")
                        .append("<head>")
                        .append("<title>netty http server</title>")
                        .append("</head>")
                        .append("<body>")
                        .append(result)
                        .append("</body>")
                        .append("</html>\r\n");
                ByteBuf responseBuf = Unpooled.copiedBuffer(sb, CharsetUtil.UTF_8);
                response.content().writeBytes(responseBuf);
                responseBuf.release();
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}