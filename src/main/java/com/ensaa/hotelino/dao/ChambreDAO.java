package com.ensaa.hotelino.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ensaa.hotelino.database.ConnexionBD;
import com.ensaa.hotelino.entity.Chambre;

public class ChambreDAO {

    private static final String SELECT =
            "SELECT id_chambre, numero, etage, type, capacite, prix_par_nuit, statut FROM chambre";

    public void ajouter(Chambre c) throws SQLException {
        String sql = "INSERT INTO chambre (numero, etage, type, capacite, prix_par_nuit, statut) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection cn = ConnexionBD.get();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNumero());
            ps.setInt(2, c.getEtage());
            ps.setInt(3, c.getType().ordinal());
            ps.setInt(4, c.getCapacite());
            ps.setDouble(5, c.getPrixParNuit());
            ps.setInt(6, c.getStatut().ordinal());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    c.setId(keys.getLong(1));
                }
            }
        }
    }

    public boolean modifier(Chambre c) throws SQLException {
        String sql = "UPDATE chambre SET numero = ?, etage = ?, type = ?, capacite = ?, "
                + "prix_par_nuit = ?, statut = ? WHERE id_chambre = ?";
        try (Connection cn = ConnexionBD.get();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, c.getNumero());
            ps.setInt(2, c.getEtage());
            ps.setInt(3, c.getType().ordinal());
            ps.setInt(4, c.getCapacite());
            ps.setDouble(5, c.getPrixParNuit());
            ps.setInt(6, c.getStatut().ordinal());
            ps.setLong(7, c.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean supprimer(Long id) throws SQLException {
        try (Connection cn = ConnexionBD.get();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM chambre WHERE id_chambre = ?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public Chambre trouverParId(Long id) throws SQLException {
        try (Connection cn = ConnexionBD.get();
             PreparedStatement ps = cn.prepareStatement(SELECT + " WHERE id_chambre = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapper(rs) : null;
            }
        }
    }

    public Chambre trouverParNumero(String numero) throws SQLException {
        try (Connection cn = ConnexionBD.get();
             PreparedStatement ps = cn.prepareStatement(SELECT + " WHERE numero = ?")) {
            ps.setString(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapper(rs) : null;
            }
        }
    }

    public List<Chambre> trouverTous() throws SQLException {
        List<Chambre> liste = new ArrayList<>();
        try (Connection cn = ConnexionBD.get();
             PreparedStatement ps = cn.prepareStatement(SELECT + " ORDER BY numero");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(mapper(rs));
            }
        }
        return liste;
    }

    public List<Chambre> trouverDisponibles(LocalDate debut, LocalDate fin) throws SQLException {
        String sql = "SELECT c.id_chambre, c.numero, c.etage, c.type, c.capacite, c.prix_par_nuit, c.statut "
                + "FROM chambre c WHERE c.statut <> 2 AND NOT EXISTS ("
                + "SELECT 1 FROM reservation r WHERE r.id_chambre = c.id_chambre "
                + "AND r.statut <> 2 AND ? < r.date_fin AND ? > r.date_debut) ORDER BY c.numero";
        List<Chambre> liste = new ArrayList<>();
        try (Connection cn = ConnexionBD.get();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(debut));
            ps.setDate(2, Date.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapper(rs));
                }
            }
        }
        return liste;
    }

    static Chambre mapper(ResultSet rs) throws SQLException {
        Chambre c = new Chambre();
        c.setId(rs.getLong("id_chambre"));
        c.setNumero(rs.getString("numero"));
        c.setEtage(rs.getInt("etage"));
        c.setType(Chambre.TypeChambre.values()[rs.getInt("type")]);
        c.setCapacite(rs.getInt("capacite"));
        c.setPrixParNuit(rs.getDouble("prix_par_nuit"));
        c.setStatut(Chambre.StatutChambre.values()[rs.getInt("statut")]);
        return c;
    }
}
