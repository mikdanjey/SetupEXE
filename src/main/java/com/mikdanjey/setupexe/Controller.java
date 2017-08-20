package com.mikdanjey.setupexe;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.hibernate.HibernateException;
import org.hibernate.Metamodel;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.metamodel.EntityType;
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
    private void submit_action() {
        insertUser(first_name_textbox.getText(), last_name_textbox.getText(), email_textbox.getText());
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

    private int insertUser(String fname, String lname, String email) {
        final Session session = Main.getSession();
        Transaction tx = null;
        int userIdSaved = 0;
        try {
            tx = session.beginTransaction();
            UsersEntity u = new UsersEntity(fname, lname, email);
            userIdSaved = (int) session.save(u);
            tx.commit();

        } catch (HibernateException ex) {
            if (tx != null)
                tx.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }

        return userIdSaved;

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
