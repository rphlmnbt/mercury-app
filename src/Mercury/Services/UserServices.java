package Mercury.Services;

import Mercury.Model.User;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class UserServices {

    public void addUser(User user) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.2/mercury?user=root&password=&serverTimezone=UTC");
        String sql = "INSERT INTO user (username, password, name) VALUES (?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getName());
        ps.execute();
        con.close();
        System.out.println("User Registered");
    }

    public void addUser(User user, String picturePath) throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.2/mercury?user=root&password=&serverTimezone=UTC");
        String sql = "INSERT INTO user (username, password, name, picture) VALUES (?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getName());

        File file = new File(picturePath);
        FileInputStream input = new FileInputStream(file);
//        BufferedImage bufferedImage = ImageIO.read(file);
//        Image image = SwingFXUtils.toFXImage(bufferedImage,null);
        ps.setBinaryStream(4, input);


        ps.execute();
        con.close();
        System.out.println("User Registered");
    }

    public ArrayList<User> listUsers() throws ClassNotFoundException, SQLException {
        ArrayList<User> userList = new ArrayList<>();

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.2/mercury?user=root&password=&serverTimezone=UTC");
        String sql = "SELECT * FROM user ORDER BY id ASC";
        PreparedStatement ps = con.prepareStatement(sql);

        ps.execute();

        ResultSet rs = ps.getResultSet();
        while(rs.next()) {
            User user = new User(rs);

            userList.add(user);
        }

        con.close();
        System.out.println("Master List Initialized 1");
        return  userList;
    }

    public User getUserById(int id) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.2/mercury?user=root&password=&serverTimezone=UTC");
        String sql = "SELECT * FROM user WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        rs.next();
        User user = new User(rs);
        con.close();
        System.out.println("User Searched");
        return user;
    }

    public void updateUser(User user) throws ClassNotFoundException, SQLException {
        UserServices us = new UserServices();
        User originalUser = us.getUserById(user.getId());
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.2/mercury?user=root&password=&serverTimezone=UTC");
        String sql = "UPDATE user SET name = ?, password = ?, username = ?, picture = ? WHERE id = ?";
        String name = user.getName();
        String password = user.getPassword();
        String username = user.getUsername();

        if(name != null) {
            originalUser.setName(user.getName());
        }

        if(password != null) {
            originalUser.setPassword(user.getPassword());
        }

        if(username != null) {
            originalUser.setUsername(user.getUsername());
        }

        PreparedStatement ps =con.prepareStatement(sql);
        ps.setString(1, originalUser.getName());
        ps.setString(2, originalUser.getPassword());
        ps.setString(3, originalUser.getUsername());
        ps.setInt(4, originalUser.getId());

        ps.execute();
        con.close();

        System.out.println("User Updated");
    }

    public void updateUser(User user, String picturePath) throws ClassNotFoundException, SQLException, FileNotFoundException {
        UserServices us = new UserServices();
        User originalUser = us.getUserById(user.getId());
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.2/mercury?user=root&password=&serverTimezone=UTC");
        String sql = "UPDATE user SET name = ?, password = ?, username = ?, picture = ? WHERE id = ?";
        String name = user.getName();
        String password = user.getPassword();
        String username = user.getUsername();

        if(name != null) {
            originalUser.setName(user.getName());
        }

        if(password != null) {
            originalUser.setPassword(user.getPassword());
        }

        if(username != null) {
            originalUser.setUsername(user.getUsername());
        }

        PreparedStatement ps =con.prepareStatement(sql);
        ps.setString(1, originalUser.getName());
        ps.setString(2, originalUser.getPassword());
        ps.setString(3, originalUser.getUsername());
        ps.setInt(5, originalUser.getId());

        if(picturePath != null) {
            File file = new File(picturePath);
            FileInputStream input = new FileInputStream(file);
            ps.setBinaryStream(4, input);
        }



        ps.execute();
        con.close();

        System.out.println("User Updated");
    }

    public void deleteUserById(int id) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.2/mercury?user=root&password=&serverTimezone=UTC");
        String sql = "DELETE FROM user WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);

        ps.execute();
        con.close();

        System.out.println("User Deleted");
    }

    public void deleteUserById(User user) throws SQLException, ClassNotFoundException {
        deleteUserById(user.getId());
    }
}
