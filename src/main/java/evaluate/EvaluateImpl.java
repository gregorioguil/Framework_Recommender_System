package evaluate;

import metrics.Metrics;

import java.io.*;
import java.util.*;

public class EvaluateImpl extends Evaluate {
    private ArrayList<Metrics> metrics;
    private File score;
    private File partition;
    private RandomAccessFile recommend;
    private String pathDataBase;
    private int numberSystens;
    private int numberPartitions;
    private File sessions;
    private TreeMap<Integer, ArrayList<Integer>> sessionsMap;

    public EvaluateImpl(ArrayList<Metrics> metrics, String path, String pathLog, int numberSystens,int numberPartitions){
        this.metrics = metrics;
        String[] data = path.split("/");
        this.pathDataBase = "";
        for(int i = 0; i < data.length -1; i++)
            this.pathDataBase += "/"+ data[i];

        this.pathDataBase += "/DataBase/";

        this.numberSystens = numberSystens;
        this.numberPartitions = numberPartitions;

    }




    @Override
    public void run() {
        System.out.println("Módulo evaluate running...");
        //executeForJournal();
        System.out.println("Sistema "+this.numberSystens+" "+this.numberPartitions);



        for (int j = 0; j < this.numberSystens; j++) {
            for (int i = 0; i < this.metrics.size(); i++) {
                String nameMetric = this.metrics.get(i).getName();
                if(nameMetric == null)
                    nameMetric = "metric"+i;
                System.out.println("Metrica " + nameMetric);
                if (new File(this.pathDataBase + "results/" + nameMetric + "/system" + j + "/").mkdirs())
                    System.out.println("Create directory results.");
            }
            try {
                this.recommend = new RandomAccessFile(this.pathDataBase + "systens/" + "system" + j + "/recommends.txt","rw");


                for(int k = 1; k < this.numberPartitions; k ++ ) {
                    this.sessions = new File(this.pathDataBase + "templates/template" + k + ".txt");
                    Map<Long, List<Long>> listSessions = getListSession(this.sessions);

                    HashMap<Long, List<ArrayList<Long>>> listRecommends = getListRecommends(this.recommend, listSessions);

                    for (int i = 0; i < this.metrics.size(); i++) {
                        String nameMetric = this.metrics.get(i).getName();
                        if (nameMetric == null)
                            nameMetric = "metric" + i;
                        this.score = new File(this.pathDataBase + "results/" + nameMetric + "/system" + j + "/" + nameMetric + "result.csv");

                        FileWriter writeScore = new FileWriter(score, true);


                        System.out.println("Metrics size " + this.metrics.size());
                        Double value = this.metrics.get(i).run(listSessions, listRecommends);

                        System.out.println(value);
                        writeScore.write(value + "\n");
                        writeScore.close();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void executeForJournal() {
        System.out.println("Execute Jornal");
        Long position;
        for (int i = 0; i < this.metrics.size(); i++) {
            String nameMetric = this.metrics.get(i).getName();
            if (nameMetric == null)
                nameMetric = "metric" + i;
            System.out.println("Metrica " + nameMetric);
            if (new File(this.pathDataBase + "results/" + nameMetric + "/Journal/").mkdirs())
                System.out.println("Create directory results.");
        }
        try {
            this.recommend = new RandomAccessFile(this.pathDataBase + "systens/" + "Journal/recommends.txt","rw");

            for (int k = 1; k < this.numberPartitions; k++) {

                this.sessions = new File(this.pathDataBase + "templates/template" + k + ".txt");
                Map<Long, List<Long>> listSessions = getListSession(this.sessions);
                HashMap<Long, List<ArrayList<Long>>> listRecommends = getListRecommends(this.recommend,listSessions);
                position = this.recommend.getFilePointer();
                System.out.println(position);


                for (int i = 0; i < this.metrics.size(); i++) {


                    String nameMetric = this.metrics.get(i).getName();
                    if (nameMetric == null)
                        nameMetric = "metric" + i;
                    System.out.println("Metrica " + nameMetric);

                    this.score = new File(this.pathDataBase + "results/" + nameMetric + "/Journal/" + nameMetric + "result.csv");
                    FileWriter writeScore = new FileWriter(score,true);
                    System.out.println("Metrics size " + this.metrics.size());
                    Double value = this.metrics.get(i).run(listSessions, listRecommends);

                    System.out.println(value);
                    writeScore.write(value + "\n");
                    writeScore.close();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<Long, List<ArrayList<Long>>> getListRecommends(RandomAccessFile recommend, Map<Long, List<Long>> listSessions) {
        HashMap<Long, List<ArrayList<Long>>> recommendMap = new HashMap<>();
        Map<Long, List<Long>> listMap = new HashMap<>(listSessions);
        Set<Long> set1 = listSessions.keySet();
        for(Long id : set1){

            listMap.put(id,new ArrayList<Long>(listSessions.get(id)));
        }
        System.out.println("Mont recommends");
        FileReader fileReader = null;
        try {

            String line  = recommend.readLine();
            int cont  = 0;
            while(line != null) {

                cont++;
                String[] arg = line.split(";");
                Long idSession = Long.parseLong(arg[0]);
                //System.out.println(cont);
                //System.out.println(listMap.size()+" <--> "+recommendMap.size());
                if(arg[1].equals("[]") || arg[2].equals("false")) {
                    if(!listMap.containsKey(idSession)){

                        line = recommend.readLine();
                        continue;
                    }
                        //System.out.println("else listMap "+listMap.get(idSession));
                    if (listMap.get(idSession).size() > 0){
                        listMap.get(idSession).remove(0);
                        if(listMap.get(idSession).size() == 0)
                            listMap.remove(idSession);
                    }
                    else
                        listMap.remove(idSession);

                    line = recommend.readLine();
                    continue;
                }

                if(!listMap.containsKey(idSession)){

                    line = recommend.readLine();
                    continue;
                }else {
                    //System.out.println("else listMap "+listMap.get(idSession));
                    if (listMap.get(idSession).size() > 0){
                        listMap.get(idSession).remove(0);
                        if(listMap.get(idSession).size() == 0)
                            listMap.remove(idSession);
                    }
                    else
                        listMap.remove(idSession);
                }

                String[] articles = arg[1].split(",");
                ArrayList<Long> set = new ArrayList<>();
                long a = Long.parseLong(articles[0].replace("[", ""));

                set.add(a);
                for (int i = 1; i < articles.length - 1; i++) {
                    a = Long.parseLong(articles[i].replace(" ",""));
                    set.add(a);
                }
                articles[articles.length - 1] = articles[articles.length - 1].replace(" ","");
                a = Long.parseLong(articles[articles.length - 1].replace("]", ""));

                set.add(a);
                if(recommendMap.containsKey(idSession)) {

                	recommendMap.get(idSession).add(set);

                }else{

                	List <ArrayList <Long>> list = new ArrayList<ArrayList<Long>>();
                    list.add(set);
                    recommendMap.put(idSession,list);

                }


                if(recommendMap.size() == listSessions.size())
                    break;
                
                line = recommend.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recommendMap;
    }

    public HashMap<Long, List<Long>> getListSession(File sessions){
        System.out.println("get List Session.");
        HashMap<Long, List<Long>> sessionMap = new HashMap<>();
        try {
            FileReader fileReader = new FileReader(sessions);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line  = bufferedReader.readLine();

            while(line != null) {
                //System.out.println(line);
                line = line.replace(" ","");
                String[] arg = line.split(";");
                Long idSession = Long.parseLong(arg[0]);
                String[] articles = arg[1].split(",");
                if(articles.length == 1) {
                    line = bufferedReader.readLine();
                    continue;
                }
                ArrayList<Long> set = new ArrayList<Long>();
                //System.out.println(line);
                Long a = Long.parseLong(articles[0].replace("[", ""));
                if(a != -1)
                    set.add(a);
                else{
                    line = bufferedReader.readLine();
                    continue;
                }
                for (int i = 1; i < articles.length - 1; i++) {
                    a = Long.parseLong(articles[i]);
                    if(a != -1)
                        set.add(a);
                    else{
                        line = bufferedReader.readLine();
                        continue;
                    }
                }
                a = Long.parseLong(articles[articles.length - 1].replace("]", ""));
                if(a != -1)
                    set.add(a);
                else{
                    line = bufferedReader.readLine();
                    continue;
                }
                sessionMap.put(idSession, set);

                line = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sessionMap;
    }



}