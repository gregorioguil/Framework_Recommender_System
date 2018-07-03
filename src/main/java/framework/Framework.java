package framework;

import block.DataSplitFactory;
import block.DataSplitFactoryImpl;
import evaluate.EvaluateFactory;
import evaluate.EvaluateFactoryImpl;
import metrics.Metrics;
import recommend.Recommend;
import runner.RunnerFactory;
import runner.RunnerFactoryImpl;

import java.io.*;
import java.util.ArrayList;

public class Framework {
    private static DataSplitFactory dataSplitFactory = null;
    private  static RunnerFactory runnerFactory = null;
    private static EvaluateFactory evaluateFactory = null;
    private static ArrayList<Recommend> recommends = new ArrayList<Recommend>();
    private static ArrayList<Metrics> metrics = new ArrayList<Metrics>();
    private static String path = null;
    private static String pathLog = null;
    private static String database = "database.txt";
    private static int numberPartitions;

    public static void insertData(String logs, String data){
        dataSplitFactory = new DataSplitFactoryImpl();
        dataSplitFactory.createDataSplit(logs,data);
        path = data;
        pathLog = logs;
        try {
            FileWriter fileWriter = new FileWriter(new File(database));
            fileWriter.write(data+"\n");
            fileWriter.write(logs+"\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runDataSplit(Long unitTime, Long initTime){

        dataSplitFactory.run(unitTime,initTime);
        numberPartitions = dataSplitFactory.getNumberPartitions();
        try {
            FileWriter fileWriter = new FileWriter(new File(database),true);
            fileWriter.write(numberPartitions+"\n");
            System.out.println("Número de partições "+numberPartitions);
            //System.in.read();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void insertRecSys(Recommend sys){

        recommends.add(sys);
    }

    public static void runRunner(int numberOfRecommend){
        if(path == null){
            try {
                FileReader fileReader = new FileReader(new File(database));
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                path = bufferedReader.readLine();
                pathLog = bufferedReader.readLine();
                numberPartitions = Integer.parseInt(bufferedReader.readLine());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        runnerFactory = new RunnerFactoryImpl();
        runnerFactory.createRunner(recommends,numberOfRecommend,path,numberPartitions);
        runnerFactory.run();
    }

    public static void insertMetrics(Metrics m){
        metrics.add(m);
    }

    public static void runEvaluator(){
        if(path == null){
            try {
                FileReader fileReader = new FileReader(new File(database));
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                path = bufferedReader.readLine();
                pathLog = bufferedReader.readLine();
                numberPartitions = Integer.parseInt(bufferedReader.readLine());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        evaluateFactory = new EvaluateFactoryImpl();
        evaluateFactory.createEvaluate(metrics,path,pathLog,recommends.size(),numberPartitions);
        evaluateFactory.run();
    }
}
