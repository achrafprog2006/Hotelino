package com.ensaa.hotelino.servlet;

import java.io.IOException;
import java.sql.SQLException;

import com.ensaa.hotelino.dao.ClientDAO;
import com.ensaa.hotelino.entity.Client;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/clients")
public class ClientServlet extends HttpServlet {

    private static final String VUES = "/WEB-INF/views/";

    private final ClientDAO dao = new ClientDAO();

    // GET /clients                     -> liste
    // GET /clients?action=nouveau      -> formulaire d'ajout
    // GET /clients?action=modifier&id= -> formulaire de modification
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        try {
            if ("nouveau".equals(action)) {
                req.setAttribute("client", new Client());
                req.getRequestDispatcher(VUES + "client-form.jsp").forward(req, resp);
            } else if ("modifier".equals(action)) {
                Client c = dao.trouverParId(Long.valueOf(req.getParameter("id")));
                if (c == null) {
                    resp.sendRedirect(req.getContextPath() + "/clients");
                    return;
                }
                req.setAttribute("client", c);
                req.getRequestDispatcher(VUES + "client-form.jsp").forward(req, resp);
            } else {
                req.setAttribute("clients", dao.trouverTous());
                req.getRequestDispatcher(VUES + "clients.jsp").forward(req, resp);
            }
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException(e);
        }
    }

    // POST /clients  action = ajouter | modifier | supprimer
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        try {
            if ("supprimer".equals(action)) {
                try {
                    dao.supprimer(Long.valueOf(req.getParameter("id")));
                } catch (SQLException e) {
                    // client lié à des réservations (clé étrangère)
                    req.setAttribute("erreur", "Suppression impossible : ce client a des réservations.");
                    req.setAttribute("clients", dao.trouverTous());
                    req.getRequestDispatcher(VUES + "clients.jsp").forward(req, resp);
                    return;
                }
                resp.sendRedirect(req.getContextPath() + "/clients");
                return;
            }

            Client c = new Client();
            c.setNom(trim(req.getParameter("nom")));
            c.setPrenom(trim(req.getParameter("prenom")));
            c.setCin(trim(req.getParameter("cin")));
            c.setTelephone(trim(req.getParameter("telephone")));
            c.setEmail(trim(req.getParameter("email")));
            String id = req.getParameter("id");
            if (id != null && !id.isEmpty()) {
                c.setId(Long.valueOf(id));
            }

            if (c.getNom().isEmpty() || c.getPrenom().isEmpty() || c.getCin().isEmpty()) {
                erreurFormulaire(req, resp, c, "Nom, prénom et CIN sont obligatoires.");
                return;
            }

            try {
                if ("modifier".equals(action)) {
                    dao.modifier(c);
                } else {
                    dao.ajouter(c);
                }
            } catch (SQLException e) {
                // le plus probable : CIN déjà existant (UNIQUE)
                erreurFormulaire(req, resp, c, "Enregistrement impossible (CIN déjà utilisé ?).");
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/clients");
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException(e);
        }
    }

    private void erreurFormulaire(HttpServletRequest req, HttpServletResponse resp, Client c, String msg)
            throws ServletException, IOException {
        req.setAttribute("erreur", msg);
        req.setAttribute("client", c);
        req.getRequestDispatcher(VUES + "client-form.jsp").forward(req, resp);
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
