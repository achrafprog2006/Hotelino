package com.ensaa.hotelino.entity;

import java.time.LocalDate;

public class Paiement {

    public enum ModePaiement {
        ESPECES,
        CARTE,
        VIREMENT
    }

    private Long id;
    private double montant;
    private LocalDate datePaiement;
    private ModePaiement mode;
    private Reservation reservation;

    public Paiement() {
    }

    public Paiement(double montant, LocalDate datePaiement, ModePaiement mode) {
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.mode = mode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public ModePaiement getMode() {
        return mode;
    }

    public void setMode(ModePaiement mode) {
        this.mode = mode;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
