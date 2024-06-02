package com.progetto.progettoposta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.time.LocalTime.now;

public class Email implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private UUID ID;
    private String mandante;
    private List<String> riceventi;
    private String oggetto;
    private String testo;
    private LocalDate data;

    public Email(UUID ID, String mandante, List<String> riceventi, String oggetto, String testo, LocalDate data){
        this.ID=ID;
        this.mandante= mandante;
        this.riceventi=new ArrayList<>(riceventi);
        this.oggetto=oggetto;
        this.testo=testo;
        this.data=data;
    }

    public String getID() {return String.valueOf(ID);}
    public UUID getIdNumber() {return ID;};

    public String getMandante(){
        return mandante;
    }
    public List<String> getRiceventi(){
        return riceventi;
    }

    public void setRiceventi(List<String> ric){
        riceventi=ric;
    }
    public void addRiceventi(List<String> add){
        riceventi.addAll(add);

    }
    public String getOggetto(){
        return oggetto;
    }
    public String getTesto(){
        return testo;
    }
    public String getData(){return String.format(String.valueOf(LocalDate.now()));}
    @Override
    public String toString() {
        return String.join(" - ", List.of(this.mandante,this.oggetto));
    }
    transient String thisFieldWontBeSerialized;
}


