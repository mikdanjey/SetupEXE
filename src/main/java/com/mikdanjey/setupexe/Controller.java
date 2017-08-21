package com.mikdanjey.setupexe;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.HibernateException;
import org.hibernate.Metamodel;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.metamodel.EntityType;
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

    private ObservableList<UsersEntity> usersEntityObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("FirstName"));

        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));

        userTable.setEditable(true);
        userTable.setItems(getUser());
        userTable.setItems(usersEntityObservableList);

        unlimitedrunable();
    }

    private ObservableList<UsersEntity> getUser() {
        usersEntityObservableList = FXCollections.observableArrayList();

        List<UsersEntity> users = listUsers();
        for (UsersEntity u : users) {
            usersEntityObservableList.add(u);
        }
        return usersEntityObservableList;
    }

    @FXML
    private void submit_action() {

        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setFirstName(first_name_textbox.getText());
        usersEntity.setLastName(last_name_textbox.getText());
        usersEntity.setEmail(email_textbox.getText());
        UsersModel.create(usersEntity);

        List<UsersEntity> users = listUsers();
        for (UsersEntity u : users) {
            System.out.print(u.getId() + " ");
            System.out.print(u.getFirstName() + " ");
            System.out.print(u.getLastName() + " ");
            System.out.print(u.getEmail() + " ");
            System.out.println();
        }
    }

    private void listAll() {
        final Session session = Main.getSession();
        try {
            System.out.println("querying all the managed entities...");
            final Metamodel metamodel = session.getSessionFactory().getMetamodel();
            for (EntityType<?> entityType : metamodel.getEntities()) {
                final String entityName = entityType.getName();
                final Query query = session.createQuery("from " + entityName);
                System.out.println("executing: " + query.getQueryString());
                for (Object o : query.list()) {
                    System.out.println("  " + o);
                }
            }
        } finally {
            session.close();
        }
    }

    private List listUsers() {
        Transaction tx;
        List users = new ArrayList<>();
        try (Session session = Main.getSession()) {
            tx = session.beginTransaction();
            users = session.createQuery("From UsersEntity").list();
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }

        return users;
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
