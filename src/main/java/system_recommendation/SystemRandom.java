package system_recommendation;



import recommend.Recommend;
import system_recommendation.database.DataBase;

import java.io.*;
import java.util.*;


public class SystemRandom implements Recommend {
    private String path;
    private Map<Integer,TreeMap<Integer,Double> > article;
    private int numRecommend;

    public List<String> run(String line) {
        List<String> recommends = new ArrayList<String>();
        System.out.println("Sistema de Recomendação Randômico.");

        String[] arg = line.split(";");
        if(arg.length < 5) {
            if(this.article.containsKey(Integer.parseInt(arg[2]))){
                this.article.get(Integer.parseInt(arg[2])).put(Integer.parseInt(arg[0]),Double.parseDouble(arg[1]));
            }else{
                this.article.put(Integer.parseInt(arg[2]),new TreeMap<Integer,Double>());
                this.article.get(Integer.parseInt(arg[2])).put(Integer.parseInt(arg[0]),Double.parseDouble(arg[1]));
            }
            //System.out.println("Notificação...");
            return recommends;
        }
        //System.out.println("User:"+arg[0]+", Lendo:"+arg[3]);
        //String recommendations = "";
        int day = getDay(Double.parseDouble(arg[2]));
        if(arg.length > 3){
            for(int i = 0; i < this.numRecommend; i ++) {
                int d = randomDay(day);
                //System.out.println(d);
                Integer art = randomArticle(d);
                recommends.add(art.toString());
            }
        }

        return recommends;
    }



    public int getDay(Double timeStamp){
        int day;
        day = (int) Math.round((timeStamp/86400000));
        //System.out.println("day "+day);
        return day;
    }

    public int randomDay(int day){
        if(day <= 0)
            return 0;
        Random r = new Random();
        return r.nextInt(day);
    }

    public Integer randomArticle(int day){
    	//System.out.println("DAY "+day+" "+this.article.get(day));
        TreeMap<Integer,Double> a = this.article.get(day);
        //System.out.println("size "+a.size());
        Random r = new Random();
        int pos = r.nextInt(a.size());
        //System.out.println("Posicao "+pos);
        Set<Integer> keys = a.keySet();
        Iterator<Integer> it = keys.iterator();
        int cont = 0;
        Integer idArticle = null;
        while(it.hasNext()){
            idArticle = it.next();
            if(cont == pos)
                break;
            cont++;
        }
        return idArticle;
    }

    public void init(int numRecommend, File partition, String path ) {
        int part = 0;
        TreeMap<Integer, Set<Integer> > aux = new TreeMap<Integer, Set<Integer>>();

        this.article = new HashMap<Integer, TreeMap<Integer, Double>>();
        this.numRecommend = numRecommend;
        try {
            //FileWriter fileWriter = new FileWriter(path+"partition"+part+"/recommend.txt");
            FileReader fileReader = new FileReader(partition);
            FileReader fileOut = null;
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            BufferedReader bufOut = null;
            String line = bufferedReader.readLine();

            int cont = 0;
            while (line != null) {
                //System.out.println(cont);
                cont++;


                String[] arg = line.split(";");
                if(arg.length < 5){
                    line = bufferedReader.readLine();
                    continue;
                }

                Integer idUser = Integer.parseInt(arg[0]);
                Integer idArticle = Integer.parseInt(arg[3]);
                Integer day = Integer.parseInt(arg[4]);
                if (aux.containsKey(idUser)){
                    aux.get(idUser).add(idArticle);
                    //dataBase.updateUser(line);
                }else{
                    aux.put(idUser, new TreeSet<Integer>());
                    aux.get(idUser).add(idArticle);

                    //dataBase.insertUser(line);
                }
                //System.out.println(aux.get(idUser));

//                if(arg.length > 3 && !articles.containsKey(Integer.parseInt(arg[3])))
                FileReader fileReaderArticle =  new FileReader(path+"articles/"+day+"/"+idArticle+".txt");
                BufferedReader bufferedReaderArticle = new BufferedReader(fileReaderArticle);
                String art = bufferedReaderArticle.readLine();
                bufferedReaderArticle.close();
                String[] arg2 = art.split(";");
                Double timePublication = Double.parseDouble(arg2[2]);
                if(!this.article.containsKey(day)){
                    this.article.put(day, new TreeMap<Integer, Double>());
                    this.article.get(day).put(idArticle, timePublication);
                }else {
                	this.article.get(day).put(idArticle, timePublication);
                }
                line = bufferedReader.readLine();
            }
//            for(int i = 0; i < 63; i++) {
//            	System.out.println(this.article.get(i));
//            	System.in.read();
//            }
//            Set<Integer> set = aux.keySet();
//            Iterator it = set.iterator();
//            while(it.hasNext()){
//                Integer idUser = (Integer) it.next();
//                fileWriter.write(idUser+","+aux.get(idUser)+"\n");
//            }
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



	@Override
	public void clean() {
		this.article = null;
		
	}


}
