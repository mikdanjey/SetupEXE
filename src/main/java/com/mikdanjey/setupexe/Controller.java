package com.mikdanjey.setupexe;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller implements Initializable {

    @FXML
    private Label time_label;

    @FXML
    private TableView<UsersEntity> userTable;

    @FXML
    private TableColumn<UsersEntity, String> firstNameColumn;

    @FXML
    private TableColumn<UsersEntity, String> lastNameColumn;

    @FXML
    private TableColumn<UsersEntity, String> emailColumn;

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

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));

        userTable.setEditable(true);
        userTable.setItems(getUser());

        submit_button.autosize();

        backgroundDataSearch();
    }

    private ObservableList<UsersEntity> getUser() {
        ObservableList<UsersEntity> usersEntityObservableList = FXCollections.observableArrayList();

        usersEntityObservableList.addAll(listUsers());
        return usersEntityObservableList;
    }

    @FXML
    private void submit_action() {

        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setFirstName(first_name_textbox.getText());
        usersEntity.setLastName(last_name_textbox.getText());
        usersEntity.setEmail(email_textbox.getText());
        String message = UsersModel.create(usersEntity);

    }

    private List<UsersEntity> listUsers() {
        List users = new ArrayList<>();
        try (Session session = Main.getSession()) {
            users = session.createQuery("From UsersEntity").list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }

        return users;
    }

    private void backgroundDataSearch() {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(
                () -> Platform.runLater(() -> time_label.setText("Thread: " + String.valueOf(i++))),
                1,
                1,
                TimeUnit.SECONDS);
    }
}
