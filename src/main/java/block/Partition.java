package block;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Partition {

    private Integer id;
    private File articles;
    private File session;
    private File recommendation;

    public Partition(Integer id){
        this.id = id;
        this.session = new File("BaseOfData/partition"+id+"/sessions.txt");
        this.articles = new File("BaseOfData/articles/data.txt");
        this.recommendation = new File("BaseOfData/partition"+id+"/recommendation.txt");
    }

    public File getArticles() {
        return articles;
    }

    public void setArticles(String articles) {
        try {
            FileWriter fileWriter = new FileWriter(this.articles,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(articles+"\n");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getSession() {
        return session;
    }

    public void setSession(String logs) {
        try {
            System.out.println(logs);
            String[] args = logs.split(";");
            String out = args[0];
            for(int i = 2; i < args.length; i +=4){
                out += ";"+ args[i]+";"+args[i+1];
            }
            FileWriter fileWriter = new FileWriter(this.session,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(out+"\n");
            // bufferedWriter.append(logs+"\n");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        try {
            String[] args = recommendation.split(";");
            String out = args[0]+";";
            for(int i = 5; i < args.length; i += 4) {
                out += args[i] + ";";
                System.out.println(args[i]);
            }
            out  = out.substring(0,(out.length()-1));
            FileWriter fileWriter = new FileWriter(this.recommendation,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(out+"\n");
            // bufferedWriter.append(logs+"\n");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//    public void updatePartition(String logs){
//        try {
//            FileWriter fileWriter = new FileWriter(this.session,true);
//            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//            System.out.println("Escreveu");
//            bufferedWriter.write(logs+"\n");
//           // bufferedWriter.append(logs+"\n");
//            bufferedWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
