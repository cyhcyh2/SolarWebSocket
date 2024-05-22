package me.cyh2.bungee.solarwebsocket.httpserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import me.cyh2.bungee.solarwebsocket.httpserver.handlers.NettyHttpHandler;

public class NettyHttpServer {
    private final String host;
    private final int port;
    private Channel channel;

    public NettyHttpServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
                            ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65535));//将多个消息转化成一个
                            ch.pipeline().addLast("http-encoder",new HttpResponseEncoder());
                            ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());//解决大码流的问题
                            ch.pipeline().addLast("http-server", new NettyHttpHandler()); // 自定义的Handler
                        }
                    });

            //ChannelFuture f = b.bind(port).sync();
            //f.channel().closeFuture().sync();
            ChannelFuture f = b.bind(host, port).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    channel = future.channel(); // 保存Channel
                    System.out.println("Server bound successfully");
                } else {
                    System.err.println("Failed to bind to port " + port);
                    future.cause().printStackTrace();
                }
            });
        } finally {
            //workerGroup.shutdownGracefully();
            //bossGroup.shutdownGracefully();
        }
    }
    public void stop() {
        if (channel != null && channel.isOpen()) {
            // 关闭Channel以停止接受新的连接
            channel.close();
            // 通常不需要显式关闭EventLoopGroup，因为当Channel关闭时，相关的EventLoopGroup也会自动关闭
            // 但如果你需要立即关闭所有资源，可以显式调用shutdownGracefully()
            //bossGroup.shutdownGracefully();
            //workerGroup.shutdownGracefully();
        }
    }
}