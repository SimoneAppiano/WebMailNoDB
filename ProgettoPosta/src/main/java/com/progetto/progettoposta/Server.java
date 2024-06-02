package com.progetto.progettoposta;




import com.progetto.progettoposta.Email;
import com.progetto.progettoposta.EmailRicevuta;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;


    private boolean running = true;
    Socket socket = null;
    ObjectInputStream inStream = null;
    ObjectOutputStream outStream = null;
    ObjectOutputStream objectOut;
    private final ObservableList<String> inboxContent;
    private final ListProperty<String> inbox;
    public static String owner;
    EmailRicevuta er = new EmailRicevuta();
    private ExecutorService pool =null;
    private final int numberCore;

    public Server() {
        numberCore = Runtime.getRuntime().availableProcessors();
        this.inboxContent = FXCollections.observableList(new LinkedList<>());
        this.inbox = new SimpleListProperty<>();
        this.inbox.set(inboxContent);
        inboxContent.add("[Server] Server Invocato");
    }

    public ListProperty<String> inboxProperty() {
        return inbox;
    }

    /**
     * Il server si mette in ascolto su una determinata porta e serve i client.
     * I messaggi ricevuti dai client vengono stampati a video, modificati e inviati
     * nuovamente al mittente.
     * <p>
     * NB: Questa implementazione del server usa un solo thread, NON è un'implementazione
     * scalabile, NON è sufficiente ai fini del progetto.
     *
     * @param port la porta su cui è in ascolto il server.
     */
    public synchronized void listen(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            pool = Executors.newFixedThreadPool(numberCore);
            while (running) {
                serveClient(serverSocket);
            }
            closeSocket(socket);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {

//            if (socket!=null)
//                try {
//
//                    socket.close();
//                    closeStreams();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            try {
//                wait(100);
////                listen(4446);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }
    }

    private void closeSocket(Socket socket) {
        if (socket.isConnected())
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    // Gestisce un singolo client
    private synchronized void serveClient(ServerSocket serverSocket) throws InterruptedException {

        try {

            openStreams(serverSocket);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    // Chiude gli stream utilizzati durante l'ultima connessione
    public void closeStreams() {
        try {
            if (inStream != null) {

                inStream.close();

            }

            if (outStream != null) {

                outStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        running = false;
        messageFromServer("[Server] Server Terminato", inboxContent);
        closeStreams();
        closeSocket(socket);
        System.exit(0);

    }


    // apre gli stream necessari alla connessione corrente
    private void openStreams(ServerSocket serverSocket) throws IOException, ClassNotFoundException {
        socket = serverSocket.accept();

        pool.execute(new Handler(socket));

//        messageCheck(inStream.readObject().toString());

    }


    public  List<Email> ReadObjectFromFile() throws IOException, ClassNotFoundException {
        File myObj = new File("C:\\Users\\miste\\IdeaProjects\\ProgettoPosta\\src\\main\\resources\\com\\progetto\\progettoposta\\" + owner + ".txt");
        FileInputStream fi = new FileInputStream(myObj);
        List<Email> e1 = new LinkedList<Email>();
        boolean possible = true;
        while (possible) {
            try {
                inStream = new ObjectInputStream(fi);
                Email em1 = (Email) inStream.readObject();
                if (em1 != null) {
                    e1.add(em1);

                } else {
                    possible = false;

                }

            } catch (EOFException ex) {
                break;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return e1;
    }

//    private void messageCheck(Object obj) throws IOException, ClassNotFoundException {
////        Platform.runLater(() -> {
//        System.out.println("Cientro2");
//        if (obj.getClass() == String.class) {
//            owner = (String) obj;
//            try {
//                if (er.checkEmail2(owner)) {
//                    outStream = new ObjectOutputStream(socket.getOutputStream());
//                    outStream.writeObject(ReadObjectFromFile());
//                    outStream.flush();
//                }
//            } catch (IOException | ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//
//        } else if (obj.getClass() == Email.class) {
//            System.out.println("Ho ricevuto un'email");
//            er.WriteObjectToFile((Email) obj);
//            outStream = new ObjectOutputStream(socket.getOutputStream());
//            outStream.writeObject(obj);
//            System.out.println("Ho MANDATO un'email");
//            outStream.flush();
//
//        } else if (obj.getClass() == Integer.class) {
//            int i = (ReadObjectFromFile().size()) - ((Integer) obj).intValue();
//            if (i > 0) {
//                System.out.println("Email nuova");
////                List<Email> e1 = new LinkedList<Email>();
////                e1.add((Email) obj);
//                outStream = new ObjectOutputStream(socket.getOutputStream());
//                outStream.writeObject(ReadObjectFromFile().get(ReadObjectFromFile().size() - 1));
//                outStream.flush();
//            }else{
//                outStream = new ObjectOutputStream(socket.getOutputStream());
//                outStream.writeObject("Sei in pari");
//            }
//
//        }
//
//
////        });
//    }



    private void messageFromServer(String s, List<String> list) {
        Platform.runLater(() -> list.add(s));
    }


    class Handler implements Runnable {
        private final Socket socket;
        Handler(Socket socket) { this.socket = socket; }
        public void run() {
            try {
                inStream = new ObjectInputStream(socket.getInputStream());
                ArrayList obj = new ArrayList<>();
                obj= (ArrayList) inStream.readObject();
                owner = (String) obj.get(0);
                if(obj.get(1).getClass()==LinkedList.class) {
                    messageFromServer(owner + " Si connette", inboxContent);
                    List<Email> e1 = (List<Email>) obj.get(1);
                    List<Email> e2 = ReadObjectFromFile();
                    List es = new LinkedList();
                    int i = 0;
                    boolean check;

                    if (!e1.isEmpty()) {
                        for (Email em : e2) {
                            check = false;
                            for (Email em1 : e1) {
                                if (em1.toString().equals(em.toString())) {

                                    check = true;
                                    break;
                                }
                            }
                            if (!check) {
                                es.add(em);
                            }
                        }
                    } else {

                        es.addAll(ReadObjectFromFile());
                    }
                    outStream = new ObjectOutputStream(socket.getOutputStream());
                    outStream.writeObject(es);
                    outStream.flush();
//                messageCheck(inStream.readObject());

                }else if(obj.get(1).getClass()==Email.class){
                    if(obj.get(2).equals("Cancella")){
                        er.RiceviEmailDaCancellare((Email) obj.get(1), owner);
                        messageFromServer(owner + " Ha cancellato un'email", inboxContent);
                    }else {
                        messageFromServer(owner + " manda un messaggio", inboxContent);
                        if(er.WriteObjectToFile((Email) obj.get(1))){
                            messageFromServer("MESSAGGIO INVIATO CORRETTAMENTE", inboxContent);
                        }else{
                            messageFromServer("ERRORE NELL'INVIO DEL MESSAGGIO", inboxContent);
                        }
                        outStream = new ObjectOutputStream(socket.getOutputStream());
                        outStream.writeObject(obj.get(1));
                        outStream.flush();
                    }
                } else if (obj.get(1).getClass() == String.class) {
                    messageFromServer(owner + " Si Disconnette", inboxContent);
                }
                closeStreams();
            } catch (IOException | ClassNotFoundException e) {

            } finally {
                closeSocket(socket);

            }
        }
    }
}










