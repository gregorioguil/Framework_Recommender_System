package main.java.runner;

import main.java.block.DataSplit;
import main.java.recommend.Recommend;

import java.io.File;
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

    public void definedBase(long unitTime,File data, File logs){
        DataSplit dataSplit = new DataSplit(unitTime,data,logs);
        dataSplit.run();
    }
}
