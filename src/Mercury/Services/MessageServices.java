package Mercury.Services;

import Mercury.Model.Message;
import Mercury.Model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;

public class MessageServices {

    public void sendMessage(Message message) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.2/mercury?user=root&password=&serverTimezone=UTC");
        String sql = "INSERT INTO message (msg_sender, msg_receiver, msg_body, msg_date) VALUES (?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, message.getMsgSender());
        ps.setString(2, message.getMsgReceiver());
        ps.setString(3, message.getMsgBody());
        ps.setString(4, message.getMsgDate());
        ps.execute();
        con.close();
        System.out.println("Message Sent");
    }

    public void sendMessage(Message message, String attachmentPath) throws ClassNotFoundException, SQLException, FileNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.2/mercury?user=root&password=&serverTimezone=UTC");
        String sql = "INSERT INTO message (msg_sender, msg_receiver, msg_body, msg_date, msg_attachment) VALUES (?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, message.getMsgSender());
        ps.setString(2, message.getMsgReceiver());
        ps.setString(3, message.getMsgBody());
        ps.setString(4, message.getMsgDate());

        File file = new File(attachmentPath);
        FileInputStream input = new FileInputStream(file);
        ps.setBinaryStream(5, input);

        ps.execute();
        con.close();
        System.out.println("Message Sent");
    }

    public ArrayList<Message> showMessages() throws ClassNotFoundException, SQLException {
        ArrayList<Message> msgList = new ArrayList<>();

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.2/mercury?user=root&password=&serverTimezone=UTC");
        String sql = "SELECT msg_id, msg_sender, msg_receiver, msg_body, msg_date FROM message ORDER BY msg_date ASC";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        while(rs.next()) {
            Message message = new Message(rs);

            msgList.add(message);
        }

        con.close();
        System.out.println("Sent Items Initialized");
        return  msgList;
    }

    public ArrayList<Message> getUserMessages(String msg_receiver) throws ClassNotFoundException, SQLException {
        ArrayList<Message> msgList = new ArrayList<>();

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.2/mercury?user=root&password=&serverTimezone=UTC");
        String sql = "SELECT * FROM message WHERE msg_receiver = ? ORDER BY msg_date ASC";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, msg_receiver);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        while(rs.next()) {
            Message message = new Message(rs);

            msgList.add(message);
        }

        con.close();
        System.out.println("Sent Items Initialized");
        return  msgList;
    }

    public ArrayList<Message> getUserSent(String msg_sender) throws ClassNotFoundException, SQLException {
        ArrayList<Message> msgList = new ArrayList<>();

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.2/mercury?user=root&password=&serverTimezone=UTC");
        String sql = "SELECT * FROM message WHERE msg_sender = ? ORDER BY msg_date ASC";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, msg_sender);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        while(rs.next()) {
            Message message = new Message(rs);

            msgList.add(message);
        }

        con.close();
        System.out.println("Received Items Initialized");
        return  msgList;
    }

    public void deleteMsgById(int msgId) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.2/mercury?user=root&password=&serverTimezone=UTC");
        String sql = "DELETE FROM message WHERE msg_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, msgId);

        ps.execute();
        con.close();

        System.out.println("Message Deleted");
    }

    public void deleteMsgById(Message message) throws SQLException, ClassNotFoundException {
        deleteMsgById(message.getMsgId());
    }
}
