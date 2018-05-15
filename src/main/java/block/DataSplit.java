package block;

import java.io.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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
//        InterfaceDB interfaceDB = new InterfaceDB("framework_journal");
//        interfaceDB.connect();
//        try {
//            System.in.read();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        int part = 0;
        Map<String, Long> task = new HashMap<String, Long>();
        try {
            Partition partition = new Partition(part);
            if(new File("DataBase/partition"+part).mkdirs())
                System.out.println("Created dir DataBase/partition"+part);
            fileReader = new FileReader(this.logs);
            bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            int cont = 0;
            while (line != null){
                System.out.println(cont);
                cont++;

                String[] arg = line.split(";");
                //System.out.println(arg[2]+" "+this.getCurrentTime());

                line = bufferedReader.readLine();

                task = mountTask(arg,task);
                task = MapSort.sortByValue(task);
                Object out = MapSort.getFirst(task);
                String[] key = ((String) out.toString()).split("=");
                System.out.println("Tamanho do map:"+task.size());
                task.remove(key[0]);
                partition.setSession(key[0]);
                if(this.getCurrentTime() <= Long.parseLong(arg[2])){
                    part ++;
                    partition = new Partition(part);
                    this.setCurrentTime();
                    if(new File("DataBase/partition"+part).mkdirs())
                        System.out.println("Created dir DataBase/partition"+part);
                }
                System.out.println("Tamanho do map:"+task.size());
                //System.in.read();
            }
            System.out.println("Inicio da ordenação.");
            task = MapSort.sortByValue(task);
            System.out.println("Acabou de ordenar.");
            fileReader = new FileReader(this.data);
            bufferedReader = new BufferedReader(fileReader);
            line = bufferedReader.readLine();
            if(new File("DataBase/articles").mkdirs())
                System.out.println("Created dir BaseOfData/articles");

            this.articles = new File(pathArticles);
            while (line != null) {
                String[] arg = line.split(";");
                System.out.println(this.getCurrentTime()+" "+Long.parseLong(arg[2]));
                if(this.getCurrentTime() >= Long.parseLong(arg[2])){
                    this.setArticles(line);
                }else {
                    break;
                }
                line = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Map<String, Long> mountTask(String[] arg, Map<String, Long> task) throws IOException {
        // <u1,a1,time1,s1>
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
