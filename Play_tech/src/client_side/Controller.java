package client_side;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public AnchorPane ap_main;
    public Label lblUser;
    @FXML
    private Button button_send;
    @FXML
    private TextField tf_message;
    @FXML
    private VBox vbox_messages;
    @FXML
    private ScrollPane sp_main;

    private Client client;
    public static String userNameFormLogin;
    private String userName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            userName=userNameFormLogin;
            client = new Client(new Socket("localhost",1234),userName);
            client.sendMessageToServer(userName);
            lblUser.setText(userName);
        }catch (IOException e ){
            e.printStackTrace();
            System.out.println("error connecting with the server");
        }

        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((double) newValue);
            }
        });
        client.receiveMessageFromServer(vbox_messages);

        button_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String messageToSend = tf_message.getText();
                if (!messageToSend.isEmpty()){
                    HBox hBox =  new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT );
                    hBox.setPadding(new Insets(5,5,5,10));
                    Text text = new Text(messageToSend);
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setStyle("-fx-color: rgb(0,0,0);"+"-fx-background-color: rgb(255,184,28);"+"-fx-background-radius: 10px 0 10px 10px");
                    textFlow.setPadding(new Insets(5,5,5,10));
                    text.setFill(Color.color(.0,.0,.0));
                    hBox.getChildren().add(textFlow);
                    vbox_messages.getChildren().add(hBox);
                    client.sendMessageToServer(userName+" : "+messageToSend);
                    tf_message.clear();
                }
            }
        });
    }

    public static void  addLabel(String messageFromServer,VBox vBox){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        Text text = new Text(messageFromServer);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(241,235,156);"+"-fx-background-radius:  0 10px 10px 10px");
        textFlow.setPadding(new Insets(5,5,5,10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }
}
