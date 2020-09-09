package Mercury.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewMessageController {
    @FXML
    private Label msg_sender;
    @FXML
    private Label msg_receiver;
    @FXML
    private Label msg_date;
    @FXML
    private Text msg_body;
    @FXML
    private Button reply;

    private String type;

    public void setMsg(String sender, String receiver, String date, String body,String type) {
        msg_body.setText(body);
        msg_date.setText(date);
        msg_receiver.setText(receiver);
        msg_sender.setText(sender);
        this.type = type;
    }

    public void showSendScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Mercury_Send.fxml"));
            Parent root = loader.load();

            SendMessageController sendMessageController = loader.getController();
            if (type.equals("inbox")) {
                sendMessageController.setMsgSender(msg_receiver.getText());
                sendMessageController.setMsgReceiver(msg_sender.getText());
            } else {
                sendMessageController.setMsgSender(msg_sender.getText());
                sendMessageController.setMsgReceiver(msg_receiver.getText());
            }

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
