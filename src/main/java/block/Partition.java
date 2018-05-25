package block;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Partition {

    private Integer id;
    private File session;
    private File recommendation;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;

    public Partition(Integer id){
        this.id = id;
        this.session = new File("/home/gregorio/Dropbox/Ufop/Monografia 1/DataBase/partition"+id+"/sessions.txt");

        this.recommendation = new File("/home/gregorio/Dropbox/Ufop/Monografia 1/DataBase/partition"+id+"/recommendation.txt");

        try {
            this.fileWriter = new FileWriter(this.session,true);
            //this.bufferedWriter = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




    public File getSession() {
        return session;
    }

    public void setSession(ArrayList<String> logs) throws IOException {
        try {
            //this.fileWriter = new FileWriter(this.session,true);

            //this.bufferedWriter = new BufferedWriter(fileWriter);

            for(int i = 0; i < logs.size(); i++)
                this.fileWriter.write(logs.get(i)+"\n");
            // bufferedWriter.append(logs+"\n");
            //fileWriter.close();
            //bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
//        } finally {
//            if(fileWriter != null){
//                fileWriter.close();
//            }
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

    public void setNotification(String id, Double timePublication){

        try {
            //this.fileWriter = new FileWriter(this.session,true);
            this.fileWriter.write(id+";"+timePublication+"\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closed() {
        try {
            this.fileWriter.close();
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
