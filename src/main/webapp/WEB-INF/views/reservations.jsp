<%@ page contentType="text/html;charset=UTF-8" import="java.util.List,java.util.Map" %>
<%@ page import="com.ensaa.hotelino.entity.Client,com.ensaa.hotelino.entity.Chambre,com.ensaa.hotelino.entity.Reservation" %>
<%@ page import="com.ensaa.hotelino.util.Html" %>
<%
    List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
    Map<Long, Client> clients = (Map<Long, Client>) request.getAttribute("clientsParId");
    Map<Long, Chambre> chambres = (Map<Long, Chambre>) request.getAttribute("chambresParId");
    String erreur = (String) request.getAttribute("erreur");
    String ctx = request.getContextPath();
%>
<html>
<head><title>Réservations</title></head>
<body>
<h1>Réservations</h1>
<p><a href="<%= ctx %>/">Accueil</a> | <a href="<%= ctx %>/reservations?action=nouveau">Nouvelle réservation</a></p>

<% if (erreur != null) { %><p style="color:red"><%= Html.esc(erreur) %></p><% } %>

<table border="1" cellpadding="5" cellspacing="0">
    <tr>
        <th>ID</th><th>Client</th><th>Chambre</th><th>Début</th><th>Fin</th>
        <th>Pers.</th><th>Montant (DH)</th><th>Statut</th><th>Actions</th>
    </tr>
    <% for (Reservation r : reservations) {
           Client cl = clients.get(r.getClientId());
           Chambre ch = chambres.get(r.getChambreId());
    %>
    <tr>
        <td><%= r.getId() %></td>
        <td><%= cl == null ? "?" : Html.esc(cl.getNom() + " " + cl.getPrenom()) %></td>
        <td><%= ch == null ? "?" : Html.esc(ch.getNumero()) %></td>
        <td><%= r.getDateDebut() %></td>
        <td><%= r.getDateFin() %></td>
        <td><%= r.getNbPersonnes() %></td>
        <td><%= String.format("%.2f", r.getMontantTotal()) %></td>
        <td><%= r.getStatut() %></td>
        <td>
            <% if (r.getStatut() == Reservation.StatutReservation.EN_ATTENTE) { %>
            <form method="post" action="<%= ctx %>/reservations" style="display:inline">
                <input type="hidden" name="action" value="confirmer">
                <input type="hidden" name="id" value="<%= r.getId() %>">
                <button type="submit">Confirmer</button>
            </form>
            <% } %>
            <% if (r.getStatut() != Reservation.StatutReservation.ANNULEE) { %>
            <form method="post" action="<%= ctx %>/reservations" style="display:inline">
                <input type="hidden" name="action" value="annuler">
                <input type="hidden" name="id" value="<%= r.getId() %>">
                <button type="submit">Annuler</button>
            </form>
            <% } %>
            <form method="post" action="<%= ctx %>/reservations" style="display:inline"
                  onsubmit="return confirm('Supprimer cette réservation ?');">
                <input type="hidden" name="action" value="supprimer">
                <input type="hidden" name="id" value="<%= r.getId() %>">
                <button type="submit">Supprimer</button>
            </form>
        </td>
    </tr>
    <% } %>
    <% if (reservations.isEmpty()) { %><tr><td colspan="9">Aucune réservation.</td></tr><% } %>
</table>
</body>
</html>
