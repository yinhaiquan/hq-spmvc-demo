package hq.com.netty.im.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 报文请求处理
 *
 * @author yinhaiquan
 * @describle: <p>
 * <b>note:</b>
 * Text消息的处理类
 * <p>
 * 报文内容格式：
 * {
 * "from": "",  //报文发送者
 * "to": "",    //报文接收者
 * "type": 0,   //报文接收者类型 0个人 1群组
 * "msg": ""    //报文内容
 * }
 * </p>
 * @date 2017/5/12
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static Logger log = LoggerFactory.getLogger(TextWebSocketFrameHandler.class);
    /**
     * 通道组
     */
    private final ChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
    /**
     * 群聊通道组载体
     */
    private final ConcurrentHashMap<String, ChannelGroup> mapGroups;
    /**
     * 单聊组载体
     */
    private final ConcurrentHashMap<String, Channel> mapSignles;

    public TextWebSocketFrameHandler(ConcurrentHashMap<String, ChannelGroup> mapGroups, ConcurrentHashMap<String, Channel> mapSignles) {
        super();
        this.mapGroups = mapGroups;
        this.mapSignles = mapSignles;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        super.userEventTriggered(ctx, evt);
        /**第一次握手成功添加用户通道*/
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            ctx.pipeline().remove(HttpRequestHandler.class);
        }
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()) {
                case READER_IDLE:
                    log.info("读空闲");
                    ctx.close();
                    break;
                case WRITER_IDLE:
                    log.info("写空闲");
                    break;
                case ALL_IDLE:
                    log.info("读写空闲");
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                TextWebSocketFrame msg) throws Exception {
        String respContext = msg.text();
        JSONObject json = JSONObject.parseObject(respContext);
        System.out.println(json.get("from"));
        System.out.println(json.get("to"));
        System.out.println(json.get("type"));
        System.out.println(json.get("msg"));
        System.out.println(respContext);
        System.out.println(ctx.channel().id().asShortText());
        group.writeAndFlush(msg.retain());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
        cause.printStackTrace();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        log.info("Client:" + incoming.remoteAddress() + "在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        log.info("Client:" + incoming.remoteAddress() + "掉线");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel inChannel = ctx.channel();
        for (Channel c : group) {
            c.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + inChannel.remoteAddress() + " 加入"));
        }
        group.add(ctx.channel());
        log.info("Client:" + inChannel.remoteAddress() + "加入");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : group) {
            channel.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 离开"));
        }
        log.info("Client:" + incoming.remoteAddress() + "离开");
        group.remove(ctx.channel());
    }
}
