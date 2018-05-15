package block;

import java.sql.*;

public class DataBase {
    private String name;
    private Connection connection;

    public DataBase(String name){
        this.connection = null;
        this.name = name;
    }

    public void connect() {
        try {
            //DriverManager.registerDriver(new Driver());
            Class.forName("org.postgresql.Driver");
            //Class.forName("org.postgres.Driver");
            this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+this.name,"postgres", "");



        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public void createTableArticles() {
        Statement statement = null;
        try {
            statement = this.connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS article " +
                    "(article_id INT PRIMARY KEY NOT NULL," +
                    "publication TEXT NOT NULL," +
                    "wordtitle TEXT[][]," +
                    "wordtext TEXT[][])";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertArticle(String line) {
        Statement statement = null;
        String[] argsArticle = line.split(";");
        String values = "("+Integer.parseInt(argsArticle[0])+","+argsArticle[2]+")";
        String[] title = {argsArticle[18],argsArticle[19],argsArticle[20]};
        String[] text = new String[30];

        int j = 0;
        for(int i = 21; i < argsArticle.length; i++){
            text[j] = argsArticle[i];
        }
        String sql = "INSERT INTO article VALUES"+values;
        try {
//            statement = this.connection.createStatement();
//            statement.executeUpdate(sql);
            sql = "INSERT INTO article VALUES("+Integer.parseInt(argsArticle[0])+","+argsArticle[2]+",?, ?)";
            Array arrayTitle = connection.createArrayOf("text",title);
            Array arrayText = connection.createArrayOf("text",text);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,"wordtitle");
            preparedStatement.setArray(2,arrayTitle);
            preparedStatement.executeUpdate();
            preparedStatement.setString(1,"wordtext");
            preparedStatement.setArray(2,arrayText);
            preparedStatement.executeUpdate();
//            statement.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
