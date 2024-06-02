package com.progetto.progettoposta;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class ControllerForward {
    @FXML
    private Label mit;
    @FXML
    private TextField newDest;
    @FXML
    private Label arg;
    @FXML
    private Label text;
    @FXML
    private Button invia;
    private Client model;
    private Email selected;
    private UUID value;




    public void handleSend(ActionEvent event) throws IOException, ClassNotFoundException {

        Email email=new Email(selected.getIdNumber(), selected.getMandante(), new ArrayList<String>(Arrays.asList(newDest.getText().split(","))) ,selected.getOggetto(), selected.getTesto(), LocalDate.now());
        model.addEmail(email);


    }

    public void initialize(Client c, Email email) {
        model=c;
        selected=email;
        value=email.getIdNumber();
        mit.setText(model.indirizzoEmailProperty().getValue());
        arg.setText(selected.getOggetto());
        text.setText(selected.getTesto());
        invia=new Button();

    }
}
