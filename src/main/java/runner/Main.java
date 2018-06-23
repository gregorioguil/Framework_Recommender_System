package runner;




import framework.Framework;
import metrics.MesureCTR;
import system_recommendation.SystemRandom;
import system_recommendation.SystemTFIDF;
import system_recommendation.SystemTopRecommends;

public class Main {

    public static void main(String[] args){
        long iniTime = 0, unitTime = 60000;
        Framework.insertData("/home/gregorio/Documentos/Guilherme IC/logTest.csv","/home/gregorio/Documentos/Guilherme IC/data.csv");
        Framework.runDataSplit(unitTime,iniTime);
        SystemRandom systemRandom = new SystemRandom();
        SystemTopRecommends systemTopRecommends = new SystemTopRecommends();
        SystemTFIDF systemTFIDF = new SystemTFIDF();
        Framework.insertRecSys(systemRandom);
        Framework.insertRecSys(systemTopRecommends);
        Framework.insertRecSys(systemTFIDF);
        Framework.runRunner(4);
        MesureCTR ctr = new MesureCTR("CTR");
        Framework.insertMetrics(ctr);
        Framework.runEvaluator();
    }
}

