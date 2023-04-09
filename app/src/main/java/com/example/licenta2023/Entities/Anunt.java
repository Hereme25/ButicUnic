package com.example.licenta2023.Entities;

public class Anunt {
    private String titlu;
    private String proprietar;
    private String categorie;
    private String email;
    private Double pret;
    private String descriere;
    private String localizare;
    private String telefon;

    public Anunt() {
    }

    public Anunt(String titlu, String proprietar, String categorie, String email, Double pret, String descriere, String localizare, String telefon) {
        this.titlu = titlu;
        this.categorie = categorie;
        this.email = email;
        this.pret = pret;
        this.proprietar=proprietar;
        this.descriere = descriere;
        this.localizare = localizare;
        this.telefon = telefon;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getPret() {
        return pret;
    }

    public void setPret(Double pret) {
        this.pret = pret;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getLocalizare() {
        return localizare;
    }

    public void setLocalizare(String localizare) {
        this.localizare = localizare;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getProprietar() {
        return proprietar;
    }

    public void setProprietar(String proprietar) {
        this.proprietar = proprietar;
    }
}
