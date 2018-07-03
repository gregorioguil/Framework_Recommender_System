package runner;




import framework.Framework;
import metrics.F1Mesure;
import metrics.MesureCTR;
import system_recommendation.SimilarUser;
import system_recommendation.SystemRandom;
import system_recommendation.SystemTFIDF;
import system_recommendation.SystemTopRecommends;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args){
        long iniTime = 0, unitTime = 604800000;
//        Framework.insertData("/home/gregorio/Documentos/Guilherme IC/logNormalized.csv","/home/gregorio/Documentos/Guilherme IC/data.csv");
//        Framework.runDataSplit(unitTime,iniTime);
        //SystemRandom systemRandom = new SystemRandom();
        //SystemTopRecommends systemTopRecommends = new SystemTopRecommends();
        SystemTFIDF systemTFIDF = new SystemTFIDF();
        //System.out.println("Main");
        //SimilarUser similarUser = new SimilarUser();
        //Framework.insertRecSys(systemRandom);
        //Framework.insertRecSys(systemTopRecommends);
        Framework.insertRecSys(systemTFIDF);
        //Framework.insertRecSys(similarUser);
        Framework.runRunner(4);
        getRecommendsJornal();
        MesureCTR ctr = new MesureCTR("CTR");
        F1Mesure f1 = new F1Mesure("F1");
        Framework.insertMetrics(ctr);
        Framework.insertMetrics(f1);
        Framework.runEvaluator();
    }


    public static void getRecommendsJornal(){
        if(new File("/home/gregorio/Documentos/Guilherme IC/DataBase/systens/Journal/").mkdirs())
            System.out.println("Create Journal directory");
        File file = new File("/home/gregorio/Documentos/Guilherme IC/logNormalized.csv");
        File file1 = new File("/home/gregorio/Documentos/Guilherme IC/DataBase/systens/Journal/recommends.txt");
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            FileWriter fileWriter = new FileWriter(file1);
            String line = bufferedReader.readLine();
            while(line != null){
                String[] arg = line.split(";");
                String out = arg[1];
                for(int i = 6; i < arg.length; i += 4){
                    String rec = arg[i]+";0";

                    fileWriter.write(out+";"+rec+"\n");
                }
                line = bufferedReader.readLine();
            }
            fileWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

