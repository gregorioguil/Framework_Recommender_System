package block;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Partition {

    private Integer id;
    private File session;
    private File recommendation;

    public Partition(Integer id){
        this.id = id;
        this.session = new File("DataBase/partition"+id+"/sessions.txt");

        this.recommendation = new File("DataBase/partition"+id+"/recommendation.txt");
    }




    public File getSession() {
        return session;
    }

    public void setSession(String logs) {
        try {
            FileWriter fileWriter = new FileWriter(this.session,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(logs+"\n");
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

    public void setNotification(String id,Long timePublication) {

        try {
            FileWriter fileWriter = new FileWriter(this.session,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(id+";"+timePublication+"\n");
            // bufferedWriter.append(logs+"\n");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
