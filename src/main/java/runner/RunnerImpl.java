package runner;


import org.apache.commons.io.FileUtils;
import recommend.Recommend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

class RunnerImpl extends Runner {

    private ArrayList<Recommend> recommends;
    private int numRecommendation;
    private String lastArticle;
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
        File partition = new File(pathDataBase+"partition"+part+"/sessions.txt");
        int cont = 0;
        for(int i = 0; i < this.recommends.size(); i++){

            syst = this.recommends.get(i);
            syst.init(numRecommendation,partition,pathDataBase);
            //syst.getUserIten();
            System.out.println("Número de partições: "+this.numberPartitions);
            for(int j = 1; j < this.numberPartitions ; j ++){
            	partition = new File(pathDataBase+"partition"+1+"/sessions.txt");
                new File(pathDataBase+"systens/"+"system"+i+"/").mkdirs();
                System.out.println("Partição "+j);
                //File partition = new File(path+"partition"+numberPartitions+"/"+"sessions.txt");
                File recommend = new File(pathDataBase+"systens/"+"system"+i+"/recommends"+j+".txt");
                FileWriter fileWriter;
                FileReader fileReader;
                BufferedReader bufferedReader;

                try {
                    fileReader = new FileReader(partition);
                    bufferedReader = new BufferedReader(fileReader);
                    String line = bufferedReader.readLine();
                    fileWriter = new FileWriter(recommend);
                    
                    while(line != null) {
                    	String[] args = line.split(";");
                    	
                    	if(args.length < 4) {
                    		//System.out.println("Não existe na base.");
                    		line = bufferedReader.readLine();
                    		continue;
                    	}
                    	int idSession = Integer.parseInt(args[1]);
                    	long init_time = System.currentTimeMillis();
                        List<String> output = syst.run(line);
                        long end_time = System.currentTimeMillis();
                        end_time = end_time - init_time;
                        line = bufferedReader.readLine();
                        System.out.println(cont++);
                        //System.in.read();
                        fileWriter.write(idSession+";"+output+";"+end_time+"\n");
                    }
                    fileWriter.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

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
