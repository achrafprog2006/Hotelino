package com.ensaa.hotelino.entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Reservation {

    public enum StatutReservation {
        EN_ATTENTE,
        CONFIRMEE,
        ANNULEE
    }

    private Long id;
    private LocalDate dateCreation;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private int nbPersonnes;
    private StatutReservation statut;
    private double montantTotal;
    private Client client;
    private Chambre chambre;
    private List<Paiement> paiements = new ArrayList<>();

    public Reservation() {
    }

    public Reservation(LocalDate dateCreation, LocalDate dateDebut, LocalDate dateFin, int nbPersonnes, StatutReservation statut, double montantTotal) {
        this.dateCreation = dateCreation;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.nbPersonnes = nbPersonnes;
        this.statut = statut;
        this.montantTotal = montantTotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public int getNbPersonnes() {
        return nbPersonnes;
    }

    public void setNbPersonnes(int nbPersonnes) {
        this.nbPersonnes = nbPersonnes;
    }

    public StatutReservation getStatut() {
        return statut;
    }

    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Chambre getChambre() {
        return chambre;
    }

    public void setChambre(Chambre chambre) {
        this.chambre = chambre;
    }

    public List<Paiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(List<Paiement> paiements) {
        this.paiements = paiements;
    }

    public double calculerMontant() {
        long nuits = ChronoUnit.DAYS.between(dateDebut, dateFin);
        montantTotal = nuits * chambre.getPrixParNuit();
        return montantTotal;
    }

    public void annuler() {
        this.statut = StatutReservation.ANNULEE;
    }
}
