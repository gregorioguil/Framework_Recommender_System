package system_recommendation;

import recommend.Recommend;
import system_recommendation.database.DataBase;

import java.io.*;
import java.util.*;


public class SystemRandom implements Recommend {
    private String path;
    private Map<Integer,TreeMap<Integer,Double> > article;
    private int numRecommend;

    public void run(int numberPartitions) {

        System.out.println("Sistema de Recomendação Randômico.");
        File partition = new File(path+"partition"+numberPartitions+"/"+"sessions.txt");
        FileReader fileReader;
        BufferedReader bufferedReader;
        try {
            fileReader = new FileReader(partition);
            bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while(line != null){
                String[] arg = line.split(";");
                String recommendations = "";
                int day = getDay(Double.parseDouble(arg[2]));
                if(arg.length > 3){
                    for(int i = 0; i < this.numRecommend; i ++) {
                        int d = randomDay(day);
                        int art = randomArticle(d);
                        recommendations += art+";";
                    }
                    System.out.println(recommendations);
                }
                line = bufferedReader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public int getDay(Double timeStamp){
        int day;
        day = (int) Math.round((timeStamp/86400));

        return day;
    }

    public int randomDay(int day){
        Random r = new Random();
        return r.nextInt(day);
    }

    public Integer randomArticle(int day){
        String art;
        TreeMap<Integer,Double> a = this.article.get(day);
        Random r = new Random();
        int pos = r.nextInt(a.size());
        Set<Integer> keys = a.keySet();
        Iterator<Integer> it = keys.iterator();
        int cont = 0;
        Integer idArticle = null;
        while(it.hasNext()){
            if(cont == pos)
                break;
            idArticle = it.next();
            cont++;
        }
        return idArticle;
    }

    public void init(int numRecommend, File partition, String path) {
        int part = 0;
        this.path = path;
        TreeMap<Integer, Double> articles = new TreeMap<Integer,Double>();
        this.article = new HashMap<Integer, TreeMap<Integer, Double>>();
        this.numRecommend = numRecommend;
        try {
            FileReader fileReader = new FileReader(partition);
            FileReader fileOut = null;
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            BufferedReader bufOut = null;
            String line = bufferedReader.readLine();
            DataBase  dataBase = new DataBase("systemrandom");
            dataBase.connect();
            dataBase.createTableArticles();
            dataBase.createUsers();
            while(line != null){
                System.out.println(line);
                line = bufferedReader.readLine();
                dataBase.insertUser(line);
                String[] arg = line.split(";");
                if(arg.length == 3)
                    articles.put(Integer.parseInt(arg[3]), Double.parseDouble(arg[2]));

            }
            this.article.put(part,articles);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * receive news items
     *
     * @param article it is a line of features in the article.
     */
    @Override
    public void getNews(String article) {

    }


}
