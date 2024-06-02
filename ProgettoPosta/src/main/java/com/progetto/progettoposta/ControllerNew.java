package com.progetto.progettoposta;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ControllerNew {

    @FXML
    private Label mit;
    @FXML
    private TextField dest;
    @FXML
    private TextField arg;
    private UUID value;
    @FXML
    private TextArea text;
    private Client model;

    public void initialize(Client c, Email selectedEmail) {
       model=c;
       mit.setText(model.indirizzoEmailProperty().getValue());
       value=UUID.randomUUID();
    }
    public List<String> addRic(){

        return new ArrayList<String>(Arrays.asList(dest.getText().split(",")));
    }
    public void handleSend(ActionEvent event) throws IOException, ClassNotFoundException, InterruptedException {

        Email email=new Email(value,mit.getText(), addRic() ,arg.getText(),text.getText(), LocalDate.now());
        model.addEmail(email);


    }
}
