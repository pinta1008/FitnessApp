package com.example.fitnessapp.models;

public class ModelCreatedTraining {
    String id,name,ponavljanja, serije, ukupno, image;

    public ModelCreatedTraining() {
    }

    public ModelCreatedTraining(String id, String name, String ponavljanja, String serije, String ukupno, String image) {
        this.id = id;
        this.name = name;
        this.ponavljanja = ponavljanja;
        this.serije = serije;
        this.ukupno = ukupno;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPonavljanja() {
        return ponavljanja;
    }

    public void setPonavljanja(String ponavljanja) {
        this.ponavljanja = ponavljanja;
    }

    public String getSerije() {
        return serije;
    }

    public void setSerije(String serije) {
        this.serije = serije;
    }

    public String getUkupno() {
        return ukupno;
    }

    public void setUkupno(String ukupno) {
        this.ukupno = ukupno;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
