package com.progetto.progettoposta;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerApplication extends Application {
    public void start(Stage stage) throws IOException, InterruptedException, ClassNotFoundException {


        FXMLLoader fxmlLoader2 = new FXMLLoader(Server.class.getResource("InterfacciaServer.fxml"));
        Scene scene2 = new Scene(fxmlLoader2.load(), 470, 250);
        Stage stage2 = new Stage();
        stage2.setTitle("S");
        ServerController contr1 = fxmlLoader2.getController();
        contr1.initialize();
        stage2.setScene(scene2);
        stage2.show();
    }


}
