package com.mikdanjey.setupexe;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller implements Initializable {

    @FXML
    private Label time_label;

    @FXML
    private TextField first_name_textbox;

    @FXML
    private TextField last_name_textbox;

    @FXML
    private TextField email_textbox;

    @FXML
    private Button submit_button;

    private int i = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        unlimitedrunable();
    }

    @FXML
    private void submit_action(){

    }

    private void unlimitedrunable() {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                time_label.setText("Thread: " + String.valueOf(i++));
                            }
                        });
                    }
                },
                1,
                1,
                TimeUnit.SECONDS);
    }

    private void startScheduledExecutorService() {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(
                new Runnable() {
                    int counter = 0;

                    @Override
                    public void run() {
                        counter++;
                        if (counter <= 100) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    time_label.setText(
                                            "isFxApplicationThread: "
                                                    + Platform.isFxApplicationThread() + "\n"
                                                    + "Counting: "
                                                    + String.valueOf(counter));
                                }
                            });
                        } else {
                            scheduler.shutdown();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    time_label.setText(
                                            "isFxApplicationThread: "
                                                    + Platform.isFxApplicationThread() + "\n"
                                                    + "-Finished-");
                                }
                            });
                        }
                    }
                },
                1,
                1,
                TimeUnit.SECONDS);
    }
}
