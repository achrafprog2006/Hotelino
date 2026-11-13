<%@ page contentType="text/html;charset=UTF-8" import="java.util.List" %>
<%@ page import="com.ensaa.hotelino.entity.Client,com.ensaa.hotelino.entity.Chambre" %>
<%@ page import="com.ensaa.hotelino.util.Html" %>
<%
    List<Client> clients = (List<Client>) request.getAttribute("clients");
    List<Chambre> chambres = (List<Chambre>) request.getAttribute("chambres");
    String erreur = (String) request.getAttribute("erreur");
    String ctx = request.getContextPath();
    String pClient = request.getParameter("clientId");
    String pChambre = request.getParameter("chambreId");
%>
<html>
<head><title>Nouvelle réservation</title></head>
<body>
<h1>Nouvelle réservation</h1>
<p><a href="<%= ctx %>/reservations">Retour à la liste</a></p>

<% if (erreur != null) { %><p style="color:red"><%= Html.esc(erreur) %></p><% } %>

<form method="post" action="<%= ctx %>/reservations">
    <input type="hidden" name="action" value="ajouter">
    <p>Client :
        <select name="clientId" required>
            <% for (Client c : clients) { %>
            <option value="<%= c.getId() %>" <%= String.valueOf(c.getId()).equals(pClient) ? "selected" : "" %>>
                <%= Html.esc(c.getNom() + " " + c.getPrenom() + " (" + c.getCin() + ")") %>
            </option>
            <% } %>
        </select>
        <a href="<%= ctx %>/clients?action=nouveau">+ client</a>
    </p>
    <p>Chambre :
        <select name="chambreId" required>
            <% for (Chambre c : chambres) { %>
            <option value="<%= c.getId() %>" <%= String.valueOf(c.getId()).equals(pChambre) ? "selected" : "" %>>
                <%= Html.esc(c.getNumero() + " - " + c.getType() + " - " + c.getCapacite() + " pers. - "
                        + c.getPrixParNuit() + " DH/nuit - " + c.getStatut()) %>
            </option>
            <% } %>
        </select>
    </p>
    <p>Date début : <input type="date" name="dateDebut" value="<%= Html.esc(request.getParameter("dateDebut")) %>" required></p>
    <p>Date fin : <input type="date" name="dateFin" value="<%= Html.esc(request.getParameter("dateFin")) %>" required></p>
    <p>Nombre de personnes :
        <input type="number" name="nbPersonnes" min="1"
               value="<%= request.getParameter("nbPersonnes") == null ? "1" : Html.esc(request.getParameter("nbPersonnes")) %>" required>
    </p>
    <button type="submit">Réserver</button>
</form>
</body>
</html>
