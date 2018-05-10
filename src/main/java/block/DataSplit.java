package block;

import java.io.*;

import java.util.ArrayList;


public class DataSplit {

    private long unitTime;
    private File data;
    private File logs;
    private long currentTime;

    public DataSplit(long unitTime, File logs,File data){
        this.data = data;
        this.unitTime = unitTime;
        this.logs = logs;
        currentTime = unitTime;
    }

    public void run() {

        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        int part = 1;

        try {
            Partition partition = new Partition(part);
            if(new File("BaseOfData/partition"+part).mkdirs())
                System.out.println("Created dir BaseOfData/partition"+part);
            fileReader = new FileReader(this.logs);
            bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null){
                partition.setSession(line);
                partition.setRecommendation(line);
                String[] arg = line.split(";");
                System.out.println(arg[2]+" "+this.getCurrentTime());
                if(this.getCurrentTime() <= Long.parseLong(arg[2])){
                    part ++;
                    partition = new Partition(part);
                    this.setCurrentTime();
                    if(new File("BaseOfData/partition"+part).mkdirs())
                        System.out.println("Created dir BaseOfData/partition"+part);
                }
                line = bufferedReader.readLine();
                String session = mountSession(arg);
            }
            fileReader = new FileReader(this.data);
            bufferedReader = new BufferedReader(fileReader);
            line = bufferedReader.readLine();
            if(new File("BaseOfData/partition"+part).mkdirs())
                System.out.println("Created dir BaseOfData/articles");
            while (line != null) {
                line = bufferedReader.readLine();
                String[] arg = line.split(";");
                if(this.getCurrentTime() >= Long.parseLong(arg[2])){
                    partition.setArticles(line);
                }else {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String mountSession(String[] arg) {
        // userId;sizeOfSession;time;article,[article1,article2,article3,article4];time;articleX...
        String session = arg[0];
        return session;
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
}
