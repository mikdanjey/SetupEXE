package com.mikdanjey.setupexe;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField first_name_textbox;

    @FXML
    private TextField last_name_textbox;

    @FXML
    private TextField email_textbox;

    @FXML
    private Button submit_button;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            new TableViewStarter().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        submit_button.autosize();
    }


    @FXML
    private void submit_action() {

        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setFirstName(first_name_textbox.getText());
        usersEntity.setLastName(last_name_textbox.getText());
        usersEntity.setEmail(email_textbox.getText());
        String message = UsersModel.create(usersEntity);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
