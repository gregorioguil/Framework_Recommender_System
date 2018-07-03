package system_recommendation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import block.MapSort;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.Jaccard;
import recommend.Recommend;

public class SystemTFIDF implements Recommend {
	private TreeMap<Long, Set<Long>> users;
	private String path;
	//<day,(id,timePublication)>
	private HashMap<Integer, TreeMap<Long, Long>> article;
	private HashMap<Long, HashMap<String,Float>> termFrequency;
	private HashMap<String, HashMap<Long,Float> > tf;
	private HashMap<String, HashMap<Float, Set <Long> > > idf;
	private HashMap<Long, HashMap<String,Float> > tf_idf;
	private Map<Long, Map<Long, Double>> similaritys;
	private long numberDocuments;



	public SystemTFIDF(){
		this.termFrequency = new HashMap<Long, HashMap<String,Float>>();
		this.tf = new HashMap<String, HashMap <Long,Float> >();
		this.idf = new HashMap<String, HashMap<Float, Set <Long> > >();
		this.numberDocuments = 0;
		this.tf_idf = new HashMap<Long, HashMap<String,Float>>();
		this.similaritys = new TreeMap<Long, Map<Long, Double>>();
	}
	

	

	@Override
	public List<String> run(String task) {
		// TODO Auto-generated method stub
		List<String> recommends = new ArrayList<String>();

		
		String[] arg = task.split(";");
		Long idUser;
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
				//System.out.println(art);
				if(this.termFrequency.get(idArticle) == null)
					this.termFrequency.put(idArticle,new HashMap<String, Float>());
				this.termFrequency.put(idArticle,freqTerm(this.termFrequency, art));
				//System.out.println("Frequancia de termos para todos artigos\n"+this.termFrequency);
				this.idf = calculateIDF(numberDocuments, art,termFrequency.get(idArticle));
				//System.out.println("IDF\n"+this.idf);
				this.tf = calculateTF(art,termFrequency.get(idArticle));
				//System.out.println("TF\n"+this.tf);
				this.tf_idf = calculateTF_IDF(this.tf_idf);

				similaritys.put(idArticle, calculateSimilarity(idArticle));

				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return recommends;
		}

		idArticle = Long.parseLong(arg[3]);


		if(!similaritys.containsKey(idArticle)) {
			//System.out.println("Calcula Similaridade...");
			similaritys.put(idArticle, calculateSimilarity(idArticle));
		}
		Set<Long> set = similaritys.get(idArticle).keySet();
		Iterator<Long> iterator = set.iterator();

		while(iterator.hasNext()) {
			Long id = iterator.next();
			recommends.add(id.toString());
			if(recommends.size() == 4)
				break;
		}
		
		return recommends;
	}
	
