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
    <script>
        function GetTimeIO(){
            //ondorengo objetua erabiliz, JS kodea HTTP eskaerak egin ditzake (azterketan sartu)
            var request = new XMLHttpRequest(); //AJAX

            // http://www.w3schools.com/xml/ajax_xmlhttprequest_response.asp
            request.onreadystatechange = function() {
                if (request.readyState == 4){ // 4: request finished and response is ready
                    if (request.status == 200) {
                        var json = request.responseText; // HTTP erantunetik JSON katea atera
                        var jsonObj = JSON.parse(json); // JSON katea hiztegi moduko objetu bihurtu

                        var taula = document.getElementById("MezuTaula"); //Taula elementua atera

                        var errenkadak = taula.getElementsByTagName('tr');
                        var errenkdaCount = errenkadak.length;
                        for (var i=1; i<errenkdaCount; i++){
                            errenkadak[1].remove();
                        }

                        // taula berreraiki
                        errenkdaCount = jsonObj.length;
                        for (var i=0; i<errenkdaCount; i++){
                            var errenkada = document.createElement('tr');

                            var username = jsonObj[i].username;
                            var gelaxka_username = document.createElement('td');
                            gelaxka_username.innerHTML = username;
                            errenkada.appendChild(gelaxka_username);

                            var message = jsonObj[i].message;
                            var gelaxka_message = document.createElement('td');
                            gelaxka_message.innerHTML = message;
                            errenkada.appendChild(gelaxka_message);

                            taula.appendChild(errenkada);
                        }
                    }
                }
            }

            request.open("GET", "/MezuTaula/servlet/MainServlet?format=json", true); //true-->async
            request.send(null); //eskaera bidali

            setTimeout("GetTimeIO()", 5000);
        }
        setTimeout("GetTimeIO()", 5000); //5s barru GetTimeIO funtzioa deitu
    </script>
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