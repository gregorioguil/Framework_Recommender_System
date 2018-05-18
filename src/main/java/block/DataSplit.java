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
    static final String pathArticles = "DataBase/articles/data.txt";

    public DataSplit(long initTime, long unitTime, File logs, File data){
        this.data = data;
        this.unitTime = unitTime;
        this.logs = logs;
        currentTime = unitTime;
        this.initTime = initTime;
    }

    public void run() {
        System.out.println("Iniciando conexão com o banco de dados...");
        DataBase dataBase = new DataBase("framework_journal");
        dataBase.connect();
        dataBase.createTableArticles();

        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        int part = 0;
        Map<String, Long> task = new HashMap<String, Long>();
        try {
            //inserção de artigos no banco de dados
            fileReader = new FileReader(this.data);
            bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
//            if(new File("DataBase/articles").mkdirs())
//                System.out.println("Created dir DataBase/articles");

            this.articles = new File(pathArticles);
            int cont = 0;
            while (line != null) {
                String[] arg = line.split(";");
                //System.out.println(this.getCurrentTime()+" "+Long.parseLong(arg[2]));
                dataBase.insertArticle(line);
                line = bufferedReader.readLine();
                cont ++;
                System.out.println(cont);

            }


            Partition partition = new Partition(part);
            if(new File("DataBase/partition"+part).mkdirs())
                System.out.println("Created dir DataBase/partition"+part);
            fileReader = new FileReader(this.logs);
            bufferedReader = new BufferedReader(fileReader);
            line = bufferedReader.readLine();
            cont = 0;

            while (line != null) {
                System.out.println(cont);
                cont++;

                String[] arg = line.split(";");
                //System.out.println(arg[2]+" "+this.getCurrentTime());

                line = bufferedReader.readLine();

                task = mountTask(arg, task);
                task = MapSort.sortByValue(task);
                Object out = MapSort.getLast(task);
                Long timeTask = getTimeTask(out);
                if (timeTask <= Long.parseLong(arg[3])) {
                    task = writePartition(task, partition);
                } else {
                    out = MapSort.getFirst(task);
                    String[] key = ((String) out.toString()).split("=");
                    String[] id = key[0].split(";");
                    Long timePublication = getTimePublication(id[3]);
                    if (this.getCurrentTime() <= Long.parseLong(id[2])) {
                        part++;
                        partition = new Partition(part);
                        this.setCurrentTime();
                        if (new File("DataBase/partition" + part).mkdirs())
                            System.out.println("Created dir DataBase/partition" + part);
                    }
                    if (timePublication > 0) {
                        if (noExists(id[3])) {
                            partition.setNotification(id[3], timePublication);
                        }
                    }
                    partition.setSession(key[0]);
                    task.remove(key[0]);
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static boolean noExists(String s) {
        return false;
    }

    private void insertData(String out) {
    }

    public static Long getTimePublication(String s) {
        Long timePublication = Long.valueOf(0);
        return timePublication;
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

    public static Long getTimeTask ( Object object){
        String[] key = ((String) object.toString()).split("=");
        String[] id = key[0].split(";");
        return Long.parseLong(id[2]);
    }


    public static Map<String,Long> writePartition(Map<String,Long> tasks, Partition partition){
        Set<String> set = tasks.keySet();
        Iterator<String> iterator = set.iterator();
        int i = 0;
        while(i < (tasks.size() - 1)){
            String id = iterator.next();
            String[] arg = id.split(";");
            Long timePublication = getTimePublication(arg[3]);
            if(timePublication > 0) {
                if(noExists(arg[3]))
                    partition.setNotification(arg[3], timePublication);
            }
            partition.setSession(id);
            tasks.remove(id);
            i++;
        }
        return tasks;
    }

    public void setCurrentTime(){ this.currentTime += this.unitTime;}

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
