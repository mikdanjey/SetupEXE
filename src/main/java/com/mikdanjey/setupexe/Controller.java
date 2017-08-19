package com.mikdanjey.setupexe;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Label time_label;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        time_label.setText("OK");
    }
}
