<%--
  Created by IntelliJ IDEA.
  User: cvzcaoio
  Date: 2022-03-17
  Time: 9:23 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>

<%@ page import="java.util.*,helper.info.*" %>

<%  ArrayList<MessageInfo> messageList = (ArrayList) request.getAttribute("messageList");

    String username = (String) session.getAttribute("username");
    ServletContext context = request.getServletContext();
    HashMap<String, String> loggedinUsers = (HashMap) context.getAttribute("loggedin_users");

%>

<html>

<head>
    <title>View Messages</title>
    <link href="/MezuTaula/css/styleSheet.css" rel="stylesheet" />
</head>

<body>

<header>
    <h1>A webapp to share short messages</h1>
    <h3>View Messages</h3>
</header>

<section>
    <font color="white"> Active user:</font>
    <%= username %>
</section>

<section>
    <font color="white"> Active user:</font>
    <% for(Map.Entry<String, String> entry: loggedinUsers.entrySet()){ %>
        <%= entry.getKey() %>
    <% } %>

</section>

<section>
    <form method="POST" action="/MezuTaula/servlet/MainServlet">
        <table>
            <tr>
                <td>Message</td>
                <!-- Garrantzitzuena hemen name da, ez id -->
                <td><textarea id="message" name="message" cols="25" rows="3"></textarea></td>
            </tr>
        </table>
        <button>Send</button>
    </form>
</section>

<section>
    <table id="MezuTaula">
        <tr>
            <th>Username</th>
            <th>Message</th>
        </tr>
        <% for(int i = 0; i < messageList.size(); i++) {
            MessageInfo messageInfo = messageList.get(i); %>
        <tr>
            <td><%= messageInfo.getUsername() %></td>
            <td><%= messageInfo.getMessage() %></td>
        </tr>
        <% } %>
    </table>
</section>

<footer>
    Server Date: <%=new Date().toString()%>
    <br/>
    <script src="/MezuTaula/js/clientDate.js"></script>
</footer>

</body>

</html>