package hq.com.netty.im.server;

import hq.com.netty.im.handler.HttpRequestHandler;
import hq.com.netty.im.handler.TextWebSocketFrameHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * websocket netty4 服务端
 * <p>
 * 断线重连
 * http://www.cnblogs.com/fanguangdexiaoyuer/p/6132968.html
 * </p>
 * Created by kidy on 2017/5/12.
 */
public class Netty4IMServer {
    private static Logger log = LoggerFactory.getLogger(Netty4IMServer.class);
    /**
     * 群聊载体
     */
    private final ConcurrentHashMap<String, ChannelGroup> mapGroups = new ConcurrentHashMap<>();
    /**
     * 单聊载体
     */
    private final ConcurrentHashMap<String, Channel> mapSignles = new ConcurrentHashMap<>();
    private final ChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final static String WEBSOCKET_PATH_URI = "/ws";
    private Channel channel;

    public ChannelFuture start(InetSocketAddress address) {
        ServerBootstrap boot = new ServerBootstrap();
        boot.group(workerGroup).
                channel(NioServerSocketChannel.class).
                childHandler(createInitializer(mapGroups, mapSignles));
        ChannelFuture f = boot.bind(address).syncUninterruptibly();
        channel = f.channel();
        return f;
    }

    protected ChannelHandler createInitializer(ConcurrentHashMap<String, ChannelGroup> mapGroups, ConcurrentHashMap<String, Channel> mapSignles) {
        return new ChatServerInitializer(mapGroups, mapSignles);
    }

    public void destroy() {
        if (channel != null)
            channel.close();
        Set<Map.Entry<String, ChannelGroup>> set = mapGroups.entrySet();
        for (Map.Entry<String, ChannelGroup> entry : set) {
            entry.getValue().close();
        }
        workerGroup.shutdownGracefully();
    }

    public static void main(String[] args) {
        final Netty4IMServer server = new Netty4IMServer();
        ChannelFuture f = server.start(new InetSocketAddress(2048));
        System.out.println("server start................");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                server.destroy();
            }
        });
        f.channel().closeFuture().syncUninterruptibly();
    }

    public class ChatServerInitializer extends ChannelInitializer<Channel> {
        private final ConcurrentHashMap<String, ChannelGroup> mapGroups;
        private final ConcurrentHashMap<String, Channel> mapSignles;

        public ChatServerInitializer(ConcurrentHashMap<String, ChannelGroup> mapGroups, ConcurrentHashMap<String, Channel> mapSignles) {
            super();
            this.mapGroups = mapGroups;
            this.mapSignles = mapSignles;
        }

        protected void initChannel(Channel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(new HttpServerCodec());
            pipeline.addLast(new ChunkedWriteHandler());
            pipeline.addLast(new HttpObjectAggregator(64 * 1024));
            pipeline.addLast(new HttpRequestHandler(WEBSOCKET_PATH_URI));
            pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH_URI));
            pipeline.addLast(new TextWebSocketFrameHandler(mapGroups, mapSignles));
            /** 心跳检测处理器
             *
             * 它的作用就是用来检测客户端的读取超时的，
             * 该类的第一个参数是指定读操作空闲秒数，
             * 第二个参数是指定写操作的空闲秒数，
             * 第三个参数是指定读写空闲秒数，
             * 当有操作操作超出指定空闲秒数时，便会触发UserEventTriggered事件
             */
            pipeline.addLast(new IdleStateHandler(25, 15, 10, TimeUnit.SECONDS));
        }
    }
}
