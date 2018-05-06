package main.java.block;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Set;

public class DataSplit {

    private long unitTime;
    private File dados;
    private File logs;
    private long currentTime;

    public DataSplit(long unitTime, File dados, File logs){
        this.dados = dados;
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
            fileReader = new FileReader(this.dados);
            bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null){
                line = bufferedReader.readLine();
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
                String session = mountSession(arg);
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

    public void setDados(File dados) {
        this.dados = dados;
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

    public File getDados() {
        return dados;
    }

    public File getLogs() {
        return logs;
    }
}
