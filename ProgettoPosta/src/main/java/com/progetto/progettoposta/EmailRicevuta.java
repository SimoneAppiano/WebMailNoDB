package com.progetto.progettoposta;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class EmailRicevuta {
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Socket socket;
    private FileOutputStream fileOut;

    public EmailRicevuta(){

    }


    public synchronized boolean WriteObjectToFile(Email serObj) throws IOException {
        boolean andato=false;
        if(checkEmail(serObj.getRiceventi())) {
            try {
                andato=true;
                fileOut = new FileOutputStream("C:\\Users\\miste\\IdeaProjects\\ProgettoPosta\\src\\main\\resources\\com\\progetto\\progettoposta\\test.txt", true);

                outputStream = new ObjectOutputStream(fileOut);
                outputStream.writeObject(serObj);

                outputStream.flush();
                int i = 0;
                while (i <= serObj.getRiceventi().size() - 1) {
                    fileOut = new FileOutputStream("C:\\Users\\miste\\IdeaProjects\\ProgettoPosta\\src\\main\\resources\\com\\progetto\\progettoposta\\" + serObj.getRiceventi().get(i) + ".txt", true);
                    outputStream = new ObjectOutputStream(fileOut);
                    outputStream.writeObject(serObj);
                    outputStream.flush();
                    i++;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return andato;
        }else {
            Platform.runLater(() -> {
            Stage stage = new Stage();
            Label ta = new Label();
            Scene scene = new Scene(ta, 600, 20);
            stage.setTitle("Errore");
            ta.setText("L'indirizzo inserito non esiste");
            stage.setScene(scene);
            stage.show();

            });
            return andato; }
    }

    public void reWriteObjectToFile(String filepath, Email em, String owner) {
        File myObj1= new File("C:\\Users\\miste\\IdeaProjects\\ProgettoPosta\\src\\main\\resources\\com\\progetto\\progettoposta\\"+owner+".txt");
        try {

            fileOut=new FileOutputStream(myObj1,true);
            outputStream = new ObjectOutputStream(fileOut);
            outputStream.writeObject(em);
            System.out.println("Email scritta correttamente");


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public synchronized boolean checkEmail(List<String> nomeEmail) throws IOException {
        boolean check=false;
        File myObj1= new File("C:\\Users\\miste\\IdeaProjects\\ProgettoPosta\\src\\main\\resources\\com\\progetto\\progettoposta\\Database.txt");
        List<String> lines = Files.readAllLines(Paths.get(myObj1.getPath()));
        for (String a:nomeEmail) {
            System.out.println(a);
            if (lines.contains(a)) {
                check = true;
            }else{
                check =false;
                break;
            }
        }
        return check;
    }
    public boolean checkEmail2(String nomeEmail) throws IOException {
        boolean check=false;
        File myObj1= new File("C:\\Users\\miste\\IdeaProjects\\ProgettoPosta\\src\\main\\resources\\com\\progetto\\progettoposta\\Database.txt");
        List<String> lines = Files.readAllLines(Paths.get(myObj1.getPath()));
        System.out.println(nomeEmail);
        for (String s:lines) {
            System.out.println(s);
            if (s.equals(nomeEmail)) {
                check = true;
                break;
            }
        }

        return check;
    }

    public void RiceviEmailDaCancellare(Email email, String owner) throws IOException {

        File myObj1= new File("C:\\Users\\miste\\IdeaProjects\\ProgettoPosta\\src\\main\\resources\\com\\progetto\\progettoposta\\"+owner+".txt");

        List<Email> e1 = ReadObjectFromFile(myObj1,email);
        new FileOutputStream(myObj1,false).close();
        //e1.remove(email);
        for(Email es :e1) {

            if (!es.getID().equals(email.getID())) {

                reWriteObjectToFile("C:\\Users\\miste\\IdeaProjects\\ProgettoPosta\\src\\main\\resources\\com\\progetto\\progettoposta\\"+owner+".txt", es,owner);
            }

        }

    }

    public List<Email> ReadObjectFromFile(File myObj, Email email) throws IOException {

        FileInputStream fi = new FileInputStream(myObj);
        List<Email> e1 = new LinkedList<Email>();
        boolean possible=true;
        while (possible) {
            try {
                inputStream = new ObjectInputStream(fi);
                Email em1= (Email) inputStream.readObject();
                if(em1!=null){

                    e1.add(em1);

                }else{
                    possible=false;

                }

            } catch (EOFException ex) {
                break;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return e1;
    }


}