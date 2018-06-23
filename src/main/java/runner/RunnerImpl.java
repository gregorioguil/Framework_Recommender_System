package runner;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import recommend.Recommend;

class RunnerImpl extends Runner {

    private ArrayList<Recommend> recommends;
    private int numRecommendation;
    private  String pathDataBase;
    private int numberPartitions;


    public RunnerImpl(ArrayList<Recommend> recommends, int numRecommendation, String path, int numberPartitions){
        this.recommends = recommends;
        this.numRecommendation = numRecommendation;
        String[] data = path.split("/");
        this.pathDataBase = "";
        for(int i = 0; i < data.length -1; i++)
            this.pathDataBase += "/"+ data[i];
        this.pathDataBase += "/DataBase/";
        this.numberPartitions = numberPartitions;
    }

    public void run(){
        Recommend syst = null;
        int part = 0;
        File partition = new File(pathDataBase+"partition"+part+"/tasks.txt");
        int cont = 0;
        for(int i = 0; i < this.recommends.size(); i++){

            syst = this.recommends.get(i);
            syst.init(numRecommendation,partition,pathDataBase);
            //syst.getUserIten();
            System.out.println("Número de partições: "+this.numberPartitions);
            new File(pathDataBase+"systens/"+"system"+i+"/").mkdirs();
            File recommend = new File(pathDataBase+"systens/"+"system"+i+"/recommends.txt");
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(recommend);

                for(int j = 1; j < this.numberPartitions ; j ++){
                    partition = new File(pathDataBase+"partition"+j+"/tasks.txt");

                    //System.out.println("Partição "+j);
                    //File partition = new File(path+"partition"+numberPartitions+"/"+"sessions.txt");


                    FileReader fileReader;
                    BufferedReader bufferedReader;


                    fileReader = new FileReader(partition);
                    bufferedReader = new BufferedReader(fileReader);
                    String line = bufferedReader.readLine();
                    

                    while(line != null) {
                        String[] args = line.split(";");

                        if (args.length < 4) {
                            //System.out.println("Não existe na base.");
                            line = bufferedReader.readLine();
                            continue;
                        }
                        Long idSession = Long.parseLong(args[1]);
                        Long idArticle = Long.parseLong(args[2]);


                        long init_time = System.currentTimeMillis();
                        List<String> output = syst.run(line);
                        long end_time = System.currentTimeMillis();
                        end_time = end_time - init_time;
                        line = bufferedReader.readLine();
//                        for(int k = 0; k < output.size(); k++)
//                        	System.out.println(k+" "+output.get(k));
                        //System.in.read();
                        fileWriter.write(idSession + ";" + output + ";" + end_time + "\n");
//                        if(idSession == 1422){
//                            System.out.println(idSession + ";"+idArticle+";"+ output + ";" + end_time);
//                            System.in.read();
//                        }
                    }
                    bufferedReader.close();
                    
                }

            
            fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            syst.clean();
            syst = null;
       }
    }

    public void definedBase(double initTime, double unitTime, File logs, File data) throws IOException {
        //DataSplit dataSplit = new DataSplit(initTime,unitTime,logs,data);
        //dataSplit.insertData("/home/gregorio/Dropbox/Ufop/Iniciação Cientifica 2/DadosUnificados/data.csv");
        //dataSplit.run();
        //this.lastArticle = dataSplit.getlastArticle();
    }

    public void cleanBase(){
        try {
            FileUtils.deleteDirectory(new File("DataBase"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
