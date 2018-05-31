package runner;


import org.apache.commons.io.FileUtils;
import recommend.Recommend;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

        for(int i = 0; i < this.recommends.size(); i++){
            syst = this.recommends.get(i);
            syst.init(numRecommendation,partition,pathDataBase);
            //syst.getUserIten();
            for(int j = 1; j < this.numberPartitions ; j ++){
                syst.run(j);
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
