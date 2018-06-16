package system_recommendation;

import block.MapSort;
import recommend.Recommend;

import java.io.*;
import java.util.*;

public class SystemTopRecommends implements Recommend {
    private String path;
    private Map<Integer,TreeMap<Integer,Double> > article;
    private Map<Integer, Integer> acess;
    private TreeMap<Integer, Set<Integer> > aux;
    private int numRecommend;

    @Override
    public List<String> run(String task) {
        List<String> recommends = new ArrayList<String>();
        System.out.println("Sistema de Recomendação artigos mais acessados.");
        String[] arg = task.split(";");
        Integer idArticle = Integer.parseInt(arg[2]);
        Integer idUser = Integer.parseInt(arg[0]);
        if(arg.length < 5) {
            if(this.article.containsKey(idArticle)){
                this.article.get(idArticle).put(idUser, Double.parseDouble(arg[1]));
            }else{
                this.article.put(idArticle,new TreeMap<Integer,Double>());
                this.article.get(idArticle).put(Integer.parseInt(arg[0]),Double.parseDouble(arg[1]));
            }

            return recommends;
        }
        System.out.println("User:"+arg[0]+", Lendo:"+arg[3]);

        //int day = getDay(Double.parseDouble(arg[2]));
        if(this.acess.containsKey(Integer.parseInt(arg[3]))){
            Integer count  = this.acess.get(Integer.parseInt(arg[3]));
            count++;
            this.acess.put(Integer.parseInt(arg[3]),count);
            if(this.aux.containsKey(Integer.parseInt(arg[0]))){
                this.aux.get(Integer.parseInt(arg[0])).add(Integer.parseInt(arg[3]));
            }else{
                this.aux.put(Integer.parseInt(arg[0]), new TreeSet<Integer>());
                this.aux.get(Integer.parseInt(arg[0])).add(Integer.parseInt(arg[3]));
            }
        }else{
            this.acess.put(Integer.parseInt(arg[3]),1);
        }
        this.acess = MapSort.sortByValue(this.acess);

        if(arg.length > 3){
            Set<Integer> keys = this.acess.keySet();
            Iterator<Integer> iterator = keys.iterator();
            System.out.println("Número de recomendação "+this.numRecommend);
            for(int i = 0; i < this.numRecommend;) {
                if(!iterator.hasNext()) {
                	break;
                }
                idArticle = iterator.next();
                if(!this.aux.containsKey(idArticle)) {
                	recommends.add(idArticle.toString());
                	 i ++;
                }
                System.out.println("Tamanho map Acess "+this.acess.size());
//                try {
//					//System.in.read();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
                //int d = randomDay(day);
                //System.out.println(d);
                //int art = randomArticle(d);
                //recommendations += art+";";
            }
        }

        return recommends;
    }

    @Override
    public void init(int numberOfRecommend, File partition,String path) {
        //int part = 0;
        this.path = path;
        //TreeMap<Integer, Double> articles = new TreeMap<Integer,Double>();
        this.aux = new TreeMap<Integer, Set<Integer>>();
        this.acess = new HashMap<Integer,Integer>();

        this.article = new HashMap<Integer, TreeMap<Integer, Double>>();
        this.numRecommend = numberOfRecommend;
        try {
            //FileWriter fileWriter = new FileWriter(path+"partition"+part+"/recommend.txt");
            FileReader fileReader = new FileReader(partition);
            
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            String line = bufferedReader.readLine();

            int cont = 0;
            while (line != null) {
                System.out.println(cont);
                cont++;


                String[] arg = line.split(";");
                if(arg.length < 5){
                    line = bufferedReader.readLine();
                    continue;
                }
                Integer idUser = Integer.parseInt(arg[0]);
                Integer idArticle = Integer.parseInt(arg[3]);
                if (this.aux.containsKey(idUser)){
                    this.aux.get(idUser).add(idArticle);
                }else{
                    this.aux.put(idUser, new TreeSet<Integer>());
                    this.aux.get(idUser).add(idArticle);
                }
                if(this.acess.containsKey(idArticle)) {
                	Integer countAcess = this.acess.get(idArticle);
                    countAcess++;
                    this.acess.put(idArticle,countAcess);
                }else {
                	this.acess.put(idArticle,1);
                }

                FileReader fileReaderArticle =  new FileReader(path+"articles/"+arg[4]+"/"+arg[3]+".txt");
                BufferedReader bufferedReaderArticle = new BufferedReader(fileReaderArticle);
                String art = bufferedReaderArticle.readLine();
                bufferedReaderArticle.close();
                String[] arg2 = art.split(";");
                if(this.article.containsKey(Integer.parseInt(arg[4]))){
                    this.article.get(Integer.parseInt(arg[4])).put(Integer.parseInt(arg[3]), Double.parseDouble(arg2[2]));
                    
                }else{
                    this.article.put(Integer.parseInt(arg[4]), new TreeMap<Integer, Double>());
                    this.article.get(Integer.parseInt(arg[4])).put(Integer.parseInt(arg[3]), Double.parseDouble(arg2[2]));
                    
                }
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getNews(String article) {

    }
}
