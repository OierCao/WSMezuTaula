package test;

import com.google.gson.Gson;
import helper.db.MySQLdb;
import helper.info.MessageInfo;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class TestServlet extends HttpServlet {
    private MySQLdb mySQLdb;

    public TestServlet() {
        super();

        mySQLdb = new MySQLdb();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("---> Entering doGet() TestServlet");

        // Erantzunaren edukian idazteko
        PrintWriter http_out = response.getWriter();

        response.setHeader("Cache-Control", "no-cache");

        // Eskaeratik type parametroa irakurri
        String type = request.getParameter("type");

        // http://localhost:8080/MezuTaula/servlet/TestServlet?type=...
        if(type != null) {

            // http://localhost:8080/MezuTaula/servlet/Test?type=registerUser&email=user@ehu.eus&password=1234&username=user
            if(type.equals("registerUser")) {
                System.out.println("\tregisterUser has been called");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String username = request.getParameter("username");
                if(email != null && password !=null && username!=null) {
                    System.out.println("\tExtracting request parameters: " + email + " " + password + " " + username);
                    mySQLdb.setUserInfo(email, password, username);
                    System.out.println("\tUpdating users table in the database");
                    http_out.println("Ekintza ondo burutu da!");
                } else {
                    http_out.println("registerUser egiteko parametroak ez dira ondo bidali!");
                }

            // http://localhost:8080/MezuTaula/servlet/Test?type=getUsername&email=user@ehu.eus&password=1234
            } else if(type.equals("getUsername")) {
                System.out.println("\tgetUsername has been called");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                if(email != null && password !=null) {
                    System.out.println("\tExtracting request parameters: " + email + " " + password);
                    String username = mySQLdb.getUsername(email, password);
                    System.out.println("\tRetrieved data from db: " + username);
                    http_out.println("Aplikazioan kautotu zara: " + username);
                } else {
                    http_out.println("getUsername egiteko parametroak ez dira ondo bidali!");
                }

            // http://localhost:8080/MezuTaula/servlet/Test?type=registerMessage&username=user&message=”Kaixo, Mundua!”
            } else if(type.equals("registerMessage")) {
                System.out.println("\tregisterMessage has been called");
                String username = request.getParameter("username");
                String message = request.getParameter("message");
                if(username != null && message !=null) {
                    System.out.println("\tExtracting request parameters: " + username + " " + message);
                    mySQLdb.setMessageInfo(message, username);
                    System.out.println("\tUpdating messages table in the database");
                    http_out.println("Ekintza ondo burutu da!");
                } else {
                    http_out.println("registerMessage egiteko parametroak ez dira ondo bidali!");
                }

            // http://localhost:8080/MezuTaula/servlet/TestServlet?type=getAllMessages
            } else if(type.equals("getAllMessages")) {
                System.out.println("\tgetAllMessages has been called");
                ArrayList<MessageInfo> messageList = mySQLdb.getAllMessages();
                String format = request.getParameter("format");
                if(format != null) {
                    if(format.equals("json")) {
                        System.out.println("\tConverting ArrayList<MessageInfo> to json");
                        Gson gson = new Gson();
                        String messageList_json = gson.toJson(messageList);
                        System.out.println("\tmessageList_json: " + messageList_json);
                        response.setContentType("application/json");
                        http_out.println(messageList_json);

                    } else if(format.equals("html")) {
                        System.out.println("\tRedirecting the user to viewMessages.jsp");
                        // datuak servlet-etik JSP-ra pasatzeko
                        request.setAttribute("messageList", messageList);
                        RequestDispatcher rd = request.getRequestDispatcher("/jsp/viewMessages.jsp");
                        rd.forward(request, response);

                    } else {
                        http_out.println("format parametroak ez du balio egokia!");
                    }

                } else {
                    http_out.println("getAllMessages egiteko parametroak ez dira ondo bidali!");
                }

            // http://localhost:8080/MezuTaula/servlet/TestServlet?type=qwerty
            } else {
                http_out.println("type parametroaren balioa ez da zuzena!");
            }

            // http://localhost:8080/MezuTaula/servlet/TestServlet
        }  else {
            http_out.println("Ez da 'type' parametrorik bidali!");
        }

        System.out.println("<--- Exiting doGet() TestServlet");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
