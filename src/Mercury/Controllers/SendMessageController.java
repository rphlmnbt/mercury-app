package Mercury.Controllers;

import Mercury.Model.Message;
import Mercury.Services.MessageServices;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SendMessageController {

    @FXML
    private TextField msgReceiver;
    @FXML
    private Label msgSender;
    @FXML
    private TextArea msgBody;
    @FXML
    private Button closeSendScreen;
    private String filePath;

    public void setMsgSender(String currentUser) {
        msgSender.setText(currentUser);
    }

    public void setMsgReceiver(String receiver) {
        msgReceiver.setText(receiver);
    }

    public String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        String date = String.valueOf(localDate);
        return date;
    }

    public void sendMessage() {
        try {
            MessageServices ms = new MessageServices();

            String receiver = msgReceiver.getText();
            String sender = msgSender.getText();
            String body = msgBody.getText();
            String date = this.getDate();
            Message message = new Message(sender, receiver, body, date);

            if (this.filePath == null) {
                ms.sendMessage(message);
            } else {
                body = msgBody.getText() + "   (Includes file from: " + filePath + ")";
                message = new Message(sender, receiver, body, date);
                ms.sendMessage(message, filePath);
            }
        } catch (Exception e) {
            System.out.println("E sendMessage");
            e.printStackTrace();
        }

    }

    public void attachFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        this.filePath = file.getAbsolutePath();
    }

    public void exit(){
        Stage stage = (Stage) closeSendScreen.getScene().getWindow();
        stage.close();
    }

}
