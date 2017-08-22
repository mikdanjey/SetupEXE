package com.mikdanjey.setupexe;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

public class TableViewController  implements Initializable {

    @FXML
    private TableView<UsersEntity> userTable;

    @FXML
    private TableColumn<UsersEntity, String> firstNameColumn;

    @FXML
    private TableColumn<UsersEntity, String> lastNameColumn;

    @FXML
    private TableColumn<UsersEntity, String> emailColumn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));

        userTable.setEditable(true);

        userTable.setItems(getUser());


        backgroundDataSearch();
    }

    private void backgroundDataSearch() {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(
                () -> Platform.runLater(() ->  userTable.setItems(getUser()) ),
                0,
                3,
                TimeUnit.SECONDS);
    }

    private ObservableList<UsersEntity> getUser() {
        ObservableList<UsersEntity> usersEntityObservableList = FXCollections.observableArrayList();

        usersEntityObservableList.addAll(listUsers());
        return usersEntityObservableList;
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
}
