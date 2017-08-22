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
import org.hibernate.Transaction;

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

        submit_button.autosize();

        userTable.setItems(getUser());

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

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();

        userTable.setItems(getUser());

    }

    private List<UsersEntity> listUsers() {
        List users = new ArrayList<>();
        Transaction tx;
        try (Session session = Main.getSession()) {
            tx = session.beginTransaction();
            users = session.createQuery("From UsersEntity").list();
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            e.printStackTrace();
        }

        return users;
    }

    private void backgroundDataSearch() {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(
                () -> Platform.runLater(() -> System.out.println("ok") ),
                0,
                5,
                TimeUnit.SECONDS);
    }
}
