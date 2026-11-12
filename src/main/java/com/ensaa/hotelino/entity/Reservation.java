package com.ensaa.hotelino.entity;

import java.time.LocalDate;

public class Reservation {
    public int id;
    public int idClient;
    public int idChambre;
    public double montant;
    public LocalDate dateArrivee;
    public LocalDate dateDepart;
    public StatutReservation statut;

    public enum StatutReservation {
        CONFIRMEE, EN_COURS, TERMINEE, ANNULEE
    }
}
