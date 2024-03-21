package MezuTaula;

import com.google.gson.Gson;
import helper.db.MySQLdb;
import helper.info.MessageInfo;

import javax.print.attribute.standard.MediaSize;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class MainServlet extends HttpServlet {
    private String message;
    private MySQLdb mySQLdb;

    public void init() {
        System.out.println("---> MainServlet ---> init() metodoan sartzen");
        mySQLdb = new MySQLdb();
        System.out.println("---> MainServlet ---> init() metodotik irtetzen");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("---> MainServlet ---> doGet() metodoan sartzen...");
        response.setHeader("Cache-Control", "no-cache");

        String logout = request.getParameter("logout");
        System.out.print("---> MainServlet ---> Logout: " + logout);

        if (logout != null) { // erabiltzaileak saioa itxi nahi du
            if(logout.equals("true")) {
                System.out.println("---> MainServlet --> User log out: redirecting to login form");
                HttpSession session = request.getSession(false);
                session.invalidate(); // saioa amaitu

                boolean log_out = true;
                request.setAttribute("log_out", log_out);
                RequestDispatcher rd = request.getRequestDispatcher("/jsp/login_form.jsp");
                rd.forward(request, response);
            }
        } else {
            HttpSession session = request.getSession();
            if (request.getSession(false) != null) {
                System.out.println("---> MainServlet ---> User is logged: redirecting to welcome.jsp");

                String format = request.getParameter("format");
                if (format != null){ // HTTP erantzunaren edukia json
                    System.out.println("\tConverting ArrayList<MessageInfo> to json");
                    ArrayList<MessageInfo> messageList = mySQLdb.getAllMessages();
                    Gson gson = new Gson();
                    String messageList_json = gson.toJson(messageList);
                    System.out.println("\tMessageList_json: " + messageList_json);

                    response.setContentType("application/json");
                    PrintWriter http_out = response.getWriter();
                    http_out.println(messageList_json);
                }
                else { // HTTP erantzunaren edukia JSParen konpilazioak buelatatutako HTMLa
                    //mezuen zerrenda atera eta eskaeran atributu bezala erantsi
                    //gero JSP-ak atributu hori eskaeratik atera dezan

                    String message = request.getParameter("message");
                    if (message != null){
                        String username = (String) session.getAttribute("username");
                        mySQLdb.setMessageInfo(message, username);
                    }

                    ArrayList<MessageInfo> messageList = mySQLdb.getAllMessages();
                    request.setAttribute("messageList", messageList);

                    RequestDispatcher rd = request.getRequestDispatcher("/jsp/viewMessages.jsp");
                    rd.forward(request, response);
                }
            } else {
                System.out.println("---> MainServlet ---> User is not logged in: redirecting to login_form.jsp");
                RequestDispatcher rd = request.getRequestDispatcher("/jsp/login_form.jsp");
                rd.forward(request, response);
            }
        }
        System.out.print("---> MainServlet ---> doGet() metodotik irtetzen");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void destroy() {
    }
}