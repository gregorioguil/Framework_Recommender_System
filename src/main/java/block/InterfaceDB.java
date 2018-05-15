package block;

import java.sql.Connection;
import java.sql.DriverManager;
import org.postgresql.Driver;

public class InterfaceDB {
    private Connection connection;
    private String dbName;
    public InterfaceDB(String dbName){
        this.connection = null;
        this.dbName = dbName;
    }

    public void connect(){
        try {
            //DriverManager.registerDriver(new Driver());
            Class.forName("org.postgresql.Driver");
            //Class.forName("org.postgres.Driver");
            this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbName,"postgres", "");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }
}
