package Mercury.Controllers;

import Mercury.Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;

public class UserListViewCell extends ListCell<User> {

    @FXML
    private Label username;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private GridPane gridPane;
    private String currentUser;
    private FXMLLoader mLLoader;

    public UserListViewCell(String currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);

        if(empty || user == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("../Mercury_UserList.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            id.setText(String.valueOf(user.getId()));
            username.setText(user.getUsername());
            name.setText(user.getName());

            setText(null);
            setGraphic(gridPane);
        }

    }

    public void showSendScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Mercury_Send.fxml"));
            Parent root = loader.load();

            SendMessageController sendMessageController = loader.getController();
            sendMessageController.setMsgSender(currentUser);
            sendMessageController.setMsgReceiver(username.getText());

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

}
