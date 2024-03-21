<%--
  Created by IntelliJ IDEA.
  User: LENOVO
  Date: 14/03/2024
  Time: 8:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.*,helper.info.*" %>

<%@ page session="false" %>

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

    <% Object login_error_aux = request.getAttribute("login_error");
        if (login_error_aux != null) {
            if ((boolean) login_error_aux) { %>
                <h3>LOGIN ERROR!!!</h3>
        <% }
        } %>

    <section>
        <form method="POST" action="/MezuTaula/servlet/LoginServlet">
            <table>
                <tr>
                    <td>Email:</td>
                    <td><input name="email"/></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><input type="password" name="password"/></td>
                </tr>
            </table>
            <button>Send</button>
        </form>
    </section>


    <footer>
        Server Date: <%=new Date().toString()%>
        <br/>
        <script src="/MezuTaula/js/clientDate.js"></script>
    </footer>

</body>

</html>
