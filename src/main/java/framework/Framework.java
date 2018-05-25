package framework;

import block.DataSplitFactory;
import block.DataSplitFactoryImpl;
import evaluate.Metrics;
import recommend.Recommend;
import runner.RunnerFactory;
import runner.RunnerFactoryImpl;

import java.util.ArrayList;

public class Framework {
    private static DataSplitFactory dataSplitFactory = null;
    private  static RunnerFactory runnerFactory = null;
    private static ArrayList<Recommend> recommends = new ArrayList<Recommend>();
    public static void insertData(String logs, String data){
        dataSplitFactory = new DataSplitFactoryImpl();
        dataSplitFactory.createDataSplit(logs,data);
    }

    public static void runDataSplit(Double unitTime, Double initTime){
        dataSplitFactory.run(unitTime,initTime);
    }

    public static void insertRecSys(Recommend sys){
        recommends.add(sys);
    }

    public static void runRunner(int numberOfRecommend){
        runnerFactory = new RunnerFactoryImpl();
        runnerFactory.createRunner(recommends,numberOfRecommend);
        runnerFactory.run();
    }

    public static void insertMetrics(Metrics metrics){

    }

    public static void runEvaluator(){

    }
}
