package hello.java.netty;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageUtils {
    //将BaseMessage消息写入ByteBuf
    public static ByteBuf getByteBuf(BaseMessage baseMessage)
            throws UnsupportedEncodingException {
        byte[] req = JSON.toJSONString(baseMessage).getBytes("UTF-8");
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(req);

        return byteBuf;
    }
    /*
     * 从ByteBuf中获取信息,使用UTF-8编码后解析为BaseMessage系统消息格式
     */
    public static BaseMessage getBaseMessage(ByteBuf buf) {
        byte[] con = new byte[buf.readableBytes()];
        buf.readBytes(con);
        try {
            String message = new String(con, "UTF8");
            BaseMessage baseMessage =   JSON.parseObject(message, BaseMessage.class);
            baseMessage.setReceiveTime(new Date());
            return  baseMessage;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }


}
