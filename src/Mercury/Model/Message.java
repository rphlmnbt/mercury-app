package Mercury.Model;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Message {
    private int msgId;
    private String msgSender;
    private String msgReceiver;
    private String msgBody;
    private String msgDate;
    private InputStream msgAttachment;

    public Message() {

    }

    public Message(ResultSet rs) throws SQLException {
        int msgId = rs.getInt("msg_id");
        String msgSender = rs.getString("msg_sender");
        String msgReceiver = rs.getString("msg_receiver");
        String msgBody = rs.getString("msg_body");
        String msgDate = rs.getString("msg_date");
        InputStream msgAttachment = rs.getBinaryStream("msg_attachment");

        this.msgId = msgId;
        this.msgSender = msgSender;
        this.msgReceiver = msgReceiver;
        this.msgBody = msgBody;
        this.msgDate = msgDate;
        this.msgAttachment = msgAttachment;
    }

    public Message(int msgId, String msgSender, String msgReceiver, String msgBody, String msgDate) throws SQLException {
        this.msgId = msgId;
        this.msgSender = msgSender;
        this.msgReceiver = msgReceiver;
        this.msgBody = msgBody;
        this.msgDate = msgDate;
    }

    public Message(String msgSender, String msgReceiver, String msgBody, String msgDate) throws SQLException {
        this.msgSender = msgSender;
        this.msgReceiver = msgReceiver;
        this.msgBody = msgBody;
        this.msgDate = msgDate;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getMsgSender() {
        return msgSender;
    }

    public void setMsgSender(String msgSender) {
        this.msgSender = msgSender;
    }

    public String getMsgReceiver() {
        return msgReceiver;
    }

    public void setMsgReceiver(String msgReceiver) {
        this.msgReceiver = msgReceiver;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public InputStream getMsgAttachment() {
        return msgAttachment;
    }

    public void setMsgAttachment(InputStream msgAttachment) {
        this.msgAttachment = msgAttachment;
    }
}
