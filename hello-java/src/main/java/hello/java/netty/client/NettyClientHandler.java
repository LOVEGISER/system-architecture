package hello.java.netty.client;

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

public class NettyClientHandler extends ChannelHandlerAdapter {

    private final static Log logger = LogFactory.getLog(NettyClientHandler.class);
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       //创建一条消息，发送给服务端
        BaseMessage message = new BaseMessage(0,
                "message from client",new Date());
        ByteBuf byteBuf =  MessageUtils.getByteBuf(message);
        ctx.writeAndFlush(byteBuf);
        logger.info("send a message for server:"+ JSON.toJSONString(message));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        BaseMessage message = MessageUtils.getBaseMessage(buf);
        logger.info("received message form server:"+JSON.toJSONString(message));

    }


}