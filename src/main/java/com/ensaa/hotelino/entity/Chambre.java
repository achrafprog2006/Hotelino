package com.ensaa.hotelino.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Chambre {

    public enum TypeChambre {
        SIMPLE,
        DOUBLE,
        SUITE
    }

    public enum StatutChambre {
        DISPONIBLE,
        OCCUPEE,
        MAINTENANCE
    }

    private Long id;
    private String numero;
    private int etage;
    private TypeChambre type;
    private int capacite;
    private double prixParNuit;
    private StatutChambre statut;
    private List<Equipement> equipements = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    public Chambre() {
    }

    public Chambre(String numero, int etage, TypeChambre type, int capacite, double prixParNuit, StatutChambre statut) {
        this.numero = numero;
        this.etage = etage;
        this.type = type;
        this.capacite = capacite;
        this.prixParNuit = prixParNuit;
        this.statut = statut;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getEtage() {
        return etage;
    }

    public void setEtage(int etage) {
        this.etage = etage;
    }

    public TypeChambre getType() {
        return type;
    }

    public void setType(TypeChambre type) {
        this.type = type;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public double getPrixParNuit() {
        return prixParNuit;
    }

    public void setPrixParNuit(double prixParNuit) {
        this.prixParNuit = prixParNuit;
    }

    public StatutChambre getStatut() {
        return statut;
    }

    public void setStatut(StatutChambre statut) {
        this.statut = statut;
    }

    public List<Equipement> getEquipements() {
        return equipements;
    }

    public void setEquipements(List<Equipement> equipements) {
        this.equipements = equipements;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public boolean estDisponible(LocalDate debut, LocalDate fin) {
        if (statut == StatutChambre.MAINTENANCE) {
            return false;
        }
        for (Reservation r : reservations) {
            if (r.getStatut() == Reservation.StatutReservation.ANNULEE) {
                continue;
            }
            boolean chevauche = debut.isBefore(r.getDateFin()) && fin.isAfter(r.getDateDebut());
            if (chevauche) {
                return false;
            }
        }
        return true;
    }
}
