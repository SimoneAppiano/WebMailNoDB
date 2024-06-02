package com.progetto.progettoposta;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.*;

import static java.time.LocalTime.now;

public class Client implements Runnable {
    File myObj = new File("C:\\Users\\miste\\IdeaProjects\\ProgettoPosta\\src\\main\\resources\\com\\progetto\\progettoposta\\test.txt");


    final int MAX_ATTEMPTS = 5;
    private final ListProperty<Email> inbox;
    private final ObservableList<Email> contenutoInbox;
    private final StringProperty indirizzoEmail;
    private final List<Email> listaEmail=new LinkedList<>();
    Socket socket = null;
    ObjectOutputStream outputStream = null;
    ObjectInputStream inputStream = null;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    boolean connesso = false;
    boolean running=true;


    public Client(String indirizzoEmail) {
        this.contenutoInbox = FXCollections.observableList(new LinkedList<>());
        this.inbox = new SimpleListProperty<>();
        this.inbox.set(contenutoInbox);
        this.indirizzoEmail = new SimpleStringProperty(indirizzoEmail);


    }


    public ListProperty<Email> inboxProperty() {
        return inbox;
    }

    public StringProperty indirizzoEmailProperty() {
        return indirizzoEmail;
    }

    public void deleteEmail(Email email, String owner) throws IOException, ClassNotFoundException {
        EmailRicevuta er = new EmailRicevuta();
        socket = new Socket("127.0.0.1", 4446);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        ArrayList e1= new ArrayList<>();
        e1.add(indirizzoEmailProperty().getValue());
        e1.add(email);
        e1.add("Cancella");
        outputStream.writeObject(e1);
        outputStream.flush();
        inbox.remove(email);
//        er.RiceviEmailDaCancellare(email, owner);
    }

    public void addEmail(Email em) throws IOException, ClassNotFoundException {
        socket = new Socket("127.0.0.1", 4446);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        ArrayList e1= new ArrayList<>();
        e1.add(indirizzoEmailProperty().getValue());
        e1.add(em);
        e1.add("ADD");
        outputStream.writeObject(e1);
        outputStream.flush();

//        ser.addEmail(em);
    }



    public synchronized void communicate(String host, int port) throws InterruptedException {
        int attempts = 0;

        boolean success = false;
        while (attempts < MAX_ATTEMPTS && !success) {
            attempts += 1;
            System.out.println("[Client " + this.indirizzoEmail.toString() + "] Tentativo nr. " + attempts);

            success = tryCommunication(host, port);
            if (success) {
                continue;
            }
            Thread.sleep(5000);
            try {
               Thread.sleep(5000);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
    public synchronized void ListaEmail() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (socket.isConnected()) {
                    try {
                       ArrayList obj = new ArrayList<>();
                        obj.add(indirizzoEmailProperty().getValue());
                        obj.add(listaEmail);
                        obj.add("Ricarica");

                        outputStream = new ObjectOutputStream(socket.getOutputStream());

                        outputStream.writeObject(obj);
                        outputStream.flush();
                        inputStream = new ObjectInputStream(socket.getInputStream());
                        listFromServer((List<Email>) inputStream.readObject(), inboxProperty());
                        connesso = true;
                        closeStreams();

                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    private boolean tryCommunication(String host, int port) {

        try {

            connectToServer(host, port);
            Thread.sleep(8000);
            return true;
        } catch (ConnectException ce) {
            // nothing to be done
            return false;
        } catch (IOException ie) {
            ie.printStackTrace();
            return false;
        } catch (ClassNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }



    private void connectToServer(String host, int port) throws IOException, ClassNotFoundException {
        socket = new Socket(host, port);
        ListaEmail();
        System.out.println("[Client " + this.indirizzoEmail.getValue() + "] Connesso");

    }
    public void closeStreams() {
        try {
            if (inputStream!= null) {

                inputStream.close();

            }

            if (outputStream != null) {

                outputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void closeConnections() {
        if (socket != null) {
            try {
                socket = new Socket("127.0.0.1", 4446);
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                ArrayList obj = new ArrayList<>();
                obj.add(indirizzoEmailProperty().getValue());
                String s=indirizzoEmailProperty().getValue()+" si e' disconnesso";
                obj.add(s);
                outputStream.writeObject(obj);
                outputStream.flush();
                closeStreams();
                connesso=false;
                socket.close();
            } catch (IOException e) {
                connesso=false;
                System.out.println("Server not connected");
            }
        }

    }

    private void listFromServer(List<Email> el, List<Email> list){
        Platform.runLater(() -> {list.addAll(el);listaEmail.addAll(el);connesso=true;closeConnections();});
    }

    @Override
    public void run() {
        while(running){
            try {
                communicate("127.0.0.1", 4446);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}







