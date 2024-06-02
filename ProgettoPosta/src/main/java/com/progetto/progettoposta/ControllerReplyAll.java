package com.progetto.progettoposta;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ControllerReplyAll {
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

        Email email=new Email(value,mit.getText(), addRic() ,arg.getText(),risp.getText()+"\n"+"\n"+"\n"+"MESSAGGIO ORIGINALE"+"\n"+text.getText(), LocalDate.now());
        model.addEmail(email);


    }

    public List<String> addRic(){
        selected.getRiceventi().clear();
        selected.addRiceventi(new ArrayList<String>(Arrays.asList(dest.getText().split(","))));
        return selected.getRiceventi();
    }

    public String StringBuilder(){
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(selected.getMandante()).append(",");
        for (int i = 0; i <selected.getRiceventi().size(); i++) {
            if(!selected.getRiceventi().get(i).equals(model.indirizzoEmailProperty().getValue())) {
                strBuilder.append(selected.getRiceventi().get(i)).append(",");
            }

        }
        String newString = strBuilder.toString();
        return newString;
    }

    public void initialize(Client c, Email email) {
        model=c;
        selected=email;
        value= UUID.randomUUID();
        mit.setText(model.indirizzoEmailProperty().getValue());
        dest.setText(StringBuilder());
        arg.setText(selected.getOggetto());
        text.setText(selected.getTesto());
        invia=new Button();

    }

}
