package block;

import java.io.*;

import java.util.*;


public class DataSplit {
    private long initTime;
    private long unitTime;
    private File data;
    private File logs;
    private  File articles;
    private long currentTime;
    private String lastArticle;
    private long secondsDay;
    static final String pathArticles = "DataBase/articles/";
    private TreeMap<String,Integer> indices;

    public DataSplit(long initTime, long unitTime, File logs, File data){
        this.data = data;
        this.unitTime = unitTime;
        this.logs = logs;
        currentTime = unitTime;
        this.initTime = initTime;
        this.secondsDay = 0;
        this.indices = new TreeMap<String, Integer>();
    }



    public void run() throws IOException {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        int part = 0;
        Map<String, Long> task = new HashMap<String, Long>();
        try {

            Partition partition = new Partition(part);
            if (new File("DataBase/partition" + part).mkdirs())
                System.out.println("Created dir DataBase/partition" + part);
            fileReader = new FileReader(this.logs);
            bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            int cont = 0;

            while (line != null) {
                System.out.println(cont);
                cont++;
//                if(cont == 100)
//                    break;
                String[] arg = line.split(";");
                //System.out.println(arg[2]+" "+this.getCurrentTime());
                //System.out.println(task);
                task = mountTask(arg, task);
                task = MapSort.sortByValue(task);
                Object out = MapSort.getLast(task);
                Long timeTask = getTimeTask(out);
                System.out.println(timeTask+" "+this.getCurrentTime()+" "+task.size());
                if (timeTask >= this.getCurrentTime()) {
                    part++;

                    task = writePartition(task, partition);
                    partition = new Partition(part);
                    this.setCurrentTime();
                    if (new File("DataBase/partition" + part).mkdirs())
                        System.out.println("Created dir DataBase/partition" + part);
                    //System.in.read();
                }

                    //                } else {
//                    //System.out.println("Escreveu na particao, apenas 1.");
//                    out = MapSort.getFirst(task);
//                    String[] key = ((String) out.toString()).split("=");
//                    String[] id = key[0].split(";");
//                    Long timePublication = getTimePublicationArticle(id[3]);
//                    if (timePublication == null) {
//                        task.remove(key[0]);
//                        continue;
//                    }
//
//                    if (timePublication > 0) {
//                        if (!verifyExists(id[3])) {
//                            partition.setNotification(id[3], timePublication);
//                        }
//                    }
//                    partition.setSession(key[0]);
//                    task.remove(key[0]);
//                }
                line = bufferedReader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Long getTimePublicationArticle(String s) {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        Integer day = this.indices.get(s);
        if(day == null) {
            return null;
        }
        Long timePublication = null;
        try {
            fileReader = new FileReader(pathArticles+day+"/"+s+".txt");
            bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            String[] arg = line.split(";");
            timePublication = Long.parseLong(arg[2]);
        } catch (IOException e) {
            return timePublication;
        }

        return timePublication;
    }

    public  TreeMap<String, Integer> getIndices() {
        return indices;
    }

    public void setIndices(TreeMap<String, Integer> indices) {
        this.indices = indices;
    }

    private static boolean noExists(String s) {
        return false;
    }

    public void insertData(String path) {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        int day = 0;
        try {
            fileReader = new FileReader(path);
            bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            if(new File("DataBase/articles/"+day+"/").mkdirs())
                System.out.println("Created dir DataBase/articles/"+day+"/");

            int cont = 0;
            while (line != null) {
                String[] arg = line.split(";");
                Long timeStamp = Long.parseLong(arg[2]);
                if (timeStamp > this.getSecondsDay()) {
                    day++;
                    if (new File("DataBase/articles/" + day + "/").mkdirs())
                        System.out.println("Created dir DataBase/articles/" + day + "/");
                    this.setSecondsDay();
                }
                writeArticleFile(arg[0], line, day);
                this.indices.put(arg[0], day);
                //System.out.println(this.getCurrentTime()+" "+Long.parseLong(arg[2]));
                //dataBase.insertArticle(line);

                line = bufferedReader.readLine();
                cont++;
                System.out.println(cont);
                System.out.println("Mapsize " + this.indices.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeArticleFile(String s, String line, int day) {
        try {
            File file = new File("DataBase/articles/"+day+"/"+s+".txt");
            FileWriter fileWriter = new FileWriter("DataBase/articles/"+day+"/"+s+".txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(line);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Long getSecondsDay(){
        return this.secondsDay;
    }

    public void setSecondsDay(){
        this.secondsDay += 86400;
    }

    private Map<String, Long> mountTask(String[] arg, Map<String, Long> task) throws IOException {
        // <u1,s1,time1,a1>
        String flagTaks = "";
        for(int i = 3; i < arg.length; i += 4) {
            flagTaks += ";" + arg[i];
            flagTaks += ";" + arg[i+1];
            task.put(arg[0]+";"+arg[1]+flagTaks,Long.parseLong(arg[i]));
            flagTaks = "";
        }
        return task;
    }

    public static ArrayList<File>    mountPartirion(long unitTime){
        ArrayList<File> partitions = new ArrayList<File>();
        return partitions;
    }

    private static Long getTimeTask(Object object){
        String[] key = ((String) object.toString()).split("=");
        String[] id = key[0].split(";");
        return Long.parseLong(id[2]);
    }


    public Map<String,Long> writePartition(Map<String, Long> tasks, Partition partition){
        //Set<String> set = tasks.keySet();
        //Iterator<String> iterator = set.iterator();

        while(tasks.size()  > 0){
            Object object = MapSort.getFirst(tasks);
            String[] key = ((String) object.toString()).split("=");
            String[] arg = key[0].split(";");
            System.out.println(arg[3]);
            Long timePublication = getTimePublicationArticle(arg[3]);
            if(timePublication == null) {

                tasks.remove(key[0]);
                System.out.println("Não existe na base.");
                continue;

            }
            if(timePublication > 0) {
                if(!verifyExists(arg[3]))
                    partition.setNotification(arg[3], timePublication);
            }
            partition.setSession(key[0]);
            tasks.remove(key[0]);
        }
        return tasks;
    }

    private boolean verifyExists(String s) {
        if(this.indices.containsKey(s)){
            return true;
        }
        return false;
    }

    public void setCurrentTime(){
        this.currentTime += this.unitTime;
    }

    public void setUnitTime(long unitTime) {
        this.unitTime = unitTime;
    }

    public void setdata(File data) {
        this.data = data;
    }

    public void setLogs(File logs) {
        this.logs = logs;
    }

    public long getUnitTime() {

        return unitTime;
    }

    public  long getCurrentTime(){
        return currentTime;
    }

    public File getdata() {
        return data;
    }

    public File getLogs() {
        return logs;
    }

    public void setArticles(String line) {
        try {
            FileWriter fileWriter = new FileWriter(this.articles,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(line+"\n");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lastArticle(String id){
        this.lastArticle = id;
    }

    public String getlastArticle() {
        return this.lastArticle;
    }
}
