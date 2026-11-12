package com.ensaa.hotelino.entity;

import java.util.ArrayList;
import java.util.List;

public class Equipement {

    private Long id;
    private String libelle;
    private List<Chambre> chambres = new ArrayList<>();

    public Equipement() {
    }

    public Equipement(String libelle) {
        this.libelle = libelle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<Chambre> getChambres() {
        return chambres;
    }

    public void setChambres(List<Chambre> chambres) {
        this.chambres = chambres;
    }
}
