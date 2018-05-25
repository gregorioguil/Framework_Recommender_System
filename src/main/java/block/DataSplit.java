package block;

import java.io.*;

import java.util.*;


public class DataSplit {
    private double initTime;
    private double unitTime;
    private File data;
    private File logs;
    private  File articles;
    private double currentTime;
    private String lastArticle;
    private double secondsDay;
    static final String pathArticles = "/home/gregorio/Dropbox/Ufop/Monografia 1/DataBase/articles/";
    private TreeMap<String,String> indices;

    public DataSplit(double initTime, double unitTime, File logs, File data){
        this.data = data;
        this.unitTime = unitTime;
        this.logs = logs;
        currentTime = unitTime;
        this.initTime = initTime;
        this.secondsDay = 0;
        this.indices = new TreeMap<String, String>();
    }



    public void run() throws IOException {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        int part = 0;
        Map<String, Double> task = new HashMap<String, Double>();
        try {


            if (new File("/home/gregorio/Dropbox/Ufop/Monografia 1/DataBase/partition" + part).mkdirs())
                System.out.println("Created dir DataBase/partition" + part);
            Partition partition = new Partition(part);
            fileReader = new FileReader(this.logs);
            bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            String[] arg = line.split(";");
            task = mountTask(arg, task);
            int cont = 0;

            while (line != null) {

                task = MapSort.sortByValue(task);
                Object out = MapSort.getLast(task);
                Double timeTask = getTimeTask(out);
                line = bufferedReader.readLine();
                if(line == null){
                    task = writePartition(task, partition);
                    break;
                }
                System.out.println(cont);
                cont++;
//                if(cont == 100)
//                    break;
                arg = line.split(";");
                if(timeTask <= Double.parseDouble(arg[3]) && task.size() >= 10){
                    task = writePartition(task, partition);
                }else{
                    if(timeTask >= this.getCurrentTime()){
                        part++;

                        task = writePartition(task, partition);
                        partition.closed();
                        partition = null;
                        this.setCurrentTime();
                        if (new File("/home/gregorio/Dropbox/Ufop/Monografia 1/DataBase/partition" + part).mkdirs())
                            System.out.println("Created dir DataBase/partition" + part);
                        partition = new Partition(part);
                    }
                }
                task = mountTask(arg, task);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Double getTimePublicationArticle(String s) {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        String dado = this.indices.get(s);

        if(dado == null) {
            return null;
        }
        String[] day = dado.split(";");
        Double timePublication = null;
        try {
            fileReader = new FileReader(pathArticles+day[0]+"/"+s+".txt");
            bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            String[] arg = line.split(";");
            timePublication = Double.parseDouble(arg[2]);
        } catch (IOException e) {
            return timePublication;
        }

        return timePublication;
    }

    public  TreeMap<String, String> getIndices() {
        return indices;
    }

    public void setIndices(TreeMap<String, String> indices) {
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
            if(new File("/home/gregorio/Dropbox/Ufop/Monografia 1/DataBase/articles/"+day+"/").mkdirs())
                System.out.println("Created dir DataBase/articles/"+day+"/");
            //System.in.read();
            int cont = 0;
            while (line != null) {
                String[] arg = line.split(";");
                Double timeStamp = Double.parseDouble(arg[2]);
                if (timeStamp > this.getSecondsDay()) {
                    day++;
                    if (new File("/home/gregorio/Dropbox/Ufop/Monografia 1/DataBase/articles/" + day + "/").mkdirs())
                        System.out.println("Created dir DataBase/articles/" + day + "/");
                    this.setSecondsDay();
                }
                writeArticleFile(arg[0], line, day);
                if(timeStamp > 0){
                    this.indices.put(arg[0], day+";false");
                }else{
                    this.indices.put(arg[0], day+";true");
                }
                //System.out.println(this.getCurrentTime()+" "+Double.parseDouble(arg[2]));
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
            File file = new File("/home/gregorio/Dropbox/Ufop/Monografia 1/DataBase/articles/"+day+"/"+s+".txt");
            FileWriter fileWriter = new FileWriter("/home/gregorio/Dropbox/Ufop/Monografia 1/DataBase/articles/"+day+"/"+s+".txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(line);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getSecondsDay(){
        return this.secondsDay;
    }

    public void setSecondsDay(){
        this.secondsDay += 86400;
    }

    private Map<String, Double> mountTask(String[] arg, Map<String, Double> task) throws IOException {
        // <u1,s1,time1,a1>
        String flagTaks = "";
        for(int i = 3; i < arg.length; i += 4) {
            flagTaks += ";" + arg[i];
            flagTaks += ";" + arg[i+1];
            task.put(arg[0]+";"+arg[1]+flagTaks,Double.parseDouble(arg[i]));
            flagTaks = "";
        }
        return task;
    }

    public static ArrayList<File>    mountPartirion(Double unitTime){
        ArrayList<File> partitions = new ArrayList<File>();
        return partitions;
    }

    private static Double getTimeTask(Object object){
        String[] key = ((String) object.toString()).split("=");
        String[] id = key[0].split(";");
        return Double.parseDouble(id[2]);
    }


    public Map<String,Double> writePartition(Map<String, Double> tasks, Partition partition) throws IOException {
        System.out.println("Escrita na partição "+partition.getId());
        ArrayList<String> taskOut = new ArrayList<String>();
        while(tasks.size()  > 0){
            Object object = MapSort.getFirst(tasks);
            String[] key = ((String) object.toString()).split("=");
            String[] arg = key[0].split(";");
            //System.out.println(arg[3]);
            Double timePublication = getTimePublicationArticle(arg[3]);
            if(timePublication == null) {
                tasks.remove(key[0]);
                continue;
            }
            if(timePublication > 0) {
                if (!verifyExists(arg[3])) {
                    partition.setNotification(arg[3], timePublication);
                }
            }
            taskOut.add(key[0]);
            tasks.remove(key[0]);
        }
        if(taskOut != null) {
            System.out.println("Esse é o 0:"+taskOut.size());
            partition.setSession(taskOut);
        }
        return tasks;
    }

    private boolean verifyExists(String s) {
        String[] dado = this.indices.get(s).split(";");
        if(dado[1].equals("true")){
            return true;
        }
        this.indices.remove(s);
        this.indices.put(s,dado[0]+";true");
        return false;
    }

    public void setCurrentTime(){
        this.currentTime += this.unitTime;
    }

    public void setUnitTime(Double unitTime) {
        this.unitTime = unitTime;
    }

    public void setdata(File data) {
        this.data = data;
    }

    public void setLogs(File logs) {
        this.logs = logs;
    }

    public double getUnitTime() {

        return unitTime;
    }

    public double getCurrentTime(){
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
