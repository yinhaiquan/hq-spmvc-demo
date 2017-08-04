package hq.com.netty.im.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * http请求处理
 *
 * @author yinhaiquan
 * @describle： <p>
 * <b>note:</b>
 * 基于netty4 对http请求处理器,包括对请求地址合法性，请求头验证以及其他业务处理
 * </p>
 * @date 2017/5/12
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static Logger log = LoggerFactory.getLogger(HttpRequestHandler.class);
    private static final String HTTP_REQUEST_STRING = "id";
    private final String wsURI;

    public HttpRequestHandler(String wsURI) {
        super();
        this.wsURI = wsURI;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        /**非法请求*/
        if (wsURI.equalsIgnoreCase(req.uri())) {
            /**Handle a bad request*/
            if (!req.decoderResult().isSuccess()) {
                log.info("错误请求!");
                sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
                return;
            }
            /**Allow only GET methods.(只允许get请求，若允许其他请求可不添加此处限制)*/
            if (req.method() != HttpMethod.GET) {
                log.info("只允许GET请求方式!");
                sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.NOT_FOUND));
                return;
            }
            if (HttpUtil.is100ContinueExpected(req)) {
                FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.CONTINUE);
                ctx.writeAndFlush(response);
            }
            ctx.fireChannelRead(req.retain());
        } else {
            log.info("非法请求");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace(System.err);
    }

    /**
     * 响应http客户端
     *
     * @param ctx
     * @param req
     * @param resp
     */
    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req,
                                  FullHttpResponse resp) {
        if (resp.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(resp.status().toString(), CharsetUtil.UTF_8);
            resp.content().writeBytes(buf);
            buf.release();
            HttpUtil.setContentLength(resp, resp.content().readableBytes());
        }
        ChannelFuture f = ctx.writeAndFlush(resp);
        if (!HttpUtil.isKeepAlive(req) || resp.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
