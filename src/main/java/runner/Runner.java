package runner;

import block.DataSplit;
import org.apache.commons.io.FileUtils;
import recommend.Recommend;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Runner {

    private ArrayList<Recommend> recommends;
    private int numRecomendacao;
    //private ArrayList<File> partition;

    public Runner(ArrayList<Recommend> recommends, int numRecomendacao){
        this.recommends = recommends;
        this.numRecomendacao = numRecomendacao;
        //this.partition = partition;
    }

    public int run(){

        return 0;
    }

    public void definedBase(long unitTime,File logs, File data){
        DataSplit dataSplit = new DataSplit(unitTime,logs,data);
        dataSplit.run();
    }

    public void cleanBase(){
        try {
            FileUtils.deleteDirectory(new File("BaseOfData"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
