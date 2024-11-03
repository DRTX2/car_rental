package dao;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {

    private static String url="jdbc:mariadb:://localhost:3307/car_rental";
    private static String user="root";
    private static String password="root";
    private static Connection connection;
    
    private DB(){
    }
    
    public static Connection getDB(){
        if(connection==null){
            try{
                connection=DriverManager.getConnection(url, user, password);
            }catch(Exception e){
                System.out.println("Error while trying to connect to the database");
            }
        }
        return connection;
    }
    
    public static boolean closeConection(){
        if(connection==null) return false;
        try {
            connection.close();
            connection=null;
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
