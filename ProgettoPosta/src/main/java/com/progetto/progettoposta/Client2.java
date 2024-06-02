package com.progetto.progettoposta;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Client2 extends Application {
    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        Client c = new Client("Marco@gmail.com");
//        new Thread(c).start();
        FXMLLoader fxmlLoader = new FXMLLoader(Client1.class.getResource("Prog3Interfaccia.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 400);
        stage.setTitle("Client");
        HelloController contr = fxmlLoader.getController();
        contr.initialize(c);
        stage.setOnHidden(e -> {
            try {


                contr.setTerterminateThread();
                System.exit(0);
            } catch (IOException ex) {
                System.out.println("Il server e' spento");
                try {
                    contr.setTerterminateThread();
                } catch (IOException exc) {
                    throw new RuntimeException(exc);
                }
            }
        });
        stage.setScene(scene);
        stage.show();

    }

    public void stop() throws Exception {

        System.out.println("Chiudi tuttooooooooo");

    }





}

