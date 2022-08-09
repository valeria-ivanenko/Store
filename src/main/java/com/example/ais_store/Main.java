package com.example.ais_store;
import com.example.ais_store.TCP.ServerTCP;
import com.example.ais_store.DB.DbUtils;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class Main extends Application {

    ServerTCP server;

    public static void main(String[] args) throws SQLException {
        DbUtils.initialization("db");
        launch(args);
    }

    @FXML
    public void start(Stage stage) throws SQLException, IOException {
        server = new ServerTCP(8888);
        new Thread(server).start();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-view.fxml")));
        Scene scene = new Scene(root);
        //stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
