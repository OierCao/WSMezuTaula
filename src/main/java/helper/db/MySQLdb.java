package helper.db;

import helper.info.MessageInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MySQLdb {
    private String url = "jdbc:mysql://localhost:3306/";
    private String user = "root";
    private String passwd = "root";
    private String driver = "com.mysql.cj.jdbc.Driver";

    private Connection conn;

    public MySQLdb() {
        try {
            Class.forName(this.driver).newInstance();
            this.conn = DriverManager.getConnection(this.url, this.user, this.passwd);
            System.out.println("---> MySQLdb ---> Connected to DB!!!");
        } catch(Exception e) {
            System.out.println("---> MySQLdb ---> Exception: " + e.getMessage());
        }
    }

    public void setUserInfo(String email, String password, String username) {
        String query = "INSERT INTO mezutaula.users VALUES ('" + email + "', '" + password + "', '" + username + "');";
        System.out.println("---> MySQLdb ---> DB query: " + query);

        try {
            Statement st = this.conn.createStatement();
            st.executeUpdate(query);
            System.out.println("---> MySQLdb ---> Query successful!!!");
        } catch(Exception e) {
            System.out.println("---> MySQLdb ---> Exception: " + e.getMessage());
        }
    }

    public String getUsername(String email, String password) {
        String query = "SELECT username FROM mezutaula.users WHERE email='" + email + "' AND password='" + password + "';";
        System.out.println("---> MySQLdb ---> DB query: " + query);
        String username = null;

        try {
            Statement st = this.conn.createStatement();
            ResultSet res = st.executeQuery(query);
            while(res.next()) {
                username = res.getString("username");
            }
            System.out.println("---> MySQLdb ---> Query successful!!!");
        } catch(Exception e) {
            System.out.println("---> MySQLdb ---> Exception: " + e.getMessage());
        }

        return username;
    }

    public void setMessageInfo(String message, String username) {
        String query = "INSERT INTO mezutaula.messages VALUES ('0', '" + message + "', '" + username + "');";
        System.out.println("---> MySQLdb ---> DB query: " + query);

        try {
            Statement st = this.conn.createStatement();
            st.executeUpdate(query);
            System.out.println("---> MySQLdb ---> Query successful!!!");
        } catch(Exception e) {
            System.out.println("---> MySQLdb ---> Exception: " + e.getMessage());
        }
    }

    public ArrayList<MessageInfo> getAllMessages() {
        String query = "SELECT * FROM mezutaula.messages;";
        System.out.println("---> MySQLdb ---> DB query: " + query);
        ArrayList<MessageInfo> messageInfoList = new ArrayList<MessageInfo>();

        try {
            Statement st = this.conn.createStatement();
            ResultSet res = st.executeQuery(query);
            while(res.next()) {
                messageInfoList.add(new MessageInfo(res.getString("message"), res.getString("username")));
            }
            System.out.println("---> MySQLdb ---> Query successful!!!");
        } catch(Exception e) {
            System.out.println("---> MySQLdb ---> Exception: " + e.getMessage());
        }

        return messageInfoList;
    }
}
