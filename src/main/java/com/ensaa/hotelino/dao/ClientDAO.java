package com.ensaa.hotelino.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ensaa.hotelino.database.ConnexionBD;
import com.ensaa.hotelino.entity.Client;

public class ClientDAO {

    private static final String SELECT =
            "SELECT id_client, nom, prenom, cin, telephone, email FROM client";

    public void ajouter(Client c) throws SQLException {
        String sql = "INSERT INTO client (nom, prenom, cin, telephone, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection cn = ConnexionBD.get();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNom());
            ps.setString(2, c.getPrenom());
            ps.setString(3, c.getCin());
            ps.setString(4, c.getTelephone());
            ps.setString(5, c.getEmail());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    c.setId(keys.getLong(1));
                }
            }
        }
    }

    public boolean modifier(Client c) throws SQLException {
        String sql = "UPDATE client SET nom = ?, prenom = ?, cin = ?, telephone = ?, email = ? "
                + "WHERE id_client = ?";
        try (Connection cn = ConnexionBD.get();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, c.getNom());
            ps.setString(2, c.getPrenom());
            ps.setString(3, c.getCin());
            ps.setString(4, c.getTelephone());
            ps.setString(5, c.getEmail());
            ps.setLong(6, c.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean supprimer(Long id) throws SQLException {
        try (Connection cn = ConnexionBD.get();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM client WHERE id_client = ?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public Client trouverParId(Long id) throws SQLException {
        try (Connection cn = ConnexionBD.get();
             PreparedStatement ps = cn.prepareStatement(SELECT + " WHERE id_client = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapper(rs) : null;
            }
        }
    }

    public List<Client> trouverTous() throws SQLException {
        List<Client> liste = new ArrayList<>();
        try (Connection cn = ConnexionBD.get();
             PreparedStatement ps = cn.prepareStatement(SELECT + " ORDER BY nom, prenom");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(mapper(rs));
            }
        }
        return liste;
    }

    private Client mapper(ResultSet rs) throws SQLException {
        Client c = new Client();
        c.setId(rs.getLong("id_client"));
        c.setNom(rs.getString("nom"));
        c.setPrenom(rs.getString("prenom"));
        c.setCin(rs.getString("cin"));
        c.setTelephone(rs.getString("telephone"));
        c.setEmail(rs.getString("email"));
        return c;
    }
}
