package Mercury.Controllers;

import Mercury.Model.Message;
import Mercury.Services.MessageServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class InboxController implements Initializable {

    private ObservableList<Message> messageObservableList;
    @FXML
    private Label msg_receiver;
    @FXML
    private ListView<Message> listView;


    public void setReceiver(String currentUser) {
        this.msg_receiver.setText(currentUser);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setItems(messageObservableList);
        listView.setCellFactory(messageListView -> new MessageListViewCell("inbox"));
    }

    public void refresh() {
        MessageServices ms = new MessageServices();
        messageObservableList = FXCollections.observableArrayList();
        System.out.println(msg_receiver.getText());
        try {
            for (Message message : ms.getUserMessages(msg_receiver.getText())) {
                messageObservableList.add(message);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException InboxController");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQLException InboxController");
            e.printStackTrace();
        }
        listView.setItems(messageObservableList);
        listView.setCellFactory(messageListView -> new MessageListViewCell("inbox"));
    }

}
