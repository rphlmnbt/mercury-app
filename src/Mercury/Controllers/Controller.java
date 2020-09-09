package Mercury.Controllers;

import Mercury.Model.User;
import Mercury.Services.UserServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.*;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    private Pane regPane;
    @FXML
    private Pane loginPane;
    @FXML
    private TextField userReg;
    @FXML
    private TextField nameReg;
    @FXML
    private PasswordField passReg;
    @FXML
    private TextField userField;
    @FXML
    private PasswordField passField;
    @FXML
    private Pane mainMenu;
    @FXML
    private Pane updateScreen;
    @FXML
    private TextField updateUserField;
    @FXML
    private PasswordField updatePassField;
    @FXML
    private TextField updateNameField;
    @FXML
    private TextField userCredentials;
    @FXML
    private PasswordField passCredentials;
    @FXML
    private TitledPane deleteScreen;
    @FXML
    private TextField deleteUserCred;
    @FXML
    private PasswordField deletePassCred;
    @FXML
    private TextField searchID;
    @FXML
    private Label ID;
    @FXML
    private Label username;
    @FXML
    private Label name;
    @FXML
    private Pane searchScreen;
    @FXML
    private Label currentId;
    @FXML
    private Label currentUser;
    @FXML
    private Pane listScreen;
    @FXML
    private ListView<User> listView;
    @FXML
    private ImageView userPic;
    @FXML
    private ImageView userPicUpdate;
    @FXML
    private ImageView userPicSearch;
    private ObservableList<User> userObservableList;
    private String filePath;
    private File  defaultPicFile = new File("./src/Mercury/MercuryResources/UserIcon.png");
    private Image defaultPic = new Image(defaultPicFile.toURI().toString(),150,150,false,true,true);

    public Controller() {
        try {
            UserServices us = new UserServices();
            userObservableList = FXCollections.observableArrayList();
            for (User user : us.listUsers()){
                userObservableList.add(user);
            }
        } catch (Exception e) {
            System.out.println("E Controller");
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setItems(userObservableList);
        listView.setCellFactory(userListView -> new UserListViewCell(currentUser.getText()));
    }

    public void refresh() {
        try {
            UserServices us = new UserServices();
            userObservableList = FXCollections.observableArrayList();
            for (User user : us.listUsers()){
                userObservableList.add(user);
            }
            listView.setItems(userObservableList);
            listView.setCellFactory(userListView -> new UserListViewCell(currentUser.getText()));
        } catch (Exception e) {
            System.out.println("E refresh");
            e.printStackTrace();
        }

    }

    public void openInbox() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Mercury_Inbox.fxml"));
            Parent root = loader.load();

            InboxController inboxController = loader.getController();
            inboxController.setReceiver(currentUser.getText());

            Stage stage = new Stage();
            stage.getIcons().add(new Image("Mercury/MercuryResources/logo.png"));
            stage.setScene(new Scene(root));
            stage.setTitle("Inbox");
            stage.show();
        } catch (IOException e) {
            System.out.println("IOE openInbox");
            e.printStackTrace();
        }
    }

    public void openOutbox() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Mercury_Outbox.fxml"));
            Parent root = loader.load();

            OutboxController outboxController = loader.getController();
            outboxController.setSender(currentUser.getText());

            Stage stage = new Stage();
            stage.getIcons().add(new Image("Mercury/MercuryResources/logo.png"));
            stage.setScene(new Scene(root));
            stage.setTitle("Outbox");
            stage.show();
        } catch (IOException e) {
            System.out.println("IOE openInbox");
            e.printStackTrace();
        }
    }

    public void attachFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionPng = new FileChooser.ExtensionFilter("PNG FILES (*.png", ".png");
        File file = fileChooser.showOpenDialog(null);
        this.filePath = file.getAbsolutePath();
        Image image = new Image(file.toURI().toString(), 150, 150, false, true,true);
        userPic.setImage(image);
        userPicUpdate.setImage(image);
        userPicSearch.setImage(image);
    }

    public void showList() {
        mainMenu.setVisible(false);
        listScreen.setVisible(true);
    }

    public void showSendScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Mercury_Send.fxml"));
            Parent root = loader.load();

            SendMessageController sendMessageController = loader.getController();
            sendMessageController.setMsgSender(currentUser.getText());

            Stage stage = new Stage();
            stage.getIcons().add(new Image("Mercury/MercuryResources/logo.png"));
            stage.setScene(new Scene(root));
            stage.setTitle("Send Message");
            stage.show();
        } catch (IOException e) {
            System.out.println("IOE sendMessage");
            e.printStackTrace();
        }
    }

    public void showRegScreen() {
        regPane.setVisible(true);
        loginPane.setVisible(false);
    }

    public void showDeleteScreen() {
        deleteScreen.setVisible(true);
    }

    public void showSearch() {
        mainMenu.setVisible(false);
        searchScreen.setVisible(true);
    }

    public void showUpdateScreen() {
        try {
            UserServices us = new UserServices();
            Image image = new Image(us.getUserById(Integer.parseInt(currentId.getText())).getPicture(),150,150,false,true);
            userPicUpdate.setImage(image);
            updateScreen.setVisible(true);
            mainMenu.setVisible(false);
        } catch (Exception e) {
            System.out.println("E showUpdateScreen");
            e.printStackTrace();
        }

    }

    public String encrypt(String password) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public void regUser() {
        UserServices us = new UserServices();
        String username = userReg.getText();
        String password = encrypt(passReg.getText());
        String name = nameReg.getText();
        User user = new User(username, password, name);

        try {
            if (this.filePath == null) {
                us.addUser(user);
            } else {
                us.addUser(user, filePath);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("CNFE regUser");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SE regUser");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("FNFE regUser");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOE regUser");
            e.printStackTrace();
        }

        userReg.clear();
        passReg.clear();
        nameReg.clear();

        userPic.setImage(defaultPic);
        userPicUpdate.setImage(defaultPic);
        userPicSearch.setImage(defaultPic);
        filePath = null;

        regPane.setVisible(false);
        loginPane.setVisible(true);
    }

    public void loginUser() {
        boolean isLoggedIn = false;
        UserServices us = new UserServices();
        ArrayList<User> userList = null;
        try {
            userList = us.listUsers();
        } catch (ClassNotFoundException e) {
            System.out.println("CNFE loginUser");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SE loginUser");
            e.printStackTrace();
        }
        for (User user : userList) {
            if (userField.getText().equals(user.getUsername()) && encrypt(passField.getText()).equals(user.getPassword())) {
                userField.clear();
                passField.clear();
                currentId.setText(Integer.toString(user.getId()));
                currentUser.setText(user.getUsername());
                loginPane.setVisible(false);
                mainMenu.setVisible(true);
                isLoggedIn = true;
            }
        }
        if (isLoggedIn != true) {
            JOptionPane.showMessageDialog(null, "Invalid Credentials", "ALERT", 2);
        }
    }

    public void updateUser() {
        boolean isVerified = false;
        UserServices us = new UserServices();
        ArrayList<User> userList = null;
        try {
            userList = us.listUsers();
        } catch (ClassNotFoundException e) {
            System.out.println("CNFE updateUser");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SE updateUser");
            e.printStackTrace();
        }

        for (User user : userList) {
            if (userCredentials.getText().equals(user.getUsername()) && encrypt(passCredentials.getText()).equals(user.getPassword())) {
                isVerified = true;
                User updatedUser = new User();


                if (updateUserField.getText().isEmpty() != true) {
                    updatedUser.setUsername(updateUserField.getText());
                }
                if (updatePassField.getText().isEmpty() != true) {
                    updatedUser.setPassword(updatePassField.getText());
                }
                if (updateNameField.getText().isEmpty() != true) {
                    updatedUser.setName(updateNameField.getText());
                }
                try {
                    if (filePath == null) {
                        updatedUser.setId(user.getId());
                        us.updateUser(updatedUser);
                    } else {
                        updatedUser.setId(user.getId());
                        us.updateUser(updatedUser, filePath);
                    }
                } catch (Exception e) {
                    System.out.println("E updateUser");
                    e.printStackTrace();
                }



                userCredentials.clear();
                passCredentials.clear();
                updateUserField.clear();
                updatePassField.clear();
                updateNameField.clear();
                userPicUpdate.setImage(defaultPic);
                userPic.setImage(defaultPic);
                userPicSearch.setImage(defaultPic);

                updateScreen.setVisible(false);
                mainMenu.setVisible(true);
            }
        }
        if (isVerified != true) {
            JOptionPane.showMessageDialog(null, "Invalid Credentials", "ALERT", 2);
        }
    }

    public void deleteUser() {
        boolean isVerified = false;
        UserServices us = new UserServices();
        ArrayList<User> userList = null;

        try {
            userList = us.listUsers();
        } catch (ClassNotFoundException e) {
            System.out.println("CNFE deleteUser");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SE deleteUser");
            e.printStackTrace();
        }

        for (User user : userList) {
            if (deleteUserCred.getText().equals(user.getUsername()) && encrypt(deletePassCred.getText()).equals(user.getPassword())) {
                try {
                    us.deleteUserById(user);
                } catch (ClassNotFoundException e) {
                    System.out.println("CNFE deleteUser");
                    e.printStackTrace();
                } catch (SQLException e) {
                    System.out.println("SE deleteUser");
                    e.printStackTrace();
                }
                isVerified = true;
                deleteUserCred.clear();
                deletePassCred.clear();

                deleteScreen.setVisible(false);
                updateScreen.setVisible(false);
                loginPane.setVisible(true);
            }
        }

        if (isVerified != true) {
            JOptionPane.showMessageDialog(null, "Invalid Credentials", "ALERT", 2);
        }



    }

    public void logOut() {
        currentUser.setText("");
        currentId.setText("");
        mainMenu.setVisible(false);
        loginPane.setVisible(true);
    }

    public void searchUser() {
        User user = new User();
        UserServices us = new UserServices();
        try {
            user = us.getUserById(Integer.parseInt(searchID.getText()));
            ID.setText(Integer.toString(user.getId()));
            username.setText(user.getUsername());
            name.setText(user.getName());
            Image image = new Image(us.getUserById(Integer.parseInt(searchID.getText())).getPicture(), 150, 150, false, true);
            userPicSearch.setImage(image);
            searchID.clear();
        } catch (Exception e) {
            System.out.println("E searchUser");
            e.printStackTrace();
        }

    }

    public void backToMainList() {
        mainMenu.setVisible(true);
        listScreen.setVisible(false);
    }

    public void backToMainSearch() {
        searchScreen.setVisible(false);
        mainMenu.setVisible(true);
        ID.setText("ID");
        username.setText("Username");
        name.setText("Name");
        userPicUpdate.setImage(defaultPic);
        userPic.setImage(defaultPic);
        userPicSearch.setImage(defaultPic);
    }

    public void backToLogin() {
        regPane.setVisible(false);
        mainMenu.setVisible(true);
        userReg.clear();
        passReg.clear();
        nameReg.clear();
    }

    public void backToMainUpdate() {
        updateScreen.setVisible(false);
        mainMenu.setVisible(true);
        updateUserField.clear();
        updatePassField.clear();
        updateNameField.clear();
        userCredentials.clear();
        passCredentials.clear();
    }

    public void backToMainDelete() {
        deleteScreen.setVisible(false);
    }

    public void keyPressedLogin(KeyEvent enter) {
        if (enter.getCode()== KeyCode.ENTER) {
            loginUser();
        }
    }

    public void keyPressedReg(KeyEvent enter) {
        if (enter.getCode()== KeyCode.ENTER) {
            regUser();
        }
    }

    public void keyPressedDel(KeyEvent enter) {
        if (enter.getCode()== KeyCode.ENTER) {
            deleteUser();
        }
    }

    public void keyPressedSearch(KeyEvent enter) {
        if (enter.getCode()== KeyCode.ENTER) {
            searchUser();
        }
    }

    public void keyPressedUpdate(KeyEvent enter) {
        if (enter.getCode()== KeyCode.ENTER) {
            updateUser();
        }
    }

}
