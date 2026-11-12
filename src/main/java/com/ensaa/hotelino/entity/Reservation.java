package com.ensaa.hotelino.entity;

import java.time.LocalDate;

public class Reservation {
    public int id;
    public int idClient;
    public int idChambre;
    public LocalDate dateArrivee;
    public LocalDate dateDepart;
    public double montant;
    public String clientNom;
    public String chambreNumero;
    public StatutReservation statut;

    public enum StatutReservation {
        CONFIRMEE, EN_COURS, TERMINEE, ANNULEE
    }
}
