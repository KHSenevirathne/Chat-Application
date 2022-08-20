package client_side;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.io.IOException;

public class LoginFormController  {
    public JFXTextField txtUserName;
    public JFXButton btnSignIn;

    public void signInOnAction(ActionEvent actionEvent) throws IOException {
        if (txtUserName.getText().isEmpty()){new Alert(Alert.AlertType.WARNING,"Enter your User Name").show();return;}
        Controller.userNameFormLogin=txtUserName.getText();
        new FxmlLoader().chooseFxml("sample.fxml",actionEvent);
    }

}
