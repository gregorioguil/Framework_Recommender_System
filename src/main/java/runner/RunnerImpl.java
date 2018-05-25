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


    public RunnerImpl(ArrayList<Recommend> recommends, int numRecommendation){
        this.recommends = recommends;
        this.numRecommendation = numRecommendation;
        //this.partition = partition;
    }

    public void run(){
        Recommend syst = null;
        for(int i = 0; i < this.recommends.size(); i++){
            syst = this.recommends.get(i);
            syst.init(4);
            //syst.getUserIten();
            syst.run();
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
            FileUtils.deleteDirectory(new File("BaseOfData"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
