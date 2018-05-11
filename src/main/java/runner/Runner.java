package runner;

import block.DataSplit;
import org.apache.commons.io.FileUtils;
import recommend.Recommend;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Runner {

    private ArrayList<Recommend> recommends;
    private int numRecommendation;
    private String lastArticle;


    public Runner(ArrayList<Recommend> recommends, int numRecommendation){
        this.recommends = recommends;
        this.numRecommendation = numRecommendation;
        //this.partition = partition;
    }

    public int run(){
        Recommend syst = null;
        for(int i = 0; i < this.recommends.size(); i++){
            syst = this.recommends.get(i);
            syst.init();
            syst.run();
        }
        return 0;
    }

    public void definedBase(long unitTime,File logs, File data){
        DataSplit dataSplit = new DataSplit(unitTime,logs,data);
        dataSplit.run();
        this.lastArticle = dataSplit.getlastArticle();
    }

    public void cleanBase(){
        try {
            FileUtils.deleteDirectory(new File("BaseOfData"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
