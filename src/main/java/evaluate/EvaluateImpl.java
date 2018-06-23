package evaluate;

import metrics.Metrics;

import java.io.*;
import java.util.*;

public class EvaluateImpl extends Evaluate {
    private ArrayList<Metrics> metrics;
    private File score;
    private File partition;
    private File recommend;
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

        System.out.println("Sistema "+this.numberSystens+" "+this.numberPartitions);
        for(int k = 1; k < this.numberPartitions; k ++ ) {
            this.sessions = new File(this.pathDataBase+"template"+k+".txt");
            Map<Long, List <Long> > listSessions = getListSession(this.sessions);
            for (int j = 0; j < this.numberSystens; j++) {

                this.recommend = new File(this.pathDataBase + "systens/" + "system" + j + "/recommends.txt");
                HashMap<Long, List<ArrayList<Long>>> listRecommends = getListRecommends(this.recommend);
                System.out.println("Metrics size " + this.metrics.size());
                for (int i = 0; i < this.metrics.size(); i++) {
                    String nameMetric = this.metrics.get(i).getName();
                    if(nameMetric == null)
                        nameMetric = "metric"+i;
                    System.out.println("Metrica " + nameMetric);
                    if (new File(this.pathDataBase + "results/" +nameMetric+ "/system" + j + "/").mkdirs())
                        System.out.println("Create directory results.");
                    this.score = new File(this.pathDataBase + "results/" + nameMetric + "/system" + j + "/result.csv");
                    Set<Long> key = listSessions.keySet();
                    Iterator<Long> iterator = key.iterator();
                    Double acres = 0.0;
                    Integer size;
                    try {
                        FileWriter writeScore = new FileWriter(score);
                        while (iterator.hasNext()) {
                            Long idSession = iterator.next();
                            if (listRecommends.get(idSession) == null)
                                continue;

                            System.out.println("List sessions " + idSession + " " + listSessions.get(idSession));
                            System.out.println("List recommends " + idSession + " " + listRecommends.get(idSession));
                            //System.in.read();

                            Double v = this.metrics.get(i).run(listSessions, listRecommends);
                            writeScore.write(idSession + ";" + listSessions.get(idSession).size() + ";" + v + "\n");
                            acres += v;

                        }

                        writeScore.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Double value = acres / listSessions.get()
                    //System.out.println("Metric " + this.metrics.get(i).getName() + "\n" + "Result:" +);
                }

            }
        }
    }

    private HashMap<Long, List<ArrayList<Long>>> getListRecommends(File recommend) {
        HashMap<Long, List<ArrayList<Long>>> recommendMap = new HashMap<>();
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(recommend);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line  = bufferedReader.readLine();
            int cont  = 0;
            while(line != null) {
                //System.out.println(line);
//                cont++;
                
                String[] arg = line.split(";");
                Long idSession = Long.parseLong(arg[0]);
                
                if(arg[1].equals("[]") || arg[2].equals("false")) {
                    line = bufferedReader.readLine();
                    continue;
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
                    //System.out.println("No if e add "+set);
                	recommendMap.get(idSession).add(set);
                }else{
                   //System.out.println("Entrou no else");
                	List <ArrayList <Long>> list = new ArrayList<ArrayList<Long>>();
                    list.add(set);
                    recommendMap.put(idSession,list);
                }
                
                line = bufferedReader.readLine();
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
                line = line.replace(" ","");
                String[] arg = line.split(";");
                Long idSession = Long.parseLong(arg[0]);
                String[] articles = arg[1].split(",");
                ArrayList<Long> set = new ArrayList<Long>();
                Long a = Long.parseLong(articles[0].replace("[", ""));
                if(a != -1)
                    set.add(a);
                for (int i = 1; i < articles.length - 1; i++) {
                    a = Long.parseLong(articles[i]);
                    if(a != -1)
                        set.add(a);
                }
                a = Long.parseLong(articles[articles.length - 1].replace("]", ""));
                if(a != -1)
                    set.add(a);
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