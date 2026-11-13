<%@ page contentType="text/html;charset=UTF-8" import="com.ensaa.hotelino.entity.Client" %>
<%@ page import="com.ensaa.hotelino.util.Html" %>
<%
    Client c = (Client) request.getAttribute("client");
    String erreur = (String) request.getAttribute("erreur");
    String ctx = request.getContextPath();
    boolean edition = c.getId() != null;
%>
<html>
<head><title><%= edition ? "Modifier" : "Nouveau" %> client</title></head>
<body>
<h1><%= edition ? "Modifier" : "Nouveau" %> client</h1>
<p><a href="<%= ctx %>/clients">Retour à la liste</a></p>

<% if (erreur != null) { %><p style="color:red"><%= Html.esc(erreur) %></p><% } %>

<form method="post" action="<%= ctx %>/clients">
    <input type="hidden" name="action" value="<%= edition ? "modifier" : "ajouter" %>">
    <% if (edition) { %><input type="hidden" name="id" value="<%= c.getId() %>"><% } %>
    <p>Nom : <input name="nom" value="<%= Html.esc(c.getNom()) %>" required></p>
    <p>Prénom : <input name="prenom" value="<%= Html.esc(c.getPrenom()) %>" required></p>
    <p>CIN : <input name="cin" value="<%= Html.esc(c.getCin()) %>" required></p>
    <p>Téléphone : <input name="telephone" value="<%= Html.esc(c.getTelephone()) %>"></p>
    <p>Email : <input type="email" name="email" value="<%= Html.esc(c.getEmail()) %>"></p>
    <button type="submit">Enregistrer</button>
</form>
</body>
</html>
