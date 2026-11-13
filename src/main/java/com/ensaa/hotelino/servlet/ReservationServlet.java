package com.ensaa.hotelino.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ensaa.hotelino.dao.ChambreDAO;
import com.ensaa.hotelino.dao.ClientDAO;
import com.ensaa.hotelino.dao.ReservationDAO;
import com.ensaa.hotelino.entity.Chambre;
import com.ensaa.hotelino.entity.Client;
import com.ensaa.hotelino.entity.Reservation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/reservations")
public class ReservationServlet extends HttpServlet {

    private static final String VUES = "/WEB-INF/views/";

    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final ClientDAO clientDAO = new ClientDAO();
    private final ChambreDAO chambreDAO = new ChambreDAO();

    // GET /reservations                -> liste
    // GET /reservations?action=nouveau -> formulaire de réservation
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            if ("nouveau".equals(req.getParameter("action"))) {
                afficherFormulaire(req, resp, null);
            } else {
                afficherListe(req, resp, null);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // POST /reservations  action = ajouter | confirmer | annuler | supprimer
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        try {
            if ("ajouter".equals(action)) {
                ajouter(req, resp);
                return;
            }

            Long id = Long.valueOf(req.getParameter("id"));
            if ("supprimer".equals(action)) {
                reservationDAO.supprimer(id);
            } else if ("confirmer".equals(action) || "annuler".equals(action)) {
                Reservation r = reservationDAO.trouverParId(id);
                if (r != null) {
                    if ("annuler".equals(action)) {
                        r.annuler();
                    } else {
                        r.setStatut(Reservation.StatutReservation.CONFIRMEE);
                    }
                    reservationDAO.modifier(r);
                }
            }
            resp.sendRedirect(req.getContextPath() + "/reservations");
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException(e);
        }
    }

    private void ajouter(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        LocalDate debut;
        LocalDate fin;
        long idClient;
        long idChambre;
        int nb;
        try {
            debut = LocalDate.parse(req.getParameter("dateDebut"));
            fin = LocalDate.parse(req.getParameter("dateFin"));
            idClient = Long.parseLong(req.getParameter("clientId"));
            idChambre = Long.parseLong(req.getParameter("chambreId"));
            nb = Integer.parseInt(req.getParameter("nbPersonnes"));
        } catch (DateTimeParseException | NumberFormatException | NullPointerException e) {
            afficherFormulaire(req, resp, "Veuillez remplir tous les champs correctement.");
            return;
        }

        if (!fin.isAfter(debut)) {
            afficherFormulaire(req, resp, "La date de fin doit être après la date de début.");
            return;
        }

        Chambre chambre = chambreDAO.trouverParId(idChambre);
        Client client = clientDAO.trouverParId(idClient);
        if (chambre == null || client == null) {
            afficherFormulaire(req, resp, "Client ou chambre introuvable.");
            return;
        }
        if (nb < 1 || nb > chambre.getCapacite()) {
            afficherFormulaire(req, resp, "Nombre de personnes invalide (capacité : " + chambre.getCapacite() + ").");
            return;
        }

        boolean libre = false;
        for (Chambre c : chambreDAO.trouverDisponibles(debut, fin)) {
            if (c.getId().equals(chambre.getId())) {
                libre = true;
                break;
            }
        }
        if (!libre) {
            afficherFormulaire(req, resp, "La chambre " + chambre.getNumero() + " n'est pas disponible à ces dates.");
            return;
        }

        long nuits = ChronoUnit.DAYS.between(debut, fin);
        Reservation r = new Reservation(LocalDate.now(), debut, fin, nb,
                Reservation.StatutReservation.EN_ATTENTE, nuits * chambre.getPrixParNuit());
        r.setClientId(idClient);
        r.setChambreId(idChambre);
        reservationDAO.ajouter(r);

        resp.sendRedirect(req.getContextPath() + "/reservations");
    }

    private void afficherListe(HttpServletRequest req, HttpServletResponse resp, String erreur)
            throws SQLException, ServletException, IOException {
        Map<Long, Client> clients = new HashMap<>();
        for (Client c : clientDAO.trouverTous()) {
            clients.put(c.getId(), c);
        }
        Map<Long, Chambre> chambres = new HashMap<>();
        for (Chambre c : chambreDAO.trouverTous()) {
            chambres.put(c.getId(), c);
        }
        List<Reservation> reservations = reservationDAO.trouverToutes();
        req.setAttribute("reservations", reservations);
        req.setAttribute("clientsParId", clients);
        req.setAttribute("chambresParId", chambres);
        req.setAttribute("erreur", erreur);
        req.getRequestDispatcher(VUES + "reservations.jsp").forward(req, resp);
    }

    private void afficherFormulaire(HttpServletRequest req, HttpServletResponse resp, String erreur)
            throws SQLException, ServletException, IOException {
        req.setAttribute("clients", clientDAO.trouverTous());
        req.setAttribute("chambres", chambreDAO.trouverTous());
        req.setAttribute("erreur", erreur);
        req.getRequestDispatcher(VUES + "reservation-form.jsp").forward(req, resp);
    }
}
