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
                    "wordtitle TEXT[]," +
                    "wordtext TEXT[]," +
                    "existe BOOLEAN)";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertArticle(String line) {
        Statement statement = null;
        String[] argsArticle = line.split(";");
        String values = "("+Integer.parseInt(argsArticle[0])+","+argsArticle[2];
        String[] title = {argsArticle[18],argsArticle[19],argsArticle[20]};
        String[] text = new String[30];
        boolean exist = false;
        if(Long.parseLong(argsArticle[2]) < 0)
            exist = true;

        int j = 0;
        for(int i = 21; i < argsArticle.length; i++){
            text[j] = argsArticle[i];
            j++;
        }

        try {
            statement = this.connection.createStatement();
            Array arrayTitle = connection.createArrayOf("text",title);
            Array arrayText = connection.createArrayOf("text",text);
            values += ",'"+arrayTitle+"','"+arrayText+"',"+exist+")";
            String sql = "INSERT INTO article VALUES "+values+" ON CONFLICT (article_id) DO NOTHING";
            //System.out.println(sql);

            statement.executeUpdate(sql);

            statement.close();
//            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Long getTimePublicationArticle(String s) {
        Statement statement = null;
        ResultSet resultSet = null;
        Long publication = null;
        try {
            String query = "Select publication from article where article_id = "+s;
            System.out.println(query);
            statement = this.connection.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                publication = Long.parseLong(resultSet.getString(1));
                System.out.println("Publication: "+publication);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publication;
    }

    public boolean verifyExists(String s) {
        Statement statement = null;
        ResultSet resultSet = null;
        boolean exist = false;
        try {
            String query = "Select * from article where article_id = "+Integer.parseInt(s);

            statement = this.connection.createStatement();
            resultSet = statement.executeQuery(query);
            exist = resultSet.getBoolean(4);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exist;
    }
}
