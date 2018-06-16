package runner;




import framework.Framework;
import system_recommendation.SystemRandom;
import system_recommendation.SystemTopRecommends;

public class Main {

    public static void main(String[] args){
        long iniTime = 0, unitTime = 3600000;
//        Framework.insertData("/home/gregorio/Documentos/Guilherme IC/logTest.csv","/home/gregorio/Documentos/Guilherme IC/data.csv");
//        Framework.runDataSplit(unitTime,iniTime);
        SystemRandom systemRandom = new SystemRandom();
        SystemTopRecommends systemTopRecommends = new SystemTopRecommends();
        Framework.insertRecSys(systemRandom);
        Framework.insertRecSys(systemTopRecommends);
        Framework.runRunner(4);

    }
}