//	private float cosine(HashMap<String,Float> d1,HashMap<String,Float> d2) {
//		
//	}

	private Map<Long,Double> calculateSimilarity(Long idArticle) {
		Map<Long,Double> recommend = new TreeMap<>();
		Cosine cosine = new Cosine();
		
		Set<Long> set = tf_idf.keySet();
		Iterator<Long> iterator = set.iterator();
		
		while(iterator.hasNext()) {
			Long id = iterator.next();
			//cosine.distance(s1, s2);
			//System.out.println(id+" "+idArticle);
			if(tf_idf.get(idArticle) == null || tf_idf.get(id) == null) {
				recommend.put(id,0.0);
				continue;
			}
			if(id.equals(idArticle)) {
				recommend.put(id,0.0);
				continue;
			}
			
			recommend.put(id,cosine.similarity(tf_idf.get(id).toString(), tf_idf.get(idArticle).toString()));
			//id = iterator.next();
		}
		recommend = MapSort.sortByValueDes(recommend);
		Map<Long,Double> recommendAux = new TreeMap<>();
		Set<Long> keys = recommend.keySet();
		Iterator<Long> it = keys.iterator();
		while(it.hasNext()){
			Long v = it.next();
			recommendAux.put(v,recommend.get(v));
			if(recommendAux.size() == 1000)
				break;
		}
		
		return recommendAux;
	}

	// Inicializa base de dados do sistema de recomendação
	@Override
	public void init(int numberOfRecommend, File partition, String path) {
		this.path = path;
		this.users = new TreeMap<Long, Set<Long>>();

		this.article = new HashMap<Integer, TreeMap<Long, Long>>();
		
		try {
			//FileWriter fileWriter = new FileWriter(path+"partition"+part+"/recommend.txt");
			FileReader fileReader = new FileReader(partition);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = bufferedReader.readLine();
			
			int cont = 0;
			while (line != null) {
				
				cont++;

                System.out.println(cont);
				String[] arg = line.split(";");
				if(arg.length < 5){
					line = bufferedReader.readLine();

					continue;
				}

				Long idArticle = Long.parseLong(arg[3]);
				
				Integer day = Integer.parseInt(arg[4]);

				FileReader fileReaderArticle =  new FileReader(this.path+"articles/"+day+"/"+idArticle+".txt");
				BufferedReader bufferedReaderArticle = new BufferedReader(fileReaderArticle);
				String art = bufferedReaderArticle.readLine();
				//System.out.println(art);
				bufferedReaderArticle.close();
				
				//similaritys.put(idArticle, MapSort.sortByValue(calculateSimilarity(idArticle)));
				String[] arg2 = art.split(";");
				Long timePublication = Long.parseLong(arg2[2]);
				if(!this.article.containsKey(day)){
					//System.out.println("Novo");
					
					this.article.put(day, new TreeMap<Long, Long>());
                    this.numberDocuments ++;
                    this.article.get(day).put(idArticle, timePublication);
                    if(this.termFrequency.get(idArticle) == null)
						this.termFrequency.put(idArticle,new HashMap<String, Float>());
                    this.termFrequency.put(idArticle,freqTerm(this.termFrequency, art));
                    //System.out.println(art);
                    //System.out.println("Frequancia de termos para todos artigos\n"+this.termFrequency);
                    //System.out.println(this.numberDocuments);
                    this.idf = calculateIDF(this.numberDocuments, art,this.termFrequency.get(idArticle));
                    //System.out.println("IDF\n"+this.idf);
                    this.tf = calculateTF(art,this.termFrequency.get(idArticle));
                    //System.out.println("TF\n"+this.tf);
                    this.tf_idf = calculateTF_IDF(this.tf_idf);
					
				}else{
                    if(!this.article.get(day).containsKey(idArticle)){
                        this.numberDocuments ++;
                        this.article.get(day).put(idArticle, timePublication);
						if(this.termFrequency.get(idArticle) == null)
							this.termFrequency.put(idArticle,new HashMap<String, Float>());
						this.termFrequency.put(idArticle,freqTerm(this.termFrequency, art));
                        //System.out.println(art);
                        //System.out.println("Frequancia de termos para todos artigos\n"+this.termFrequency);
                        //System.out.println(this.numberDocuments);
						this.idf = calculateIDF(this.numberDocuments, art,this.termFrequency.get(idArticle));
                        //System.out.println("IDF\n"+this.idf);
                        this.tf = calculateTF(art,this.termFrequency.get(idArticle));
                        //System.out.println("TF\n"+this.tf);
                        this.tf_idf = calculateTF_IDF(this.tf_idf);
                    }
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
		// TODO Auto-generated method stub
		
	}

	public HashMap<String, Float> freqTerm(HashMap<Long, HashMap<String, Float>> freq, String article){
		String[] args = article.split(";");
		Long idArticle = Long.parseLong(args[0]);
		for(int i = 18; i < args.length; i++) {
			if(args[i].equals("none"))
				continue;
			String[] word = args[i].split(":");
			float freqT = Float.parseFloat(word[1]);
//			if(this.termFrequency.get(idArticle) == null)
//				this.termFrequency.put(idArticle,new HashMap<>());
			//System.out.println(this.termFrequency.get(idArticle));
			if(!this.termFrequency.get(idArticle).containsKey(word[0])){
				//System.out.println("else");
				this.termFrequency.put(idArticle,new HashMap<>());
				this.termFrequency.get(idArticle).put(word[0],freqT);

			}else{
				//System.out.println("if");
				float freqOld = freq.get(idArticle).get(word[0]);
				this.termFrequency.remove(word[0]);
				this.termFrequency.get(idArticle).put(word[0],(freqOld+freqT));
			}
		}
		HashMap<String, Float> freqAux = new HashMap<String, Float>();
		freqAux = (HashMap<String, Float>) MapSort.sortByValueDes(this.termFrequency.get(idArticle));
		Set<String> set = this.termFrequency.get(idArticle).keySet();
		Iterator<String> it = set.iterator();
		while(it.hasNext()) {
			String t = it.next();
			freqAux.put(t, this.termFrequency.get(idArticle).get(t));
			if(freqAux.size() == 10)
				break;
		}
		return freqAux;
	}

	public HashMap<Long, HashMap<String, Float>> calculateTF_IDF(HashMap<Long, HashMap<String, Float>> tf_idf2){
		Set<String> keys = this.idf.keySet();
		Iterator<String> iterator = keys.iterator();
		while(iterator.hasNext()) {
			String term = iterator.next();
			HashMap<Float, Set<Long>> setArticles = this.idf.get(term);
			Set<Float> set  = setArticles.keySet();
			Iterator<Float> it = set.iterator();
			Float idf_value =  it.next();
			Set<Long> setArticle =  this.idf.get(term).get(idf_value);
			Iterator<Long> itArticle = setArticle.iterator();
			while(itArticle.hasNext()) {
				
				Long idArticle = itArticle.next();
				//.out.println(term+" "+idArticle);
//				if(this.tf.get(term).get(idArticle) == null)
//					continue;
				float tf_value = this.tf.get(term).get(idArticle);
				//float idf_value = this.idf.get(term).get(idArticle);
				float tf_idf = tf_value*idf_value;
				if(this.tf_idf.containsKey(idArticle)) {
					this.tf_idf.get(idArticle).put(term, tf_idf);
				}else {
					this.tf_idf.put(idArticle, new HashMap<String,Float>());
					this.tf_idf.get(idArticle).put(term, tf_idf);
				}
			}
			//this.tf_idf.put(term, value)this.idf.get(term)*this.tf.get(term);
		}
		return tf_idf2;
	}

	public HashMap<String, HashMap<Float, Set<Long>>> calculateIDF(long numberDocuments2, String article, HashMap<String, Float> freqT) {
		//HashMap<String,Float> terms = new HashMap<String,Float>();
		String[] args = article.split(";");
		Long idArticle = Long.parseLong(args[0]);
		Set<String> termos = freqT.keySet();
		Iterator<String> it = termos.iterator();
		int cont = 0;
		while(it.hasNext()) {
			
			cont++;
			String t = it.next();
			float freqTotal = (float) Math.log(numberDocuments2/freqT.get(t));
			//this.idf.get(t).get(freqTotal).add(idArticle);
			if(!this.idf.containsKey(t)) {
				this.idf.put(t, new HashMap<Float, Set <Long> >());
				this.idf.get(t).put(freqTotal, new TreeSet <Long>());
				this.idf.get(t).get(freqTotal).add(idArticle);
				
			}else {
				HashMap<Float, Set<Long>> aux = this.idf.get(t);
				Set<Float> key = aux.keySet();
				Iterator iterator =  key.iterator();
				Float value = (Float) iterator.next();
				Set<Long> id = this.idf.get(t).get(value);
				id.add(idArticle);
				this.idf.get(t).remove(value);
				this.idf.get(t).put(freqTotal, id);
			}
		
		}
		return this.idf;
	}
	
	// Calculate term frequency
	public HashMap<String, HashMap<Long, Float>> calculateTF(String article, HashMap<String, Float> freqT) {
		//HashMap<String,Float> terms = new HashMap<String,Float>();
		String[] args = article.split(";");
		float qtWords = 0;
		Set<String> termos = freqT.keySet();
		Long idArticle = Long.parseLong(args[0]);
		for(int i = 18; i < args.length; i++) {
			//System.out.println("For de cima"+idArticle);
			if(args[i].equals("none"))
				continue;
			String[] word = args[i].split(":");
			qtWords += Float.parseFloat(word[1]);;
		}
		for(int i = 18; i < args.length; i++) {

			if(args[i].equals("none"))
				continue;
			String[] word = args[i].split(":");
			//System.out.println(termos);
//			if(!termos.contains(word[0]))
//				continue;
			float freq = Float.parseFloat(word[1]);

			if(this.tf.containsKey(word[0])) {
				//System.out.println("contain");
				this.tf.get(word[0]).put(idArticle, (freq/qtWords));
				//System.out.println(this.tf.get(word[0]));
			}else {
				//System.out.println("else");
				this.tf.put(word[0], new HashMap<Long,Float>());
				this.tf.get(word[0]).put(idArticle, (freq/qtWords));
			}
//			if(idArticle.equals(14933222)){
//				try {
//					System.in.read();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
		}
		//System.out.println(this.tf);
		return this.tf;
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub
		
	}

}
