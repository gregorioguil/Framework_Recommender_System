package main.java.block;

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
        this.articles = new File("BaseOfData/partition"+id+"/articles.txt");
        this.recommendation = new File("BaseOfData/partition"+id+"/recommendation.txt");
    }

    public File getArticles() {
        return articles;
    }

    public void setArticles(File articles) {
        this.articles = articles;
    }

    public File getSession() {
        return session;
    }

    public void setSession(File session) {
        this.session = session;
    }

    public File getRecommendation() {
        return recommendation;
    }

    public void setRecomendation(File recommendation) {
        this.recommendation = recommendation;
    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void updatePartition(String logs){
        try {
            FileWriter fileWriter = new FileWriter(this.session,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            System.out.println("Escreveu");
            bufferedWriter.write(logs+"\n");
           // bufferedWriter.append(logs+"\n");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
