package com.ensaa.hotelino.entity;

import java.util.Date;

public class Reservation {
    public int id;
    public int idClient;
    public int idChambre;
    public Date dateArrivee;
    public Date dateDepart;
    public String statut;
    public double montant;
    public String clientNom;
    public String chambreNumero;
}
