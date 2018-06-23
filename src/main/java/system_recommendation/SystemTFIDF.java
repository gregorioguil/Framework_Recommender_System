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
	private TreeMap<Integer, Set<Integer>> users;
	private String path;
	//<day,(id,timePublication)>
	private HashMap<Integer, TreeMap<Integer, Long>> article;
	private HashMap<String,Float> termFrequency;
	private HashMap<String, HashMap<Integer,Float> > tf;
	private HashMap<String, HashMap<Float, Set <Integer> > > idf;
	private HashMap<Integer, HashMap<String,Float> > tf_idf;
	private Map<Integer, Map<Integer,Double>> similaritys;
	private int numberDocuments;



	public SystemTFIDF(){
		this.termFrequency = new HashMap<String,Float>();
		this.tf = new HashMap<String, HashMap <Integer,Float> >();
		this.idf = new HashMap<String, HashMap<Float, Set <Integer> > >();
		this.numberDocuments = 0;
		this.tf_idf = new HashMap<Integer, HashMap<String,Float>>();
		this.similaritys = new TreeMap<Integer, Map<Integer,Double>>();
	}
	
//	public static void main(String[] args) {
//		SystemTFIDF syst = new SystemTFIDF();
//		String task = "4159;4172;3600116;14766847;0;true";
//		String task2 = "4159;4172;3600456;14118154;0;true";
//		String task3 = "4159;4172;3600516;15207514;0;true";
//		String task4 = "4159;4172;3601081;11798914;0;true";
//		String task5 = "4159;4172;3601276;15208290;0;true";
//		File partition = new File("/home/gregorio/Documentos/Guilherme IC/DataBase/partition0/tasks.txt");
//		syst.init(4,partition, "/home/gregorio/Documentos/Guilherme IC/DataBase/");
//		System.out.println(syst.run(task));
//		System.out.println(syst.run(task2));
//		System.out.println(syst.run(task3));
//		System.out.println(syst.run(task4));
//		System.out.println(syst.run(task5));
//	}
	

	@Override
	public List<String> run(String task) {
		// TODO Auto-generated method stub
		List<String> recommends = new ArrayList<String>();
		System.out.println("Sistema de Recomendação TF-IDF.");
		
		String[] arg = task.split(";");
		Integer idUser;
		Integer idArticle;
		Integer day;
		if(arg.length < 5) {
			
			idArticle = Integer.parseInt(arg[0]);
			Long timePublication = Long.parseLong(arg[1]);
			day = Integer.parseInt(arg[2]);
			if(this.article.containsKey(day)){
				this.article.get(day).put(idArticle,timePublication);
			}else{
				this.article.put(day,new TreeMap<Integer,Long>());
				this.article.get(day).put(idArticle,timePublication);
			}
			FileReader fileReaderArticle;
			try {
				
				fileReaderArticle = new FileReader(this.path+"articles/"+day+"/"+idArticle+".txt");
				BufferedReader bufferedReaderArticle = new BufferedReader(fileReaderArticle);
				String art = bufferedReaderArticle.readLine();
				bufferedReaderArticle.close();
				//System.out.println(art);
				this.termFrequency = freqTerm(this.termFrequency, art);
				System.out.println("Frequancia de termos para todos artigos\n"+this.termFrequency);
				this.idf = calculateIDF(numberDocuments, art,termFrequency);
				//System.out.println("IDF\n"+this.idf);
				this.tf = calculateTF(art);
				//System.out.println("TF\n"+this.tf);
				this.tf_idf = calculateTF_IDF(this.tf_idf);
				//System.out.println("TF-IDF\n"+this.tf_idf);
				
				System.out.println("Calculando similaridade...");
				similaritys.put(idArticle, MapSort.sortByValueDes(calculateSimilarity(idArticle)));
				Set<Integer> set = similaritys.get(idArticle).keySet();
				System.out.println(similaritys.get(idArticle).size()+" "+set);
				
				
				//System.out.println("Acabou similaridade...");
				//System.in.read();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return recommends;
		}
		idUser = Integer.parseInt(arg[0]);
		idArticle = Integer.parseInt(arg[3]);
		day = Integer.parseInt(arg[4]);
		System.out.println("User:"+idUser+", Lendo:"+idArticle);
		
		
//			fileReaderArticle = new FileReader(this.path+"articles/"+day+"/"+idArticle+".txt");
//			BufferedReader bufferedReaderArticle = new BufferedReader(fileReaderArticle);
//			String art = bufferedReaderArticle.readLine();
			//System.out.println(art);
//			System.out.println("Calculando similaridade...");
//			similaritys.put(idArticle, MapSort.sortByValue(calculateSimilarity(idArticle)));
//			System.out.println("Acabou similaridade...");
		if(!similaritys.containsKey(idArticle))
			similaritys.put(idArticle, MapSort.sortByValueDes(calculateSimilarity(idArticle)));
		Set<Integer> set = similaritys.get(idArticle).keySet();
		System.out.println(similaritys.get(idArticle).size()+" "+set);
		Iterator<Integer> iterator = set.iterator();
		int cont = 0;
		while(iterator.hasNext()) {
			Integer id = iterator.next();

			cont++;
			//System.out.println(cont+" "+id+" "+similaritys.get(idArticle).get(id));
//				if(users.get(idUser).contains(id))
//					continue;
			recommends.add(id.toString());
			if(recommends.size() == 4)
				break;
		}
	
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		return recommends;
	}
	
//	private float cosine(HashMap<String,Float> d1,HashMap<String,Float> d2) {
//		
//	}

	private Map<Integer,Double> calculateSimilarity(Integer idArticle) {
		Map<Integer,Double> recommend = new TreeMap<>();
		Cosine cosine = new Cosine();
		Jaccard jaccard = new Jaccard();
		Set<Integer> set = tf_idf.keySet();
		Iterator<Integer> iterator = set.iterator();
		while(iterator.hasNext()) {
			Integer id = iterator.next();
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
			//recommend.put(id,jaccard.distance(tf_idf.get(id).toString(), tf_idf.get(idArticle).toString()));
			recommend.put(id,cosine.similarity(tf_idf.get(id).toString(), tf_idf.get(idArticle).toString()));
		}
		recommend = MapSort.sortByValueDes(recommend);
		Set<Integer> setK  = recommend.keySet();
		Iterator<Integer> it = setK.iterator();
		int cont = 0;
//		while(it.hasNext()) {
//			Integer key = it.next();
//			cont++;
//			System.out.println(cont+" "+key+" "+recommend.get(key));
//		
//		}
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return recommend;
	}

	// Inicializa base de dados do sistema de recomendação
	@Override
	public void init(int numberOfRecommend, File partition, String path) {
		this.path = path;
		this.users = new TreeMap<Integer, Set<Integer>>();

		this.article = new HashMap<Integer, TreeMap<Integer, Long>>();
		List<Integer> artigos = new ArrayList<>();
		try {
			//FileWriter fileWriter = new FileWriter(path+"partition"+part+"/recommend.txt");
			FileReader fileReader = new FileReader(partition);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = bufferedReader.readLine();
			
			int cont = 0,teste = 1000000;
			while (line != null) {
				
				cont++;
				if(cont == teste) {
					System.out.println(cont);
					teste += teste;
				}
				String[] arg = line.split(";");
				if(arg.length < 5){
					line = bufferedReader.readLine();

					continue;
				}

				Integer idUser = Integer.parseInt(arg[0]);
				Integer idArticle = Integer.parseInt(arg[3]);
				if(!artigos.contains(idArticle));
					artigos.add(idArticle);
				Integer day = Integer.parseInt(arg[4]);
				if (this.users.containsKey(idUser)){
					this.users.get(idUser).add(idArticle);
					//dataBase.updateUser(line);
				}else{
					this.users.put(idUser, new TreeSet<Integer>());
					this.users.get(idUser).add(idArticle);

					//dataBase.insertUser(line);
				}
				//System.out.println(aux.get(idUser));

//                if(arg.length > 3 && !articles.containsKey(Integer.parseInt(arg[3])))
				//System.out.println("id artigo:"+idArticle+" day:"+day);
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
					this.article.put(day, new TreeMap<Integer, Long>());
				
					
				}
				this.numberDocuments ++;
				this.article.get(day).put(idArticle, timePublication);
				this.termFrequency = freqTerm(this.termFrequency, art);
				//System.out.println(art);
				//System.out.println("Frequancia de termos para todos artigos\n"+this.termFrequency);
				//System.out.println(this.numberDocuments);
				this.idf = calculateIDF(this.numberDocuments, art,this.termFrequency);
				//System.out.println("IDF\n"+this.idf);
				this.tf = calculateTF(art);
				//System.out.println("TF\n"+this.tf);
				this.tf_idf = calculateTF_IDF(this.tf_idf);
				//System.out.println("TF-IDF\n"+this.tf_idf);
				Cosine c =  new Cosine();
				
				//System.in.read();
				line = bufferedReader.readLine();
				
			}
			bufferedReader.close();
			//new PearsonsCorrelation().;
			System.out.println("Calculando similaridade...");
//			for(int i = 0; i < artigos.size(); i++) {
//				System.out.println(i);
//				similaritys.put(artigos.get(i),calculateSimilarity(artigos.get(i)));
//			}

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

	public HashMap<String, Float> freqTerm(HashMap<String, Float> freq, String article){
		String[] args = article.split(";");
		for(int i = 18; i < args.length; i++) {
			if(args[i].equals("none"))
				continue;
			String[] word = args[i].split(":");
			float freqT = Float.parseFloat(word[1]);
			if(this.termFrequency.containsKey(word[0])){
				float freqOld = freq.get(word[0]);
				this.termFrequency.remove(word[0]);
				this.termFrequency.put(word[0],(freqOld+freqT));
			}else{
				this.termFrequency.put(word[0],freqT);
			}
		}
		return this.termFrequency;
	}

	public HashMap<Integer, HashMap<String,Float> > calculateTF_IDF(HashMap<Integer, HashMap<String,Float> > tfidf){
		Set<String> keys = this.idf.keySet();
		Iterator<String> iterator = keys.iterator();
		while(iterator.hasNext()) {
			String term = iterator.next();
			HashMap<Float, Set<Integer>> setArticles = this.idf.get(term);
			Set<Float> set  = setArticles.keySet();
			Iterator<Float> it = set.iterator();
			Float idf_value =  it.next();
			Set<Integer> setArticle =  this.idf.get(term).get(idf_value);
			Iterator<Integer> itArticle = setArticle.iterator();
			while(itArticle.hasNext()) {
				Integer idArticle = itArticle.next();
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
		return tfidf;
	}

	public HashMap<String,HashMap<Float,Set<Integer>>> calculateIDF(int numberDocuments, String article, HashMap<String, Float> freqT) {
		//HashMap<String,Float> terms = new HashMap<String,Float>();
		String[] args = article.split(";");
		Integer idArticle = Integer.parseInt(args[0]);
		for(int i = 18; i < args.length; i++) {
			if(args[i].equals("none")) {
				
				continue;
			}
			String[] word = args[i].split(":");
			float freqTotal = (float) Math.log10(numberDocuments/freqT.get(word[0]));
			if(!this.idf.containsKey(word[0])) {
				this.idf.put(word[0], new HashMap<Float, Set <Integer> >());
				this.idf.get(word[0]).put(freqTotal, new TreeSet <Integer>());
				this.idf.get(word[0]).get(freqTotal).add(idArticle);
				
			}else {
				HashMap<Float, Set<Integer>> aux = this.idf.get(word[0]);
				Set<Float> key = aux.keySet();
				Iterator it =  key.iterator();
				Float value = (Float) it.next();
				Set<Integer> id = this.idf.get(word[0]).get(value);
				id.add(idArticle);
				this.idf.get(word[0]).remove(value);
				this.idf.get(word[0]).put(freqTotal, id);
			}
		}
		return this.idf;
	}
	
	// Calculate term frequency
	public HashMap<String,HashMap<Integer,Float>> calculateTF(String article) {
		//HashMap<String,Float> terms = new HashMap<String,Float>();
		String[] args = article.split(";");
		float qtWords = 0;
		Integer idArticle = Integer.parseInt(args[0]);
		for(int i = 18; i < args.length; i++) {
			if(args[i].equals("none"))
				continue;
			String[] word = args[i].split(":");
			qtWords += Float.parseFloat(word[1]);;
		}
		for(int i = 18; i < args.length; i++) {
			if(args[i].equals("none"))
				continue;
			String[] word = args[i].split(":");
			float freq = Float.parseFloat(word[1]);
			if(this.tf.containsKey(word[0])) {
				this.tf.get(word[0]).put(idArticle, (freq/qtWords));
			}else {
				this.tf.put(word[0], new HashMap<Integer,Float>());
				this.tf.get(word[0]).put(idArticle, (freq/qtWords));
			}
		}
		return this.tf;
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub
		
	}

}
