package com.mikdanjey.setupexe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Main extends Application {

    private static final SessionFactory ourSessionFactory;

    private static final Properties properties;
    private static final String PropertiesFile = "Application.properties";
    private static final String PropertiesSQLUserName = "UserName";
    private static final String PropertiesSQLPassword = "Password";
    private static final String PropertiesSQLHost = "Host";
    private static final String PropertiesSQLPort = "Port";
    private static final String PropertiesSQLDatabase = "Database";

    private static final String connection_driver_class_key = "hibernate.connection.driver_class";
    private static final String connection_dialect_key = "hibernate.dialect";
    private static final String connection_url_key = "hibernate.connection.url";
    private static final String connection_username_key = "hibernate.connection.username";
    private static final String connection_password_key = "hibernate.connection.password";

    private static String connection_driver_class_value = "com.mysql.cj.jdbc.Driver";
    private static String connection_dialect_value = "org.hibernate.dialect.MySQL5Dialect";

    private static String connection_host;
    private static String connection_post;
    private static String connection_database;

    private static String connection_url_value;
    private static String connection_username_value;
    private static String connection_password_value;
    private static FileReader fileReader;

    static {

        properties = new Properties();

        configLoad();

        try {
            Configuration configuration = new Configuration();
            configuration.configure("/hibernate.cfg.xml");

            configuration.getProperties().setProperty(connection_driver_class_key, connection_driver_class_value);
            configuration.getProperties().setProperty(connection_dialect_key, connection_dialect_value);
            configuration.getProperties().setProperty(connection_url_key, connection_url_value);
            configuration.getProperties().setProperty(connection_username_key, connection_username_value);
            configuration.getProperties().setProperty(connection_password_key, connection_password_value);
            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static void configLoad() {
        try {
            fileReader = new FileReader(PropertiesFile);
        } catch (FileNotFoundException e) {
            configSet();
        }
        try {
            properties.load(fileReader);
            connection_host = properties.getProperty(PropertiesSQLHost, "localhost");
            connection_post = properties.getProperty(PropertiesSQLPort, "3306");
            connection_database = properties.getProperty(PropertiesSQLDatabase, "test");

            connection_url_value = "jdbc:mysql://" + connection_host + ":" + connection_post + "/" + connection_database;

            connection_username_value = properties.getProperty(PropertiesSQLUserName, "root");
            connection_password_value = properties.getProperty(PropertiesSQLPassword, "root");

        } catch (IOException e) {
            configSet();
        }
    }

    private static void configSet() {
        properties.setProperty(PropertiesSQLHost, "localhost");
        properties.setProperty(PropertiesSQLPort, "3306");
        properties.setProperty(PropertiesSQLUserName, "root");
        properties.setProperty(PropertiesSQLPassword, "root");
        properties.setProperty(PropertiesSQLDatabase, "test");

        try {
            properties.store(new FileWriter(PropertiesFile), "Setup EXE Info");
            configLoad();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Setup EXE");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}

