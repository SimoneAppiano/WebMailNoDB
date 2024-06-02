package com.progetto.progettoposta;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class HelloController extends Thread{
    @FXML
    private Label user;
    @FXML
    private Label mandante;
    @FXML
    private Label riceventi;
    @FXML
    private Label oggetto;
    @FXML
    private Label ID;
    @FXML
    private Label data;
    @FXML
    private ListView<Email> listaEmail;
    @FXML
    private TextArea testo;
    @FXML
    private Button scrivi;

    @FXML
    private Button inoltra;
    @FXML
    private Button risp;
    @FXML
    private Button rispAll;
    @FXML
    private Button Cancella;
    private Client model;
    private Email emptyEmail;
    private Email selectedEmail;
    private boolean terminateThread=true;
    private final int[] lengthList = new int[1];



    @FXML
public void initialize(Client c) throws IOException {

    if (this.model != null)
        throw new IllegalStateException("Model can only be initialized once");
    model= c;
    new Thread(c).start();

    //model.generateRandomEmails(4);
    selectedEmail = null;
    risp.setVisible(false);
    rispAll.setVisible(false);
    inoltra.setVisible(false);
    Cancella.setVisible(false);
    //binding tra listaEmail e inboxProperty
    listaEmail.itemsProperty().bind(c.inboxProperty());
    lengthList[0] = model.inboxProperty().size();
    listaEmail.setOnMouseClicked(this::showSelectedEmail);
    user.textProperty().bind(c.indirizzoEmailProperty());
    emptyEmail = new Email(null,"", List.of(""), "", "", LocalDate.now());
    updateDetailView(emptyEmail);
        updateContiueList(c);

    }
    @FXML
    protected void onDeleteButtonClick() throws IOException, ClassNotFoundException {

        model.deleteEmail(selectedEmail,model.indirizzoEmailProperty().getValue());
        risp.setVisible(false);
        rispAll.setVisible(false);
        inoltra.setVisible(false);
        Cancella.setVisible(false);
        updateDetailView(emptyEmail);
    }


    @FXML
    protected void onAddButtonClick() throws IOException {
        //Email em=new Email(1,model.indirizzoEmailProperty().getValue(),List.of(this.mandante.getText()),"suca",this.testo.getText(),LocalDate.now());
        //model.addEmail(em);
        Email email = listaEmail.getSelectionModel().getSelectedItem();
        selectedEmail = email;
        FXMLLoader fxmlLoader = new FXMLLoader(Client1.class.getResource("replyMail.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Stage stage = new Stage();
        ControllerReply contr=fxmlLoader.getController();
        contr.initialize(model,selectedEmail);
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    protected void replyAll() throws IOException {
        FXMLLoader fxmlLoader1 = new FXMLLoader(Client1.class.getResource("replyAllMail.fxml"));
        Scene scene1 = new Scene(fxmlLoader1.load(), 320, 240);
        Stage stage1 = new Stage();
        ControllerReplyAll contr1=fxmlLoader1.getController();
        contr1.initialize(model,selectedEmail);
        stage1.setScene(scene1);
        stage1.show();
    }

    @FXML
    protected void forwardEmail() throws IOException {
        FXMLLoader fxmlLoader1 = new FXMLLoader(Client1.class.getResource("forward.fxml"));
        Scene scene1 = new Scene(fxmlLoader1.load(), 320, 240);
        Stage stage1 = new Stage();
        ControllerForward contr1=fxmlLoader1.getController();
        contr1.initialize(model,selectedEmail);
        stage1.setScene(scene1);
        stage1.show();
    }
    @FXML
    protected void nuovaEmail() throws IOException {
        FXMLLoader fxmlLoader1 = new FXMLLoader(Client1.class.getResource("sendMail.fxml"));
        Scene scene1 = new Scene(fxmlLoader1.load(), 320, 240);
        Stage stage1 = new Stage();
        ControllerNew contr1=fxmlLoader1.getController();
        contr1.initialize(model, selectedEmail);
        stage1.setScene(scene1);
        stage1.show();
    }

    /**
     * Mostra la mail selezionata nella vista
     */
    protected void showSelectedEmail(MouseEvent mouseEvent) {
        Email email = listaEmail.getSelectionModel().getSelectedItem();
        risp.setVisible(true);
        rispAll.setVisible(true);
        inoltra.setVisible(true);
        Cancella.setVisible(true);
        selectedEmail = email;
        updateDetailView(email);

    }
    protected void updateDetailView(Email email) {
        if(email != null) {
            ID.setText(email.getID());
            mandante.setText(email.getMandante());
            riceventi.setText(String.join(", ", email.getRiceventi()));
            oggetto.setText(email.getOggetto());
            testo.setText(email.getTesto());
            data.setText(String.valueOf(email.getData()));


        }


    }
    private synchronized void updateContiueList(Client c) throws IOException {
        new Thread(() -> {
            while(terminateThread) {
                try {
                    while (getTerterminateThread()) {
                        Thread.sleep(5000);
                        Platform.runLater(() -> {

                            if((lengthList[0]-(model.inboxProperty().size())) != 0){
                                lengthList[0] = model.inboxProperty().size();
                                listMod();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    private void listMod() {
        Stage stage = new Stage();
        Label ta = new Label();
        Scene scene = new Scene(ta,600,20);
        stage.setTitle("Messaggio info");
        ta.setText("Cambiamento lista. Una email è stata scritta,ricevuta,o eliminata");
        stage.setScene(scene);
        stage.show();
        //stage.close();
    }

    @FXML
    public void setTerterminateThread() throws IOException {
        terminateThread = !terminateThread;
    }
    private Boolean getTerterminateThread(){
        return terminateThread;
    }



    }