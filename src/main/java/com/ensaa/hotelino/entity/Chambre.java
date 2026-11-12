package com.ensaa.hotelino.entity;

public class Chambre {
    public int id;
    public int numero;
    public int capacite;
    public double prix;
    public StatutChambre statut;

    public enum StatutChambre {
        DISPONIBLE, RESERVEE
    }
}
