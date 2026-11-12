package com.ensaa.hotelino.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ensaa.hotelino.database.ConnexionBD;
import com.ensaa.hotelino.entity.Chambre;
import com.ensaa.hotelino.entity.Client;
import com.ensaa.hotelino.entity.Reservation;

public class ReservationDAO {

    private static final String SELECT =
            "SELECT r.id_reservation, r.date_creation, r.date_debut, r.date_fin, r.nb_personnes, "
          + "r.statut AS r_statut, r.montant_total, "
          + "cl.id_client, cl.nom, cl.prenom, cl.cin, cl.telephone, cl.email, "
          + "ch.id_chambre, ch.numero, ch.etage, ch.type, ch.capacite, ch.prix_par_nuit, "
          + "ch.statut AS ch_statut "
          + "FROM reservation r "
          + "JOIN client cl ON cl.id_client = r.id_client "
          + "JOIN chambre ch ON ch.id_chambre = r.id_chambre";

    public void ajouter(Reservation r) throws SQLException {
        String sql = "INSERT INTO reservation (id_client, id_chambre, date_creation, date_debut, date_fin, "
                + "nb_personnes, statut, montant_total) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection cn = ConnexionBD.get();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, r.getClientId());
            ps.setLong(2, r.getChambreId());
            ps.setDate(3, Date.valueOf(r.getDateCreation()));
            ps.setDate(4, Date.valueOf(r.getDateDebut()));
            ps.setDate(5, Date.valueOf(r.getDateFin()));
            ps.setInt(6, r.getNbPersonnes());
            ps.setInt(7, r.getStatut().ordinal());
            ps.setDouble(8, r.getMontantTotal());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    r.setId(keys.getLong(1));
                }
            }
        }
    }

    public boolean modifier(Reservation r) throws SQLException {
        String sql = "UPDATE reservation SET id_client = ?, id_chambre = ?, date_creation = ?, "
                + "date_debut = ?, date_fin = ?, nb_personnes = ?, statut = ?, montant_total = ? "
                + "WHERE id_reservation = ?";
        try (Connection cn = ConnexionBD.get();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, r.getClientId());
            ps.setLong(2, r.getChambreId());
            ps.setDate(3, Date.valueOf(r.getDateCreation()));
            ps.setDate(4, Date.valueOf(r.getDateDebut()));
            ps.setDate(5, Date.valueOf(r.getDateFin()));
            ps.setInt(6, r.getNbPersonnes());
            ps.setInt(7, r.getStatut().ordinal());
            ps.setDouble(8, r.getMontantTotal());
            ps.setLong(9, r.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean supprimer(Long id) throws SQLException {
        try (Connection cn = ConnexionBD.get();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM reservation WHERE id_reservation = ?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public Reservation trouverParId(Long id) throws SQLException {
        try (Connection cn = ConnexionBD.get();
             PreparedStatement ps = cn.prepareStatement(SELECT + " WHERE r.id_reservation = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapper(rs) : null;
            }
        }
    }

    public List<Reservation> trouverToutes() throws SQLException {
        return lister(SELECT + " ORDER BY r.date_debut DESC", null);
    }

    public List<Reservation> trouverParClient(Long idClient) throws SQLException {
        return lister(SELECT + " WHERE r.id_client = ? ORDER BY r.date_debut DESC", idClient);
    }

    public List<Reservation> trouverParChambre(Long idChambre) throws SQLException {
        return lister(SELECT + " WHERE r.id_chambre = ? ORDER BY r.date_debut", idChambre);
    }

    private List<Reservation> lister(String sql, Long param) throws SQLException {
        List<Reservation> liste = new ArrayList<>();
        try (Connection cn = ConnexionBD.get();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            if (param != null) {
                ps.setLong(1, param);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapper(rs));
                }
            }
        }
        return liste;
    }

    private Reservation mapper(ResultSet rs) throws SQLException {
        Reservation r = new Reservation();
        r.setId(rs.getLong("id_reservation"));
        r.setDateCreation(rs.getDate("date_creation").toLocalDate());
        r.setDateDebut(rs.getDate("date_debut").toLocalDate());
        r.setDateFin(rs.getDate("date_fin").toLocalDate());
        r.setNbPersonnes(rs.getInt("nb_personnes"));
        r.setStatut(Reservation.StatutReservation.values()[rs.getInt("r_statut")]);
        r.setMontantTotal(rs.getDouble("montant_total"));
        r.setClientId(rs.getLong("id_client"));
        r.setChambreId(rs.getLong("id_chambre"));
        return r;
    }
}
