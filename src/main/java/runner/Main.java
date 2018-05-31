package runner;




import framework.Framework;
import system_recommendation.SystemRandom;
import recommend.Recommend;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String [] args){
        double iniTime = 0, unitTime = 604800;
//        Framework.insertData("/home/gregorio/Documentos/Guilherme IC/logNormalized.csv","/home/gregorio/Dropbox/Ufop/Iniciação Cientifica 2/DadosUnificados/data.csv");
//        Framework.runDataSplit(unitTime,iniTime);
        SystemRandom systemRandom = new SystemRandom();
        Framework.insertRecSys(systemRandom);
        Framework.runRunner(4);
//        ArrayList<Recommend> recommends = new ArrayList<Recommend>();
//        recommends.add(syst1);
//        Runner runner = new Runner(recommends,4);
//        File logs = new File("/home/gregorio/Documentos/Guilherme IC/logNormalized.csv");
//        File data = new File("/home/gregorio/Dropbox/Ufop/Iniciação Cientifica 2/DadosUnificados/data.csv");
//        try {
//            runner.definedBase(iniTime,unitTime,logs,data);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        runner.run();
        //runner.cleanBase();
//        TestRec t = new TestRec();
//        t.run();
    }
}

