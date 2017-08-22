package com.mikdanjey.setupexe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TableViewStarter extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/table_view.fxml"));
        primaryStage.setTitle("Table View");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
