package MezuTaula;

import helper.db.MySQLdb;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;


public class LoginServlet extends HttpServlet {
    private String message;

    private MySQLdb mySQLdb;

    public void init() {
        System.out.println("---> LoginServlet ---> init() metodoan sartzen");
        mySQLdb = new MySQLdb();
        System.out.println("---> LoginServlet ---> init() metodotik irtetzen");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("---> Login servlet-ean sartzen...");
        response.setHeader("Cache-Control", "no-cache");


        String email = request.getParameter("email");
        String pasahitza = request.getParameter("password");
        System.out.println("---> LoginServlet ---> Email: " + email);
        System.out.println("---> LoginServlet ---> Pasahitza: " + pasahitza);

        if (email != null || pasahitza != null) {

            String username = mySQLdb.getUsername(email, pasahitza);
            System.out.println("\tRetrieved data from db: " + username);

            // vacio y null NO es lo mismo
            if(username != null){

                System.out.println("---> LoginServlet --> Login OK");
                HttpSession session = request.getSession(true); // 'true' --> saioa sortuta ez badago, saioa sortu
                String sessionID = session.getId();
                System.out.println("\tUser session for " + username + ": " + sessionID);
                session.setAttribute("username", username); //saioari lotutako atributua

                //Erabiltzaile aktibo zerrenda eguneratuko dugu
                System.out.println("\tGetting loggedin userlist from servlet context: ");
                ServletContext context = request.getServletContext(); //testuingurua
                HashMap<String, String> loggedinUsers = (HashMap) context.getAttribute("loggedin_users");

                if (loggedinUsers == null){
                    System.out.println("loggedinUsers is empty");
                    loggedinUsers = new HashMap<>();
                    loggedinUsers.put(username, sessionID);
                }
                else { //zerrenda existitzen da (web aplikazioan erabiltzaileak daude jada)
                    if (!loggedinUsers.containsKey(username)){
                        System.out.println(username + " is not in the list");
                        loggedinUsers.put(username, sessionID);
                    }
                    else {
                        System.out.println(username + " is already in the list");
                    }
                }

                //zerrenda twstuinguran gehitu atributu bezala
                context.setAttribute("loggedin_users", loggedinUsers);
                System.out.println("\tLoggedin users: " + loggedinUsers.toString());

                System.out.println("---> LoginServlet --> redirecting to MainServlet");
                // ServletContext context = request.getServletContext();
                RequestDispatcher rd = context.getNamedDispatcher("MainServlet"); // web.xml fitxategiko <servlet-name>
                rd.forward(request, response);

                /*
                response.setContentType("text/plain");
                PrintWriter erantzunaren_edukia = response.getWriter();
                erantzunaren_edukia.println("Login OK");
                */

            }
            else { //erabiltzailea eta pasahitza okerrak dira
                //message="Login ERROR";
                //request.setAttribute("katea", message);
                System.out.println("---> LoginServlet ---> /jsp/login_form.jsp");
                boolean login_error = true;
                request.setAttribute("login_error", login_error);
                RequestDispatcher rd = request.getRequestDispatcher("/jsp/login_Form.jsp");
                rd.forward(request, response);
            }
        }
        else if (request.getSession(false) != null) { // saioa existitzen da
            System.out.println("---> LoginServlet ---> /jsp/login_form.jsp");
            ServletContext context = request.getServletContext();
            RequestDispatcher rd = context.getNamedDispatcher("MainServlet");
            rd.forward(request, response);
        }
        else{ // servlet-a zuzenean, parametrorik gabe deitzen da eta ez da saiorik existitzen
            System.out.println("---> LoginServlet ---> /jsp/login_form.jsp");
            RequestDispatcher rd = request.getRequestDispatcher("/jsp/login_form.jsp");
            rd.forward(request, response);
        }
        System.out.println("---> Login servlet-etik irtetzen...");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void destroy() {
    }
}