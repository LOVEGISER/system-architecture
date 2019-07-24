package hello.java.netty.server;

import com.alibaba.fastjson.JSON;
import hello.java.netty.BaseMessage;
import hello.java.netty.MessageUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class MessageDecoder extends ChannelHandlerAdapter {
    private final static Log logger = LogFactory.getLog(MessageDecoder.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //接收客户端上报的消息并解码
        ByteBuf buf = (ByteBuf) msg;
        BaseMessage message = MessageUtils.getBaseMessage(buf);
        logger.info("received message form client:"+JSON.toJSONString(message));
        //定义回复消息并下发
        try {
            BaseMessage responseMessage = new BaseMessage(message.getMessageID()+1,
                    "response from server",new Date());
            logger.info("send response message for client:"+JSON.toJSONString(responseMessage));
            ByteBuf byteBuf  = MessageUtils.getByteBuf(responseMessage);
            ctx.writeAndFlush(byteBuf);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        logger.error("channel removed");
        super.handlerRemoved(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("channel exception");
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.error("channel registered");
        super.channelRegistered(ctx);
    }
}
