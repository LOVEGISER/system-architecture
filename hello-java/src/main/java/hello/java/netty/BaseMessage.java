package hello.java.netty;

import java.util.Date;

public class BaseMessage {
    //消息创建时间
    private Date createTime;
    //消息接收时间
    private Date receiveTime;
    //消息内容
    private String messageContent;
    //消息id
    private int messageID;

    public BaseMessage( int messageID, String messageContent,Date createTime) {
        this.messageID = messageID;
        this.messageContent = messageContent;
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }



    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }
}
