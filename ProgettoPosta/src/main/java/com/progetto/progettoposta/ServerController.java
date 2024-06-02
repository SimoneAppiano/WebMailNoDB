package com.progetto.progettoposta;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;

public class ServerController {
    @FXML
    private ListView<String> lista_Azioni;
    @FXML
    private Button start;
    @FXML
    private Button Stop;

    private Server serv;


    public void initialize() throws IOException, ClassNotFoundException {


    }
    @FXML
    protected void startServer() {
            new Thread(() -> {
                serv = new Server();
                lista_Azioni.itemsProperty().bind(serv.inboxProperty());//binding tra lstMessage e inboxProperty
                serv.listen(4446);
            }).start();

    }
@FXML
   protected void stopServer(){
        serv.stopServer();
    }

}


