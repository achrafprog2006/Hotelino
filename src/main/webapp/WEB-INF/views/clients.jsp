<%@ page contentType="text/html;charset=UTF-8" import="java.util.List,com.ensaa.hotelino.entity.Client" %>
<%@ page import="com.ensaa.hotelino.util.Html" %>
<%
    List<Client> clients = (List<Client>) request.getAttribute("clients");
    String erreur = (String) request.getAttribute("erreur");
    String ctx = request.getContextPath();
%>
<html>
<head><title>Clients</title></head>
<body>
<h1>Clients</h1>
<p><a href="<%= ctx %>/">Accueil</a> | <a href="<%= ctx %>/clients?action=nouveau">Nouveau client</a></p>

<% if (erreur != null) { %><p style="color:red"><%= Html.esc(erreur) %></p><% } %>

<table border="1" cellpadding="5" cellspacing="0">
    <tr><th>ID</th><th>Nom</th><th>Prénom</th><th>CIN</th><th>Téléphone</th><th>Email</th><th>Actions</th></tr>
    <% for (Client c : clients) { %>
    <tr>
        <td><%= c.getId() %></td>
        <td><%= Html.esc(c.getNom()) %></td>
        <td><%= Html.esc(c.getPrenom()) %></td>
        <td><%= Html.esc(c.getCin()) %></td>
        <td><%= Html.esc(c.getTelephone()) %></td>
        <td><%= Html.esc(c.getEmail()) %></td>
        <td>
            <a href="<%= ctx %>/clients?action=modifier&id=<%= c.getId() %>">Modifier</a>
            <form method="post" action="<%= ctx %>/clients" style="display:inline"
                  onsubmit="return confirm('Supprimer ce client ?');">
                <input type="hidden" name="action" value="supprimer">
                <input type="hidden" name="id" value="<%= c.getId() %>">
                <button type="submit">Supprimer</button>
            </form>
        </td>
    </tr>
    <% } %>
    <% if (clients.isEmpty()) { %><tr><td colspan="7">Aucun client.</td></tr><% } %>
</table>
</body>
</html>
