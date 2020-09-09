package Mercury.Controllers;

import Mercury.Model.Message;
import Mercury.Services.MessageServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.sql.SQLException;

public class MessageListViewCell extends ListCell<Message> {
    @FXML
    private Label sender;
    @FXML
    private Label receiver;
    @FXML
    private Label id;
    @FXML
    private Label body;
    @FXML
    private Label date;
    @FXML
    private GridPane inbox;
    private String type;
    private FXMLLoader mLLoader;
    private InputStream input;

    public MessageListViewCell(String type) {
        this.type = type;
    }

    @Override
    protected void updateItem(Message message, boolean empty) {
        super.updateItem(message, empty);

        if(empty || message == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("../Mercury_MessageListCell.fxml"));
                if (type.equals("outbox")) {
                    mLLoader = new FXMLLoader(getClass().getResource("../Mercury_MessageListCellSent.fxml"));
                }
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            id.setText(String.valueOf(message.getMsgId()));
            sender.setText(message.getMsgSender());
            receiver.setText(message.getMsgReceiver());
            body.setText(message.getMsgBody());
            date.setText(message.getMsgDate());
            input = message.getMsgAttachment();


            setText(null);
            setGraphic(inbox);
        }

    }

    public void deleteMessage() {
        MessageServices ms = new MessageServices();
        try {
            ms.deleteMsgById(Integer.parseInt(id.getText()));
        } catch (SQLException e) {
            System.out.println("SQLE deleteMessage");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("CNFE deleteMessage");
            e.printStackTrace();
        }
    }

    public void viewMessage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Mercury_View.fxml"));
            Parent root = loader.load();

            ViewMessageController viewMessageController = loader.getController();
            viewMessageController.setMsg(sender.getText(), receiver.getText(), date.getText(), body.getText(), type);

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

    public void getAttachment() {
        try {
            if (input != null) {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showSaveDialog(null);
                FileOutputStream output = new FileOutputStream(file);
                byte[] buffer = new byte[32 * 1024];
                while (input.read(buffer) > 0) {
                    output.write(buffer);
                }
            } else {
                System.out.println("No attachment");
            }
        } catch (IOException e) {
            System.out.println("IOE getAttachment");
            e.printStackTrace();
        }

    }
}
