package runner;




import System_Recommendation.SystemRandom;
import recommend.Recommend;

import java.io.File;
import java.util.ArrayList;

public class Main {

    public static void main(String [] args){
        SystemRandom syst1 = new SystemRandom();
        ArrayList<Recommend> recommends = new ArrayList<Recommend>();
        recommends.add(syst1);
        Runner runner = new Runner(recommends,4);
        File logs = new File("/home/gregorio/Documentos/Guilherme IC/cabeçalho");
        File data = new File("/home/gregorio/Dropbox/Ufop/Iniciação Cientifica 2/DadosUnificados/data.csv");
        runner.definedBase(3600,logs,data);
        runner.run();
        //runner.cleanBase();
//        TestRec t = new TestRec();
//        t.run();
    }
}

