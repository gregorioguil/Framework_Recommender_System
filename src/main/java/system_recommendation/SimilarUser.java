package system_recommendation;

import block.MapSort;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.Jaccard;
import recommend.Recommend;

import java.io.*;
import java.util.*;

public class SimilarUser implements Recommend {
    private String path;
    private Map<Long,List<Long>> users;
    private Map<Long,List<Long>> neighbor;
    private Map<Integer, TreeMap<Long, Long>> article;
    private int numberDocuments;
    private Map<Long, Map<Long, Double> > similaritys;

    /**
     * Receive news items
     *
     * @param task it is a line of partition in the <id_user,id_session,timeStamp,id_article>.
     */
    @Override
    public List<String> run(String task) {
        List<String> recommends = new ArrayList<String>();
        //System.out.println("Sistema de Recomendação Usuários similares.");

        String[] arg = task.split(";");
        Long idUser = null;
        Long idArticle;
        Integer day;
        if(arg.length < 5) {

            idArticle = Long.parseLong(arg[0]);
            Long timePublication = Long.parseLong(arg[1]);
            day = Integer.parseInt(arg[2]);
            if(this.article.containsKey(day)){
                this.article.get(day).put(idArticle,timePublication);
            }else{
                this.article.put(day,new TreeMap<Long,Long>());
                this.article.get(day).put(idArticle,timePublication);
            }
            FileReader fileReaderArticle;
            try {

                fileReaderArticle = new FileReader(this.path+"articles/"+day+"/"+idArticle+".txt");
                BufferedReader bufferedReaderArticle = new BufferedReader(fileReaderArticle);
                String art = bufferedReaderArticle.readLine();
                bufferedReaderArticle.close();


            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return recommends;
        }
        idUser = Long.parseLong(arg[0]);
        idArticle = Long.parseLong(arg[3]);
        day = Integer.parseInt(arg[4]);
        //System.out.println("User:"+idUser+", Lendo:"+idArticle);


        if(!this.users.containsKey(idUser)) {
            this.users.put(idUser, new ArrayList<>());
            if(!this.users.get(idUser).contains(idArticle))
                this.users.get(idUser).add(idArticle);
            this.similaritys.put(idUser, MapSort.sortByValueDes(calculateSimilarity(idUser)));
        }
//        if(!this.similaritys.containsKey(idUser)){
//            this.similaritys.put(idUser, MapSort.sortByValueDes(calculateSimilarity(idUser)));
//        }
        Map<Long, Double> map = similaritys.get(idUser);
        //System.out.println(similaritys.get(idUser).size());
        Set<Long> set = map.keySet();
        for(Long idAux : set) {
            List<Long> list = this.users.get(idAux);
            Random random = new Random();
            Integer index = random.nextInt(list.size());
        	recommends.add(list.get(index).toString());
            if(recommends.size() == 4)
                break;
        }

        return recommends;

    }


    private Map<Long,Double> calculateSimilarity(Long idUser) {
        List<Long> articles = this.users.get(idUser);
        List<Long> idUsers = new ArrayList<>(this.users.keySet());
        Cosine cosine = new Cosine();
        
        Map<Long,Double> recommend = new TreeMap<>();
    	
        for(Long idUser2 : idUsers) {
    		List<Long> list = this.users.get(idUser2);
    		if(!idUser.equals(idUser2))
                recommend.put(idUser2,cosine.similarity(list.toString(),articles.toString()));
    	}
        Map<Long,Double> recommendAux = new TreeMap<>();
		Set<Long> keys = recommend.keySet();
		Iterator<Long> it = keys.iterator();
		while(it.hasNext()){
			Long v = it.next();
			recommendAux.put(v,recommend.get(v));
			if(recommendAux.size() == 100)
				break;
		}

        return recommendAux;
    }

    /**
     * initial the historic data
     *
     * @param numberOfRecommend it  a number of partitions
     * @param partition         it a file of partition
     * @param path
     */
    @Override
    public void init(int numberOfRecommend, File partition, String path) {
        this.similaritys = new TreeMap<Long, Map<Long,Double>>();
        this.path = path;
        this.users = new TreeMap<>();

        this.article = new TreeMap<Integer, TreeMap<Long, Long>>();
        List<Long> artigos = new ArrayList<>();
        try {
            //FileWriter fileWriter = new FileWriter(path+"partition"+part+"/recommend.txt");
            FileReader fileReader = new FileReader(partition);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();

            int cont = 0;
            while (line != null) {

                cont++;
                
                String[] arg = line.split(";");
                if(arg.length < 5){
                	System.out.println(cont);
                    line = bufferedReader.readLine();

                    continue;
                }

                Long idUser = Long.parseLong(arg[0]);
                Long idArticle = Long.parseLong(arg[3]);
                if(!artigos.contains(idArticle));
                artigos.add(idArticle);
                Integer day = Integer.parseInt(arg[4]);
                if (this.users.containsKey(idUser)){
                    if(!this.users.get(idUser).contains(idArticle))
                        this.users.get(idUser).add(idArticle);

                }else{
                    this.users.put(idUser, new ArrayList<Long>());
                    this.users.get(idUser).add(idArticle);
                    this.similaritys.put(idUser, MapSort.sortByValueDes(calculateSimilarity(idUser)));
                }

                FileReader fileReaderArticle =  new FileReader(this.path+"articles/"+day+"/"+idArticle+".txt");
                BufferedReader bufferedReaderArticle = new BufferedReader(fileReaderArticle);
                String art = bufferedReaderArticle.readLine();
                //System.out.println(art);
                bufferedReaderArticle.close();

//                //similaritys.put(idArticle, MapSort.sortByValue(calculateSimilarity(idArticle)));
                String[] arg2 = art.split(";");
                Long timePublication = Long.parseLong(arg2[2]);
                if(!this.article.containsKey(day)){
                    //System.out.println("Novo");
                	
                    this.article.put(day, new TreeMap<Long, Long>());


                }
                this.numberDocuments ++;
                this.article.get(day).put(idArticle, timePublication);



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

    @Override
    public void clean() {

    }
}
