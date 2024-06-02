package com.progetto.progettoposta;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ControllerReply {
    @FXML
    private Label mit;
    @FXML
    private Label dest;
    @FXML
    private Label arg;
    @FXML
    private Label text;
    @FXML
    private TextArea risp;
    @FXML
    private Button invia;
    private Client model;
    private Email selected;
    private UUID value;



    public void handleSend(ActionEvent event) throws IOException, ClassNotFoundException {

        Email email=new Email(value,mit.getText(), List.of(dest.getText()),arg.getText(),risp.getText(), LocalDate.now());
        model.addEmail(email);

    }

    public void initialize(Client c, Email email) {
        model=c;
        selected=email;
        value= UUID.randomUUID();
        mit.setText(model.indirizzoEmailProperty().getValue());
        dest.setText(selected.getMandante());
        arg.setText(selected.getOggetto());
        text.setText(selected.getTesto());
        invia=new Button();

    }
}


