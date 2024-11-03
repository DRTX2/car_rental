package views;
import java.sql.DriverManager;
import org.mariadb.jdbc.Connection;

public class DBConnection {
    private String url="jdbc:mariadb:://localhost:3307/car_rental";
    private String user="root";
    private String password="root";
    private Connection conection;
    
    public DBConnection(){
        try{
            conection=(Connection) DriverManager.getConnection(url, user, password);
        }catch(Exception e){
            System.out.println("Error."+ e.getMessage());
        }
    }
    
    public Connection getConnection(){
        return this.conection;
    }
    

}
